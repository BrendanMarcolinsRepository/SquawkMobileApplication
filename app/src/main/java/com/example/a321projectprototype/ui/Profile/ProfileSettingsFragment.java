package com.example.a321projectprototype.ui.Profile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a321projectprototype.FirebaseCustomFailure;
import com.example.a321projectprototype.FirebaseUpdateDocumentField;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileSettingsFragment extends Fragment {

    private View root;
    private TextView usernameTextView, passwordTextView,emailTextView;
    private ImageView userProfileImageView,updateProfileImageButton;
    private UserModel userModel;
    private HomePage homePage;
    private NavController navController;
    private Button profileUpdateButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private String username, email,password;
    private boolean updateSuccess, updateImage = false;
    private final int SELECT_PICTURE = 200;
    private Uri selectedImageUri;
    private FirebaseCustomFailure failure;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        userProfileImageView = root.findViewById(R.id.updateProfileImageView);
        updateProfileImageButton = root.findViewById(R.id.updateProfileImageButton);
        usernameTextView  = root.findViewById(R.id.profileSettingsUsernameEdittext);
        passwordTextView = root.findViewById(R.id.profileSettingsPasswordEdittext);
        emailTextView = root.findViewById(R.id.profileSettingsEmailEdittext);
        profileUpdateButton = root.findViewById(R.id.updateProfileSettingButton);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        homePage = (HomePage) getActivity();
        userModel = homePage.getUserModel();
        navController = homePage.getNav();

        setUpViewVariables();
        setUpCurrentProfileImage();


        profileUpdateButton.setOnClickListener(profileUpdateMethod);
        updateProfileImageButton.setOnClickListener(getNewImage);


        return root;
    }

    private void setUpViewVariables() {

    }

    private void setUpCurrentProfileImage() {

        Glide.with(homePage.getApplicationContext())
                .load(userModel.getPhoto_url())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_profile)
                .into(userProfileImageView);

    }

    private final View.OnClickListener getNewImage = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        }
    };

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
                            .into(userProfileImageView);
                    updateImage = true;
                    updateSuccess = true;
                }
            }
        }
    }


            private final View.OnClickListener profileUpdateMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            checkUserInput();
        }

        private void checkUserInput() {


            username = usernameTextView.getText().toString();
            email = emailTextView.getText().toString();
            password = passwordTextView.getText().toString();
            updateSuccess = false;

            failure = new FirebaseCustomFailure();
            FirebaseUpdateDocumentField firebaseUpdateDocumentField = new FirebaseUpdateDocumentField();
            failure.onButtonShowPopupWindowClick(getContext());

            if (!username.isEmpty()) {
                updateUserNameMethod();
            } else if (!email.isEmpty()) {

                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    updateEmailMethod();
                }else{
                    Toast.makeText(homePage, "Please Enter A Proper Email Address! ", Toast.LENGTH_LONG).show();
                }

            } else if (!password.isEmpty()) {

                if (password.length() > 6) {
                    updatePasswordMethod();
                }else{
                    Toast.makeText(homePage, "Please Enter A Password With More Than 6 Letters! ", Toast.LENGTH_LONG).show();
                }

            } else if (updateImage) {
                updateImageMethod();

            } else {
                Toast.makeText(homePage, "Sorry, Looks like theres nothing to update! ", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(homePage, "Update Complete!", Toast.LENGTH_LONG).show();
        }

        private void updateImageMethod() {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference("UserImages").child(auth.getUid());
            storageReference.delete().addOnCompleteListener(task -> storageReference
                    .putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                    firebaseFirestore.collection("users")
                                            .document(auth.getUid())
                                            .update("photo_Url",uri.toString());
                                    homePage.getUserInformation();
                                });




                    })).addOnFailureListener(e -> {
                failure.onButtonShowPopupWindowClick(getContext());
            });

        }

        private void updatePasswordMethod() {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(userModel.getEmail(), userModel.getPassword());
            FirebaseUser user = auth.getCurrentUser();

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        FirebaseUser user1 = user;
                        user1.updatePassword(password)
                                .addOnCompleteListener(task1 -> {
                                    updateSuccess = true;
                                })
                                .addOnFailureListener(e -> {
                                    failure.onButtonShowPopupWindowClick(getContext());
                            });

                    })
                    .addOnFailureListener(e -> {
                        failure.onButtonShowPopupWindowClick(getContext());
            });


        }

        private void updateEmailMethod() {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(userModel.getEmail(), userModel.getPassword());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            System.out.println("user emial :" + userModel.getEmail());
            System.out.println("user passowrd :" + userModel.getPassword());

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        FirebaseUser user1 = user;
                        user1.updateEmail(email)
                                .addOnCompleteListener(task1 -> {
                                    firebaseFirestore.collection("users")
                                            .document(auth.getUid())
                                            .update("email",email);
                                            homePage.getUserInformation();
                                })
                                .addOnFailureListener(e -> {
                                    failure.onButtonShowPopupWindowClick(getContext());
                                });

                    })
                    .addOnFailureListener(e -> {
                        failure.onButtonShowPopupWindowClick(getContext());
            });


        }

        private void updateUserNameMethod() {

            firebaseFirestore.collection("users")
                    .document(auth.getUid())
                    .update("username",username)
                    .addOnCompleteListener(task -> {
                        homePage.getUserInformation();
                    }).addOnFailureListener(e -> {
                failure.onButtonShowPopupWindowClick(getContext());
            });
        }
    };

}

