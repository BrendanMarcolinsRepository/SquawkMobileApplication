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
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ForumComments extends Fragment
{

    private View root;
    private Button post;
    private TextView topic ,comment;
    private HomePage homePage;
    private NavController navController;

    private RecyclerView recyclerView;
    String topicString, commentString,postId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_forum_comments, container, false);
        topic = root.findViewById(R.id.commentTopicTextview);
        comment = root.findViewById(R.id.commentScroll);
        post = root.findViewById(R.id.commentPostButton);


        homePage = (HomePage) getActivity();
        navController = homePage.getNav();


        topicString = (String) getArguments().getSerializable("topic");
        postId = (String) getArguments().getSerializable("postId");
        topic.setText(topicString);




        post.setOnClickListener(postCommentMethod);



        return root;
    }

    //method to provide logic for adding comments to topics
    private final View.OnClickListener postCommentMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            commentString = comment.getText().toString();

            if(commentString.isEmpty())
            {
                comment.setError("Last name is requried");
                comment.requestFocus();
            }
            else
            {

                //pushs the comments to the database in firebase
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                String userID = auth.getCurrentUser().getUid();
                DocumentReference documentReference2 = firebaseFirestore.collection("users").document(userID);



                DocumentReference documentReference = firebaseFirestore.collection("comments").document();

                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
                String dateString = ft.format(date);


                HashMap<String,Object> userMap = new HashMap<>();
                userMap.put("created_at",dateString);
                userMap.put("content",commentString);
                userMap.put("updated_at", dateString);
                userMap.put("userId", userID);
                userMap.put("comment_id", documentReference.getId());
                userMap.put("post_id", postId);
                userMap.put("username", homePage.getUserModel().getUsername());


                documentReference.set(userMap).addOnSuccessListener(aVoid -> {
                });

                //Navigator
                Bundle bundle = new Bundle();
                bundle.putString("topic",topicString);
                navController.popBackStack();
            }
        }
    };


}
