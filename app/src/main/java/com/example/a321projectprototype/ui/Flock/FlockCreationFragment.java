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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import static android.app.Activity.RESULT_OK;

public class FlockCreationFragment extends Fragment
{
    private EditText name, description;
    private ImageView flockImage;
    private Switch privateFlockSwitch;
    private Button create, photoButton;
    private int SELECT_PICTURE = 200;
    private String flockNameString,flockDescriptionString;
    private HomePage homePage;
    private NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_flock_create, container, false);

        homePage = (HomePage)getActivity();


        name = root.findViewById(R.id.flock_name_editText);
        description = root.findViewById(R.id.flock_description_editText);
        flockImage = root.findViewById(R.id.flock_create_image);
        create = root.findViewById(R.id.flock_Create_Button);
        photoButton = root.findViewById(R.id.flockCreateButton);

        photoButton.setOnClickListener(selectPhotoMethod);
        create.setOnClickListener(createFlockMethod);




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
           confirmNewFlock();

        }
    };

    private void confirmNewFlock()
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
            FlockModelData flockRequest = new FlockModelData(flockNameString,0,flockDescriptionString);

            navController = homePage.getNav();
            navController.navigate(R.id.flock_fragment_nav_return);
        }
    }
}
