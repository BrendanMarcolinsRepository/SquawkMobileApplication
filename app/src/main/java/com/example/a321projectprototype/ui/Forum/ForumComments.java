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

import com.example.a321projectprototype.Database.CommentDatabase;
import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;

public class ForumComments extends Fragment
{

    private View root;
    private Button post;
    private TextView topic ,comment;
    private HomePage homePage;
    private NavController navController;
    private CommentDatabase commentDatabase;
    private RecyclerView recyclerView;
    String topicString, commentString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_forum_comments, container, false);
        topic = root.findViewById(R.id.commentTopicTextview);
        comment = root.findViewById(R.id.commentScroll);
        post = root.findViewById(R.id.commentPostButton);


        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        commentDatabase = new CommentDatabase(homePage);


        topicString = (String) getArguments().getSerializable("topic");
        topic.setText(topicString);




        post.setOnClickListener(postCommentMethod);



        return root;
    }

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
                CommentModel commentModel = new CommentModel(idCount(),homePage.getUserModel().getUsername(),topicString,commentString);
                commentDatabase.addFlock(commentModel);
                Bundle bundle = new Bundle();
                bundle.putString("topic",topicString);
                navController.popBackStack();
            }
        }
    };

    public int idCount()
    {
        int count = commentDatabase.getContactsCount();

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
