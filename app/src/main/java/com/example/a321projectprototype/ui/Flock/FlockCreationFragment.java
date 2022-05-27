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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
//Coding Done By Brendan Marcolin
public class FlockCreationFragment extends Fragment
{
    private EditText name,description;
    private ImageView flockImage,flockUpdateImage;
    private Button create,update;
    private int SELECT_PICTURE = 200;
    private String flockNameString,flockDescriptionString;
    private HomePage homePage;
    private NavController navController;
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

    //Method to pick a photo from the phone
    private final View.OnClickListener selectPhotoMethod = v -> imageChooser();

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {

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

    //buttons methods to use logic
    private final View.OnClickListener createFlockMethod = v -> confirmNewFlock();

    private final View.OnClickListener updateFlockMethod = v -> updateFlockInformation();

    //confirmed the new flock
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

    //updates the flock information
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

    //file method extension type
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = homePage.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(contentResolver.getType(uri));
    }


    //updates flcck name to the database
    private void updateFlockNameMethod() {

        firebaseFirestore.collection("flocks")
                .document(homePage.getFlockModelData().getFlockId())
                .update("name", flockNameString,"updated_at",getDate())
                .addOnCompleteListener(task -> {
                    homePage.getUserInformation();
                }).addOnFailureListener(e -> {
            failure.onButtonShowPopupWindowClick(getContext());
        });
    }

    //updates flcck description to the database
        private void updateFlockDescriptionMethod(){

            firebaseFirestore.collection("flocks")
                    .document(homePage.getFlockModelData().getFlockId())
                    .update("description", flockDescriptionString,"updated_at",getDate())
                    .addOnCompleteListener(task -> {
                        homePage.getUserInformation();
                    }).addOnFailureListener(e -> {
                failure.onButtonShowPopupWindowClick(getContext());
            });
        }

    //updates flcck image to the database

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
                                        .update("imageUrl",uri.toString(),"updated_at",getDate());
                                homePage.getUserInformation();
                            });
                })).addOnFailureListener(e -> {
            failure.onButtonShowPopupWindowClick(getContext());
        });
    }

    //method pushs data to firebase
    private void uploadFirebaseData(String storage, DocumentReference documentReference){

        ownerUsername = homePage.getName();
        String userID = auth.getCurrentUser().getUid();



        HashMap<String,Object> flockMap = new HashMap<>();
        flockMap.put("userId",userID);
        flockMap.put("flockId",documentReference.getId());
        flockMap.put("name",flockNameString);
        flockMap.put("memberCount",1);
        flockMap.put("imageUrl",storage);
        flockMap.put("description",flockDescriptionString);
        flockMap.put("created_at",getDate());
        flockMap.put("updated_at",getDate());
        documentReference.set(flockMap);


        DocumentReference documentReference1 = firebaseFirestore.collection("flockMembers").document();


        HashMap<String,Object> flockMembersMap = new HashMap<>();
        flockMembersMap.put("flockId",documentReference.getId());
        flockMembersMap.put("userId", auth.getUid());
        flockMembersMap.put("created_at",getDate());
        flockMembersMap.put("updated_at",getDate());
        documentReference1.set(flockMembersMap);


        DocumentReference documentReference2 = firebaseFirestore.collection("flockScore").document(documentReference.getId());

        HashMap<String,Object> flockScoreMap = new HashMap<>();
        flockScoreMap.put("flockname",flockNameString);
        flockScoreMap.put("scorethisweek", 0);
        flockScoreMap.put("scorethismonth", 0);
        flockScoreMap.put("scorethisyear", 0);
        flockScoreMap.put("totalScore", 0);
        flockScoreMap.put("created_at",getDate());
        flockScoreMap.put("updated_at",getDate());

        documentReference2.set(flockScoreMap).addOnSuccessListener(aVoid -> {

            navController = homePage.getNav();
            navController.navigate(R.id.flock_fragment_nav_return);



        });
    }

    //uploads the image
    private void uploadImage(){

        DocumentReference documentReference = firebaseFirestore.collection("flocks").document();
        storageReference.child(documentReference.getId())
        .putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference
                            .child(documentReference.getId())
                            .getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                uploadFirebaseData(uri.toString(), documentReference);

                            });
                });
    }

    //check if a user is in a flock or not
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

    //provides the date
    public String  getDate(){

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("dd-M-yyyy hh:mm:ss");
        String dataString = ft.format(date);

        if(dataString == null || dataString.isEmpty()){
            dataString = "";
        }

        return  dataString;

    }
}
