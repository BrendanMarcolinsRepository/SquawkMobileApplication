package com.example.a321projectprototype.ui.Forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.User.UserModel;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder>
{

    List<CommentModel> dataSet;

    private HomePage homePage;
    private NavController navigation;
    private Button joinButton, infoButton;

    private Context context;

    private ConstraintLayout registerLayout;
    private View view;
    private CommentModel commentModel;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView topic, description, username;



        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            description = itemView.findViewById(R.id.comnentRv);
            username = itemView.findViewById(R.id.commentUserHeaderRv);




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


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_forum_comments, parent, false);
        return new AdapterComment.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterComment.MyViewHolder holder, int position)
    {
        commentModel = dataSet.get(position);
        holder.username.setText("Cherper: " + commentModel.getUsername());
        holder.description.setText(commentModel.getContent());


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }




}
