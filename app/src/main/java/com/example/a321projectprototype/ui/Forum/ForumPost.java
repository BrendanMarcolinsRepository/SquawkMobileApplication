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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.CommentDatabase;
import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.ForumModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ForumPost extends Fragment
{

    private View root;
    private Button comment;
    private TextView topic ,descritpion, username;
    private HomePage homePage;
    private NavController navController;
    private CommentDatabase commentDatabase;
    private RecyclerView recyclerView;
    private AdapterComment adapterComment;
    String usernameString, topicString, descriptionString;
    private ArrayList<CommentModel> commentList;
    private ForumDatabase forumDatabase;
    private ForumModel forumModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_forum_post, container, false);
        comment = root.findViewById(R.id.commentForumComments);
        recyclerView = root.findViewById(R.id.recycleForumComments);

        View view1 = root.findViewById(R.id.forumTopicComment);
        topic = view1.findViewById(R.id.forumTopicRv);
        descritpion = view1.findViewById(R.id.forumDescriptionRv);
        username = view1.findViewById(R.id.forumUserRv);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        forumDatabase = new ForumDatabase(homePage);


        checker();




        commentDatabase = new CommentDatabase(homePage);
        commentList = commentDatabase.getAllUsers(topicString);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterComment = new  AdapterComment(commentList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterComment);



        comment.setOnClickListener(commentMethod);



        return root;
    }

    private void checker()
    {
        if(getArguments().getSerializable("topic") != null)
        {


            topicString = (String) getArguments().getSerializable("topic");

            System.out.println(topicString);

            forumModel = forumDatabase.getFlock(topicString);

            System.out.println(forumModel.getTopic());

            topic.setText(forumModel.getTopic());
            username.setText(forumModel.getUsername());
            descritpion.setText(forumModel.getDescription());
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
            navController.navigate(R.id.action_nav_comment_to_commentsPost, bundle);
        }
    };


}
