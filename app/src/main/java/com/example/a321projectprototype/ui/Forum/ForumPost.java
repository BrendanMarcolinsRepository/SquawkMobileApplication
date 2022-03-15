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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ForumPost extends Fragment
{

    private View root;
    private Button comment;
    private TextView topic ,descritpion, username;
    private HomePage homePage;
    private ImageView deleteIcon;
    private NavController navController;

    private RecyclerView recyclerView;
    private AdapterComment adapterComment;
    String usernameString, topicString, descriptionString, postIdString;
    private ArrayList<CommentModel> commentList;

    private ForumModel forumModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

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
        adapterComment = new  AdapterComment(commentList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterComment);



        comment.setOnClickListener(commentMethod);
        deleteIcon.setOnClickListener(deletePostMethod);



        return root;
    }

    private void checkIfCanDelete()
    {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        System.out.println("post id ===============" + postIdString );

        DocumentReference documentReference = firebaseFirestore.collection("posts").document(postIdString);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists())
                    {

                        String databaseName = documentSnapshot.getString("username");

                        System.out.println("names ===============" +usernameString );
                        System.out.println("names ===============" +databaseName );
                        System.out.println(databaseName + "============================= names ===============" +usernameString );
                        if(usernameString.matches(databaseName))
                        {
                            deleteIcon.setVisibility(View.VISIBLE);
                        }

                    }
                }

            }
        });
    }

    private void checker()
    {
        if(getArguments().getSerializable("topic") != null)
        {


            topicString = (String) getArguments().getSerializable("topic");
            usernameString = (String) getArguments().getSerializable("name");
            descriptionString = (String) getArguments().getSerializable("desc");
            postIdString = (String) getArguments().getSerializable("id");




            topic.setText(topicString);
            username.setText(usernameString);
            descritpion.setText(descriptionString);
        }

    }

    private final View.OnClickListener commentMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            System.out.println("worked");
            Bundle bundle = new Bundle();
            bundle.putString("topic",topicString);
            bundle.putString("postId",postIdString);
            navController.navigate(R.id.action_nav_comment_to_commentsPost, bundle);
        }
    };

    private final View.OnClickListener deletePostMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference documentReference = firebaseFirestore.collection("posts").document(postIdString);
            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(homePage,"Your Post Has Been Deleted", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private void setAdaptor1()
    {



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        System.out.println("here ================> ");



        firebaseFirestore.collection("comments").orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override


                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            System.out.println("Error ==========?>" +  error);
                            return;
                        }

                        for(DocumentChange documentChange : value.getDocumentChanges())
                        {
                            if(documentChange.getType() == DocumentChange.Type.ADDED)
                            {
                                CommentModel commentModel = documentChange.getDocument().toObject(CommentModel.class);
                                commentList.add(commentModel);
                            }
                        }
                        adapterComment.notifyDataSetChanged();
                    }
                });
    }

}
