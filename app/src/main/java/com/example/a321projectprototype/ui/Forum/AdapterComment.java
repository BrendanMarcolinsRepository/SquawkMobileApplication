package com.example.a321projectprototype.ui.Forum;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.User.UserModel;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder>
{

    List<CommentModel> dataSet;

    private HomePage homePage;
    private NavController navigation;
    private Button joinButton, infoButton;
    private int picked;
    private Context context;
    private ForumModel forumModel;
    private ForumDatabase forumDatabase;
    private UserModel userModel;
    private String name, countNumber;
    private int count;
    private UserDatabase userDatabase;
    private ConstraintLayout registerLayout;
    private View view;
    private CommentModel currentItem;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView topic, description, username;



        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            topic = itemView.findViewById(R.id.forumTopicRv);
            description = itemView.findViewById(R.id.forumDescriptionRv);
            username = itemView.findViewById(R.id.forumUserRv);


            name = username.getText().toString();

        }

    }

    AdapterComment(List<CommentModel> listItem, HomePage homePage, Context context,  View view)
    {
        this.dataSet = listItem;
        this.homePage = homePage;
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public AdapterComment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_forum_topic, parent, false);
        return new AdapterComment.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterComment.MyViewHolder holder, int position)
    {
        currentItem = dataSet.get(position);
        holder.topic.setText("Topic: " + currentItem.getTopic());
        holder.username.setText("Cherper: " + currentItem.getTopic());
        holder.description.setText(currentItem.getDescription());


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }




}
