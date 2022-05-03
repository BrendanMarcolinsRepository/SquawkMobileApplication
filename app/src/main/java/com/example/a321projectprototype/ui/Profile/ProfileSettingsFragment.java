package com.example.a321projectprototype.ui.Profile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        userProfileImageView = root.findViewById(R.id.updateProfileImageView);
        updateProfileImageButton = root.findViewById(R.id.updateProfileImageButton);
        usernameTextView  = root.findViewById(R.id.profileSettingsUsernameEdittext);
        passwordTextView = root.findViewById(R.id.profileSettingsEmailEdittext);
        emailTextView = root.findViewById(R.id.profileSettingsPasswordEdittext);
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

            if (!username.isEmpty()) {
                updateUserNameMethod();
            } else if (!email.isEmpty()) {
                updateEmailMethod();

            } else if (!password.isEmpty()) {
                updatePasswordMethod();

            } else if (updateImage) {
                updateImageMethod();

            } else {
                Toast.makeText(homePage, "Sorry, Looks like theres nothing to update! ", Toast.LENGTH_LONG).show();
                return;
            }

            if (updateSuccess) {
                Toast.makeText(homePage, "Update Complete!", Toast.LENGTH_LONG).show();

                CountDownTimer countDownTimer = new CountDownTimer(4000, 5) {

                    @Override
                    public void onTick(long millisUntilFinished_) {
                        if (millisUntilFinished_ == 2000) {
                            Toast.makeText(homePage, "Update Complete!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        navController.navigate(R.id.action_nav_Profile_Settings_to_nav_Profile);
                    }
                }.start();
            }
        }

        private void updateImageMethod() {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference("UserImages").child(auth.getUid()+"/");
            storageReference.delete().addOnCompleteListener(task -> storageReference
                    .putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        String urlString = storageReference.getDownloadUrl().toString();
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("users")
                                .document(auth.getUid())
                                .update("photo_Url",urlString);
                        updateSuccess = true;
                    }));

        }

        private void updatePasswordMethod() {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(userModel.getEmail(), userModel.getPassword());
            FirebaseUser user = auth.getCurrentUser();

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.updatePassword(email)
                                    .addOnCompleteListener(task1 -> {
                                        firebaseFirestore.collection("users")
                                                .document(auth.getUid())
                                                .update("password",password);
                                        updateSuccess = true;
                                    });

                        }
                    });


        }

        private void updateEmailMethod() {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(userModel.getEmail(), userModel.getPassword());
            FirebaseUser user = auth.getCurrentUser();

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email)
                                    .addOnCompleteListener(task1 -> {
                                        firebaseFirestore.collection("users")
                                                .document(auth.getUid())
                                                .update("email",email);
                                        updateSuccess = true;
                                    });

                        }
                    });


        }

        private void updateUserNameMethod() {

            firebaseFirestore.collection("users")
                    .document(auth.getUid())
                    .update("username",username)
                    .addOnCompleteListener(task -> {
                        updateSuccess = true;
                    });
        }
    };
}

