package com.example.a321projectprototype.ui.Flock;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.FirebaseCustomFailure;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class FlockCreationFragment extends Fragment
{
    private EditText name,description;
    private ImageView flockImage,flockUpdateImage;
    private Button create,update;
    private int SELECT_PICTURE = 200;
    private String flockNameString,flockDescriptionString;
    private HomePage homePage;
    private NavController navController;
    private ArrayList<FlockModelData> flockModelDataArrayList;
    private UserModel userModel;
    private UserDatabase userDatabase;
    private int flockCount;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private String ownerUsername;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private boolean updateImage = false;
    private FirebaseCustomFailure failure;




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flock_create, container, false);

        homePage = (HomePage)getActivity();




        userModel = homePage.getUserModel();
        userDatabase = new UserDatabase(homePage);



        name = root.findViewById(R.id.flock_name_editText);
        description = root.findViewById(R.id.flock_description_TextView);
        flockImage = root.findViewById(R.id.flock_create_image);
        create = root.findViewById(R.id.flock_Create_Button);
        update = root.findViewById(R.id.flock_update_Button);
        flockUpdateImage = root.findViewById(R.id.uploadFlockImageButton);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("FlockImages/");


        checkFlockName();

        flockUpdateImage.setOnClickListener(selectPhotoMethod);
        create.setOnClickListener(createFlockMethod);
        update.setOnClickListener(updateFlockMethod);




        return root;
    }

    private final View.OnClickListener selectPhotoMethod = v -> imageChooser();

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    Glide.with(homePage.getApplicationContext())
                            .load(selectedImageUri)
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.user_profile)
                            .into(flockImage);
                    updateImage = true;
                }
            }
        }
    }
    private final View.OnClickListener createFlockMethod = v -> confirmNewFlock();

    private final View.OnClickListener updateFlockMethod = v -> updateFlockInformation();


    private void confirmNewFlock() {
        flockNameString = name.getText().toString();
        flockDescriptionString = name.getText().toString();
        if(flockNameString.isEmpty()) {
            name.setError("Please Enter a flock name");
            return;
        } else if(flockDescriptionString.isEmpty()) {
            description.setError("Please Enter a small description");
            return;
        }else if(selectedImageUri == null) {
            return;
        } else {
            uploadImage();

        }
    }

    private void updateFlockInformation() {
        flockNameString = name.getText().toString();
        flockDescriptionString = name.getText().toString();
        failure = new FirebaseCustomFailure();

        if (!flockNameString.isEmpty()) {
            updateFlockNameMethod();
        } else if (!flockDescriptionString.isEmpty()) {
            updateFlockDescriptionMethod();
        } else if (updateImage) {
            updateImageMethod();

        } else {
            Toast.makeText(homePage, "Sorry, Looks like theres nothing to update! ", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(homePage, "Update Complete!", Toast.LENGTH_LONG).show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = homePage.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(contentResolver.getType(uri));
    }


    private void updateFlockNameMethod() {

        firebaseFirestore.collection("flocks")
                .document(homePage.getFlockModelData().getFlockId())
                .update("name", flockNameString)
                .addOnCompleteListener(task -> {
                    homePage.getUserInformation();
                }).addOnFailureListener(e -> {
            failure.onButtonShowPopupWindowClick(getContext());
        });
    }


        private void updateFlockDescriptionMethod(){

            firebaseFirestore.collection("flocks")
                    .document(homePage.getFlockModelData().getFlockId())
                    .update("description", flockDescriptionString)
                    .addOnCompleteListener(task -> {
                        homePage.getUserInformation();
                    }).addOnFailureListener(e -> {
                failure.onButtonShowPopupWindowClick(getContext());
            });
        }

    private void updateImageMethod() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("FlockImages").child(homePage.getFlockModelData().getFlockId());
        storageReference.delete().addOnCompleteListener(task -> storageReference
                .putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                firebaseFirestore.collection("flocks")
                                        .document(homePage.getFlockModelData().getFlockId())
                                        .update("imageUrl",uri.toString());
                                homePage.getUserInformation();
                            });
                })).addOnFailureListener(e -> {
            failure.onButtonShowPopupWindowClick(getContext());
        });
    }

    private void uploadFirebaseData(String storage){

        ownerUsername = homePage.getName();
        String userID = auth.getCurrentUser().getUid();

        Date date = new Date();
        String dateString = String.format("dd-M-yyyy hh:mm:ss", date.getTime());

        DocumentReference documentReference = firebaseFirestore.collection("flocks").document();

        HashMap<String,Object> myMap = new HashMap<>();
        myMap.put("userId",userID);
        myMap.put("flockId",documentReference.getId());
        myMap.put("name",flockNameString);
        myMap.put("memberCount",1);
        myMap.put("flockImage",storage);
        myMap.put("description",flockDescriptionString);
        myMap.put("created_at",dateString);
        myMap.put("updated_at",dateString);
        documentReference.set(myMap);


        DocumentReference documentReference1 = firebaseFirestore.collection("flockMembers").document();
        String flockId = documentReference.getId();

        HashMap<String,Object> myMap1 = new HashMap<>();
        myMap1.put("flockId",flockId);
        myMap1.put("userId", auth.getUid());
        myMap1.put("created_at",dateString);
        documentReference1.set(myMap1);

        HashMap<String,Object> map2 = new HashMap<>();
        myMap1.put("flockname",flockNameString);
        myMap1.put("scorethisweek", 0);
        myMap1.put("scorethismonth", 0);
        myMap1.put("scorethisyear", 0);
        myMap1.put("created_at",dateString);
        myMap1.put("updated_at",dateString);
        documentReference = firebaseFirestore
                .collection("flockScore")
                .document(flockId);
        documentReference.set(map2).addOnSuccessListener(aVoid -> {

            navController = homePage.getNav();
            navController.navigate(R.id.flock_fragment_nav_return);



        });
    }

    private void uploadImage(){
        storageReference.child(flockNameString + getFileExtension(selectedImageUri))
        .putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                uploadFirebaseData(uri.toString());

                            });
                });
    }

    private void checkFlockName() {

        if(homePage.getFlockModelData() != null) {
            create.setVisibility(View.GONE);
            // create.setOnClickListener(null);
            update.setVisibility(View.VISIBLE);

        } else {
            update.setVisibility(View.GONE);
            //  update.setOnClickListener(null);
            create.setVisibility(View.VISIBLE);
        }
    }
}
