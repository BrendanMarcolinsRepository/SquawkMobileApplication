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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder>
{
    List<FlockModelData> FullList;
    List<FlockModelData> dataSet;

    private HomePage homePage;
    private NavController navigation;
    private Button joinButton, infoButton;
    private int picked;
    private Context context;
    private FlockModelData flockModelData;
    private FlockModelData currentItem;
    private FlockDatabase flockDatabase;
    private UserModel userModel;
    private String name, countNumber;
    private int count;
    private UserDatabase userDatabase;
    private ConstraintLayout registerLayout;
    private View view;
    private FirebaseFirestore firebaseFirestore;


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView groupName, groupCountNumber;



        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            groupName = itemView.findViewById(R.id.flock_namesearch_textview);
            groupCountNumber = itemView.findViewById(R.id.flock_amount_textview);
            joinButton = itemView.findViewById(R.id.flock_join_button);
            infoButton = itemView.findViewById(R.id.flock_info_button);
            registerLayout = view.findViewById(R.id.registerlayerFlock);


            countNumber = groupCountNumber.getText().toString();

            System.out.println(name);
            System.out.println(countNumber);

            FlockModelData flockModelData = flockDatabase.getFlock(name);



            //System.out.println("Flock name 2 " + name);

            infoButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    picked = getLayoutPosition();
                    System.out.println("worked");
                    Bundle bundle = new Bundle();
                    bundle.putInt("Position",picked );
                    navigation.navigate(R.id.info_fragment_nav,bundle);
                }
            });

            joinButton.setOnClickListener(joinMethod);


        }

        View.OnClickListener joinMethod  = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = homePage.getUserModel().getUserFlock();
                if(count <= 200 && name == null)
                {
                    popUp();
                }
                else
                {
                    Toast.makeText(context, "You Have Already Joined A Group",Toast.LENGTH_LONG).show();
                }


            }
        };
    }

    AdapterFlock(List<FlockModelData> listItem, HomePage homePage, Context context, FlockDatabase flockDatabase, View view)
    {
        this.dataSet = listItem;
        this.homePage = homePage;
        FullList = new ArrayList<>(listItem);
        this.context = context;
        this.flockDatabase = flockDatabase;
        this.view = view;
    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_flock_items, parent, false);
        return new AdapterFlock.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder holder, int position)
    {
        currentItem = dataSet.get(position);
        holder.groupName.setText(currentItem.getName());
        holder.groupCountNumber.setText(currentItem.getGroupNumber() + "/200");


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
            ArrayList<FlockModelData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FlockModelData item : FullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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

    public void popUp()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.flock_popup_accept,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.flockFilterButtonYes);
        Button no = (Button) mView.findViewById(R.id.flockFilterButtonNo);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        yes.setOnClickListener(new View.OnClickListener()
        {
            private int amount;
            @Override
            public void onClick(View v)
            {
                firebaseFirestore = FirebaseFirestore.getInstance();

                DocumentReference documentReference = firebaseFirestore.collection("Flock").document("A1m2nmOrG3WISGp2jUOp");

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists())
                            {
                                if(task.getResult().getString("name").equals(currentItem.getName()))
                                {
                                    amount = Integer.parseInt(task.getResult().getString("groupNumber"));
                                }
                            }
                            else
                            {
                                System.out.println("no document");
                            }
                        }
                        else
                        {
                            System.out.println("not successfull");

                        }
                    }
                });


                firebaseFirestore = FirebaseFirestore.getInstance();




                HashMap<String,Object> myMap = new HashMap<>();
                myMap.put("groupNumber",amount);


                documentReference.set(myMap).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    { }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });

                DocumentReference documentReference1 = firebaseFirestore.collection("flockMember").document();


                HashMap<String,Object> myMap1 = new HashMap<>();
                myMap1.put("name",homePage.getUserModel().getName());
                myMap1.put("ownerUsername", name);

                documentReference1.set(myMap1).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        alertDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
            }
        });


        no.setOnClickListener(new View.OnClickListener()
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
