package com.example.a321projectprototype.ui.Past_Recordings;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;

import java.util.List;

public class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder>
{
    List<String> listItem;
    Context context;
    onClickInterface onClickInterface;

    MyRvAdapter(List<String> listItem,Context context,onClickInterface onClickInterface)
    {
        this.context = context;
        this.onClickInterface = onClickInterface;
        this.listItem = listItem;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvItem.setText(listItem.get(position));
        holder.tvItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickInterface.setClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.past_recording_dates_textview);
        }
    }
}
