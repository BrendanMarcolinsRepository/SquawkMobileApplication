package com.example.a321projectprototype.ui.Record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
//Code by Brendan Marcolin

public class RecordDataCardViewAdapter extends RecyclerView.Adapter<RecordDataCardViewAdapter.MyViewHolder> {
    List<RewardPointsModel> rewardPointsModels ;
    List<BirdRewardModel> listItem;
    HomePage homePage;
    int position;

    RecordDataCardViewAdapter(List<BirdRewardModel> listItem, HomePage homePage, List<RewardPointsModel> rewardPointsModels) {
        this.homePage = homePage;
        this.listItem = listItem;
        this.rewardPointsModels = rewardPointsModels;

    }
    @NonNull
    @Override
    public RecordDataCardViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_data_retrival_cardview,parent,false);

        return new RecordDataCardViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordDataCardViewAdapter.MyViewHolder holder, int position) {
        //setups the user interface data passed in the list in the constructor
        holder.identifiedBirdTexview.setText("Identified: " + listItem.get(position).getBird_name());

        //allows the image to load properly into the fire UI
        Glide.with(homePage.getApplicationContext())
                .load(listItem.get(position).getBird_image())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo)
                .into(holder.imageViewBirds);


        //looping through reward points
        for(int i = 0; i < rewardPointsModels.size();i++) {

            //if status matches than updates the ui
            if(rewardPointsModels.get(i).getBird_status().matches(listItem.get(position).getBird_status())){
                holder.pointsTextView.setText("Congratulations You Have Earned: " + rewardPointsModels.get(i).getReward_points());
            }
        }

        holder.url = listItem.get(position).getBird_url();
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView identifiedBirdTexview, pointsTextView;
        ImageView imageViewBirds;
        Button moreInfo;
        String url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            identifiedBirdTexview = itemView.findViewById(R.id.record_retrival_data_identified_texview);
            imageViewBirds = itemView.findViewById(R.id.record_retrival_data_imageview);
            pointsTextView = itemView.findViewById(R.id.recordCardViewtextView1);
            moreInfo = itemView.findViewById(R.id.moreInfoButtonRecordCardview);

            moreInfo.setOnClickListener(moreInfoClick);



        }


        //opens browser of the bird picked
        private final View.OnClickListener moreInfoClick = v -> onBrowseClick(v);

        //opens browser of the bird picked
        public void onBrowseClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Note the Chooser below. If no applications match,
            // Android displays a system message.So here there is no need for try-catch.
            homePage.startActivity(Intent.createChooser(intent, "Browse with"));

        }
    }
}



