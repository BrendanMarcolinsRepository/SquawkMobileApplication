package com.example.a321projectprototype.ui.Flock;

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
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import java.util.ArrayList;
import java.util.List;



public class AdapaterMemberFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder>
{
    protected List<FlockMembersModel> FullList;
    protected List<FlockMembersModel> dataSet;


    private int picked;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView memberName, score;

        MyViewHolder(View itemView)
        {
            super(itemView);

            memberName = itemView.findViewById(R.id.memberFockName);
            score = itemView.findViewById(R.id.memberFockScore);

            memberName.setOnClickListener(memberSettings);
        }

        View.OnClickListener memberSettings  = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popUp();
            }
        };
    }

    AdapaterMemberFlock(List<FlockMembersModel> listItem, Context context)
    {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);
        this.context = context;

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
        FlockMembersModel memberFlock = dataSet.get(position);
        holder.memberName.setText(memberFlock.getUsername());
        holder.score.setText(Integer.toString(memberFlock.getScore()));


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
