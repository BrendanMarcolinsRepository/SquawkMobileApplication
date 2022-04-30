package com.example.a321projectprototype.ui.Forum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        ImageView imageDeleteComment;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();




        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            topic = itemView.findViewById(R.id.forumTopicRv);
            description = itemView.findViewById(R.id.forumDescriptionRv);
            username = itemView.findViewById(R.id.forumUserRv);
            name = username.getText().toString();
            imageDeleteComment = itemView.findViewById(R.id.deletePostForum);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picked = getLayoutPosition();
                    System.out.println("worked");
                    Bundle bundle = new Bundle();
                    bundle.putString("topic",dataSet.get(getLayoutPosition()).getTitle());
                    bundle.putString("name",dataSet.get(getLayoutPosition()).getUsername());
                    bundle.putString("desc",dataSet.get(getLayoutPosition()).getDescription());
                    bundle.putString("id",dataSet.get(getLayoutPosition()).getPostId());
                    navigation.navigate(R.id.action_nav_forum_to_comment,bundle);
                }
            });

        }

    }

    AdapterForum(List<ForumModel> listItem, HomePage homePage, Context context, View view)
    {
        this.dataSet = listItem;
        this.homePage = homePage;
        FullList = new ArrayList<>(listItem);
        this.context = context;

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
    public void onBindViewHolder(@NonNull AdapterForum.MyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        currentItem = dataSet.get(position);

        holder.topic.setText("Topic: " + currentItem.getTitle());
        holder.username.setText("Chirper: " + currentItem.getUsername());
        holder.description.setText(currentItem.getDescription());

        if(currentItem.getUserId().matches(holder.firebaseAuth.getUid()))
        {
            holder.imageDeleteComment.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imageDeleteComment.setVisibility(View.GONE);
        }

        holder.imageDeleteComment.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                String commentID = dataSet.get(position).getPostId();
                DocumentReference documentReference = firebaseFirestore.collection("forums").document(commentID);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            dataSet.remove(position);
                            updateData(dataSet);
                            System.out.println("worked =====================================================================");
                            Toast.makeText(homePage.getBaseContext(),"Comment Deleted",Toast.LENGTH_LONG);

                        }
                        else
                        {
                            Toast.makeText(homePage.getBaseContext(),"Please Try Again Later",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });

    }

    public void updateData(List dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
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
            String s = constraint.toString();
            System.out.println("worked 1");
            if (s.isEmpty()) {
                filteredList.addAll(FullList);
                System.out.println("worked 2");
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                System.out.println("worked 4" + FullList.size());
                for (ForumModel item : FullList) {
                    System.out.println("title: " + item.getTitle().toLowerCase());
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
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
