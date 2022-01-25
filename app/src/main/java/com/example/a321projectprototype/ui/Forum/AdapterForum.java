package com.example.a321projectprototype.ui.Forum;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.User.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterForum extends RecyclerView.Adapter<AdapterForum.MyViewHolder>
{
    List<ForumModel> FullList;
    List<ForumModel> dataSet;

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
    private ForumModel currentItem;

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

            ForumModel forumModel = forumDatabase.getFlock(name);



            //System.out.println("Flock name 2 " + name);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    picked = getLayoutPosition();
                    System.out.println("worked");
                    Bundle bundle = new Bundle();
                    bundle.putString("topic",currentItem.getTopic());
                    navigation.navigate(R.id.action_nav_forum_to_comment,bundle);
                }
            });

        }

    }

    AdapterForum(List<ForumModel> listItem, HomePage homePage, Context context, ForumDatabase forumDatabase , View view)
    {
        this.dataSet = listItem;
        this.homePage = homePage;
        FullList = new ArrayList<>(listItem);
        this.context = context;
        this.forumDatabase = forumDatabase;
        this.view = view;
    }

    @NonNull
    @Override
    public AdapterForum.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_forum_topic, parent, false);
        return new AdapterForum.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterForum.MyViewHolder holder, int position)
    {
        currentItem = dataSet.get(position);
        holder.topic.setText("Topic: " + currentItem.getTopic());
        holder.username.setText("Cherper: " + currentItem.getTopic());
        holder.description.setText(currentItem.getDescription());


    }



    public Filter getFilter() {
        return Searched_Filter;
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ForumModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ForumModel item : FullList) {
                    if (item.getTopic().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet.clear();
            dataSet.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

    };


}
