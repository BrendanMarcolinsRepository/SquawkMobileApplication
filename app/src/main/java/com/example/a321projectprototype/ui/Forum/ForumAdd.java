package com.example.a321projectprototype.ui.Forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ForumModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ForumAdd extends Fragment {

    private View root;
    private Button addPost;
    private TextView topic ,descritpion;
    private HomePage homePage;
    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    String userID,username;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_forum_add, container, false);
        topic = root.findViewById(R.id.forumAddTopic);
        descritpion = root.findViewById(R.id.forumAddDesciption);
        addPost = root.findViewById(R.id.forumAddPost);
        homePage = (HomePage) getActivity();






        navController = homePage.getNav();

        addPost.setOnClickListener(addForum);



        return root;
    }

    //logic to create a forum
    private final View.OnClickListener addForum = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String topicString = topic.getText().toString();
            String descriptionString = descritpion.getText().toString();

            if(topicString.isEmpty()) {
                topic.setError("Please Enter A Topic");
                topic.requestFocus();
            } else if(descriptionString.isEmpty()) {
                descritpion.setError("Please Enter A Description");
                descritpion.requestFocus();
            } else {



                //logic to push data input to database in firebase and return back to previous fragment
                auth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();

                userID = auth.getCurrentUser().getUid();

                DocumentReference documentReference = firebaseFirestore.collection("posts").document();

                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
                String dateString = ft.format(date);

                HashMap<String,Object> userMap = new HashMap<>();
                userMap.put("created_at",dateString);
                userMap.put("description",descriptionString);
                userMap.put("title",topicString);
                userMap.put("updated_at", dateString);
                userMap.put("userId", userID);
                userMap.put("postId", documentReference.getId());
                userMap.put("username", homePage.getUserModel().getUsername());


                documentReference.set(userMap).addOnSuccessListener(aVoid -> System.out.println("worked"));

                navController.navigate(R.id.action_nav_add_to_forum);
            }





        }

    };


}
