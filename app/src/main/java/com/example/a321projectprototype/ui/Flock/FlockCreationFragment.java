package com.example.a321projectprototype.ui.Flock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class FlockCreationFragment extends Fragment
{
    private EditText name, description;
    private ImageView flockImage;
    private Switch privateFlockSwitch;
    private Button create, photoButton,update;
    private int SELECT_PICTURE = 200;
    private String flockNameString,flockDescriptionString;
    private boolean privateFlock = true;
    private HomePage homePage;
    private NavController navController;
    private ArrayList<FlockModelData> flockModelDataArrayList;
    private FlockDatabase flockDatabase;
    private UserModel userModel;
    private UserDatabase userDatabase;
    private int flockCount;
    private FirebaseFirestore firebaseFirestore;
    private String ownerUsername;




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_flock_create, container, false);

        homePage = (HomePage)getActivity();



        userModel = homePage.getUserModel();
        userDatabase = new UserDatabase(homePage);


        name = root.findViewById(R.id.flock_name_editText);
        description = root.findViewById(R.id.flock_description_editText);
        flockImage = root.findViewById(R.id.flock_create_image);
        create = root.findViewById(R.id.flock_Create_Button);
        photoButton = root.findViewById(R.id.flockCreateButton);
        privateFlockSwitch = root.findViewById(R.id.flock_create_private_switch);
        update = root.findViewById(R.id.flock_update_Button);

        firebaseFirestore = FirebaseFirestore.getInstance();

        checkFlockName();

        photoButton.setOnClickListener(selectPhotoMethod);
        create.setOnClickListener(createFlockMethod);
        update.setOnClickListener(updateFlockMethod);




        return root;
    }

    private final View.OnClickListener selectPhotoMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            imageChooser();
        }
    };

    void imageChooser()
    {

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
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    flockImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
    private final View.OnClickListener createFlockMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
           confirmNewFlock(v);

        }
    };

    private final View.OnClickListener updateFlockMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            confirmNewFlock(v);

        }
    };

    private void confirmNewFlock(View v)
    {
        flockNameString = name.getText().toString();
        flockDescriptionString = name.getText().toString();
        if(flockNameString.isEmpty())
        {
            name.setError("Please Enter a flock name");
        }
        else if(flockDescriptionString.isEmpty())
        {
            description.setError("Please Enter a small description");
        }
        else
        {
            ownerUsername = homePage.getName();
            privateFlock = privateFlockSwitch.getSplitTrack();



            DocumentReference documentReference = firebaseFirestore.collection("Flock").document();
            FlockModelData flockModelData = new FlockModelData(flockNameString,0,flockDescriptionString,privateFlock,ownerUsername,0);

            HashMap<String,Object> myMap = new HashMap<>();
            myMap.put("name",flockNameString);
            myMap.put("groupNumber",  1);
            myMap.put("description",flockDescriptionString);
            myMap.put("ownerUsername",ownerUsername);
            myMap.put("privateFlock",privateFlock);
            myMap.put("score",0);

            documentReference.collection("Flock").document("4OIcTerZfrxWSLMZYX1O")
                    .set(myMap).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {


                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });


            DocumentReference documentReference1 = firebaseFirestore.collection("flockMember").document();


            HashMap<String,Object> myMap1 = new HashMap<>();
            myMap1.put("name",flockNameString);
            myMap1.put("ownerUsername", ownerUsername);

            documentReference1.collection("flockMembers").document()
                    .set(myMap1).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    navController = homePage.getNav();
                    navController.navigate(R.id.flock_fragment_nav_return);

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });
        }



    }

    private void checkFlockName()
    {
        DocumentReference  documentReference = firebaseFirestore.collection("flockMembers").document("4OIcTerZfrxWSLMZYX1O");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists())
                    {

                        if(userModel.getUsername() != task.getResult().getString("ownerUsername"))
                        {

                            create.setVisibility(View.GONE);
                            create.setOnClickListener(null);
                            update.setVisibility(View.VISIBLE);


                        }
                        else
                        {
                            update.setVisibility(View.GONE);
                            update.setOnClickListener(null);
                            create.setVisibility(View.VISIBLE);

                        }

                    }
                    else
                    {
                        System.out.println("no document");
                    }
                }
                else
                {
                    System.out.println("not successfull");

                }
            }
        });

    }
}
