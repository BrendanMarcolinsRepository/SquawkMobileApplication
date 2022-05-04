package com.example.a321projectprototype.ui.Flock;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class AdapaterMemberFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder>
{
    protected List<UserModel> FullList;
    protected List<UserModel> dataSet;
    protected List<String> userIds;
    private FlockModelData flockModelData;
    private UserModel userModel;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private int size;
    private  RecyclerView recyclerView;
    private  ProgressBar progressBar;
    private TextView score;

    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView memberName, score;

        MyViewHolder(View itemView)
        {
            super(itemView);

            memberName = itemView.findViewById(R.id.textUsernameTable);
            score = itemView.findViewById(R.id.textScoreTable);


        }

    }

    AdapaterMemberFlock(List<UserModel> listItem, Context context, FlockModelData flockModelData,
                        int size, RecyclerView recyclerView, ProgressBar progressBar,TextView score)
    {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);
        this.context = context;
        this.flockModelData = flockModelData;
        this.size = size;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.score = score;

    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flock_group_table, parent, false);
        return new AdapaterMemberFlock.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder holder, int position)
    {
        userModel = dataSet.get(position);
        getUserScores(userModel, holder);


        holder.memberName.setOnClickListener(v -> {
            if(flockModelData.getUserId().equals(auth.getUid())){
                popUp();
            }
        });



    }

    private void getUserScores(UserModel userModel,MyViewHolder holder) {




        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("userScore").whereEqualTo("userId",userModel.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    String result = task.getResult().getDocuments().get(0).get("totalScore").toString();
                    holder.memberName.setText(userModel.getUsername());
                    holder.score.setText(result);

                    if(size == getItemCount()){
                        recyclerView.setAlpha(1);
                        progressBar.setVisibility(View.GONE);
                        score.setVisibility(View.VISIBLE);

                    }
                });




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void popUp()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.flock_settings_members_items,null);
        alert.setView(mView);

        Button coOwner = (Button) mView.findViewById(R.id.popMembersCoOwner);
        Button  moderator = (Button) mView.findViewById(R.id.popMembersModerator);
        Button  profile = (Button) mView.findViewById(R.id.popMembersUserProfileButton);
        Button  kick = (Button) mView.findViewById(R.id.popMembersKick);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        coOwner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });


        moderator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        kick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
