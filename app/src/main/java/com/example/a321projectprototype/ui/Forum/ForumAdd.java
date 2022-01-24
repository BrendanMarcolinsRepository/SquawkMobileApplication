package com.example.a321projectprototype.ui.Forum;

import android.app.AlertDialog;
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

import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.ui.Discover.AdapterDiscover;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

public class ForumAdd extends Fragment
{

    private View root;
    private Button addPost;
    private TextView topic ,descritpion;
    private HomePage homePage;
    private NavController navController;
    private ForumDatabase forumDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_forum_add, container, false);
        topic = root.findViewById(R.id.forumAddTopic);
        descritpion = root.findViewById(R.id.forumAddDesciption);
        addPost = root.findViewById(R.id.forumAddPost);
        homePage = (HomePage) getActivity();
        forumDatabase = new ForumDatabase(homePage);
        navController = homePage.getNav();

        addPost.setOnClickListener(addForum);



        return root;
    }

    private final View.OnClickListener addForum = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String topicString = topic.getText().toString();
            String descriptionString = descritpion.getText().toString();

            if(topicString.isEmpty())
            {
                topic.setError("Please Enter A Topic");
                topic.requestFocus();
            }
            else if(descriptionString.isEmpty())
            {
                descritpion.setError("Please Enter A Description");
                descritpion.requestFocus();
            }
            else
            {
                ForumModel forumModel = new ForumModel(idCount(),homePage.getUserModel().getUsername(),topicString,descriptionString);
                forumDatabase.addFlock(forumModel);
                navController.navigate(R.id.action_nav_add_to_forum);
            }
        }
    };

    public int idCount()
    {
        int count = forumDatabase.getContactsCount();

        if(count == 0)
        {
            return count;
        }
        else
        {
            return count + 1;

        }
    }
}
