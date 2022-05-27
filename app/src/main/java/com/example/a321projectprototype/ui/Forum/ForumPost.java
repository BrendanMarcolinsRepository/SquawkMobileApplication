package com.example.a321projectprototype.ui.Forum;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.LoginPackage.Prototype;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.ForumModel;
import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ForumPost extends Fragment {

    private View root;
    private Button comment;
    private TextView topic, descritpion, username;
    private HomePage homePage;
    private ImageView deleteIcon;
    private NavController navController;

    private RecyclerView recyclerView;
    private AdapterComment adapterComment;
    String usernameString, topicString, descriptionString, postIdString;
    private ArrayList<CommentModel> commentList;

    private ForumModel forumModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_forum_post, container, false);
        comment = root.findViewById(R.id.commentForumComments);
        recyclerView = root.findViewById(R.id.recycleForumComments);
        deleteIcon = root.findViewById(R.id.deletePostForum);
        deleteIcon.setVisibility(View.INVISIBLE);

        View view1 = root.findViewById(R.id.forumTopicComment);
        topic = view1.findViewById(R.id.forumTopicRv);
        descritpion = view1.findViewById(R.id.forumDescriptionRv);
        username = view1.findViewById(R.id.forumUserRv);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        commentList = new ArrayList<>();

        checker();
        checkIfCanDelete();
        setAdaptor1();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterComment = new AdapterComment(commentList, homePage, getContext(), root);
        recyclerView.setAdapter(adapterComment);


        comment.setOnClickListener(commentMethod);
        deleteIcon.setOnClickListener(deletePostMethod);


        return root;
    }

    //check if the user can delete a post
    private void checkIfCanDelete() {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("posts").document(postIdString);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot.exists()) {

                    String databaseName = documentSnapshot.getString("username");

                    if (usernameString.matches(databaseName)) {
                        deleteIcon.setVisibility(View.VISIBLE);
                    }

                }
            }

        });
    }


// updates what the user can see on the user interface
    private void checker() {
        if (getArguments().getSerializable("topic") != null) {


            topicString = (String) getArguments().getSerializable("topic");
            usernameString = (String) getArguments().getSerializable("name");
            descriptionString = (String) getArguments().getSerializable("desc");
            postIdString = (String) getArguments().getSerializable("id");


            topic.setText(topicString);
            username.setText(usernameString);
            descritpion.setText(descriptionString);
        }

    }

    //Navigator
    private final View.OnClickListener commentMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            System.out.println("worked");
            Bundle bundle = new Bundle();
            bundle.putString("topic", topicString);
            bundle.putString("postId", postIdString);
            navController.navigate(R.id.action_nav_comment_to_commentsPost, bundle);
        }
    };

    //delete post and comment from database
    private final View.OnClickListener deletePostMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            //deletes specific post from database
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference documentReference = firebaseFirestore.collection("posts").document(postIdString);
            documentReference.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(homePage,"Your Post Has Been Deleted",Toast.LENGTH_LONG);
                    System.out.println("worked 1");
                }
            });


            //deletes all comments associated with the post
            CollectionReference collectionReference = firebaseFirestore.collection("comments");
            Query query = collectionReference.whereEqualTo("post_id", postIdString);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if(documentSnapshot.getString("post_id").equals(postIdString)) {
                            collectionReference.document(documentSnapshot.getId()).delete();
                        }

                    }

                    System.out.println("worked 2");
                }

            });

        }
    };


    //sets the adaptor for the recycle view
    private void setAdaptor1() {


        //retrieves  data of all comments for the associate post
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("comments").orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {

                            CommentModel commentModel = documentChange.getDocument().toObject(CommentModel.class);
                            if(commentModel.getpost_id().equals(postIdString)) {
                                commentList.add(commentModel);
                            }
                        }
                    }



                    adapterComment.notifyDataSetChanged();
                });
    }
}


