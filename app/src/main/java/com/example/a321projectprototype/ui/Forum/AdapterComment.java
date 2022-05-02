package com.example.a321projectprototype.ui.Forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        ImageView imageDeleteComment;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            description = itemView.findViewById(R.id.comnentRv);
            username = itemView.findViewById(R.id.commentUserHeaderRv);
            imageDeleteComment = itemView.findViewById(R.id.deleteCommentForum);




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

        if(commentModel.getUserId().matches(holder.firebaseAuth.getUid()))
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

                String commentID = dataSet.get(position).getComment_id();
                DocumentReference documentReference = firebaseFirestore.collection("comments").document(commentID);
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

    @Override
    public int getItemCount() {
        return dataSet.size();
    }





}
