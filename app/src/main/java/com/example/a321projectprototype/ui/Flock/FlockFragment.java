package com.example.a321projectprototype.ui.Flock;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FlockFragment extends Fragment
{
    private FlockModelData flockModelData;
    private FlockViewModel flockViewModel;
    private SearchView flockSearchView;
    private RecyclerView recyclerView;
    private List<FlockModelData> flockList;
    private AdapterFlock adapterFlock;
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    private Button filterButton, createFlockButton,myflock,leaderboard;
    private TextView filterOkayTextView, textViewFlockName;
    private ImageView flockImage;
    private View root;
    private boolean reversed = false;
    private String s = "No Change", filterOrder = "o";
    private HomePage homePage;
    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ProgressBar progressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        flockViewModel = new ViewModelProvider(this).get(FlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flock, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        textViewFlockName = root.findViewById(R.id.text_flocks);
        flockSearchView = root.findViewById(R.id.flock_search_bar);
        recyclerView = root.findViewById(R.id.recycleFlock);
        createFlockButton = root.findViewById(R.id.flockCreateButton);
        myflock = root.findViewById(R.id.flockMyFlock);
        flockImage = root.findViewById(R.id.flockImage);
        leaderboard = root.findViewById(R.id.flockLeaderBoards);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = root.findViewById(R.id.flockListProgressBar);


        checkFlockName();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        flockList = new ArrayList<>();
        adapterFlock = new  AdapterFlock(flockList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterFlock);


//        System.out.println("Flock name 1 " + flockModelData.getName());



        EventChangeListener();
        createFlockButton.setOnClickListener(createFlockFragement);
        leaderboard.setOnClickListener(flockLeaderboardMethod);
        myflock.setOnClickListener(myFlockMethod);


        flockSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(flockList.contains(query))
                {
                    adapterFlock.getFilter().filter(query);
                }
                else {
                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapterFlock.getFilter().filter(newText);
                return false;
            }
        });


        return root;
    }


    private void EventChangeListener()
    {

        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("flocks").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {

                        if(error != null)
                        {
                            System.out.println("ERROR ================> " + error.getMessage());
                        }

                        for(DocumentChange documentChange : value.getDocumentChanges())
                        {

                            if(documentChange.getType() == DocumentChange.Type.ADDED)
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                flockList.add(documentChange.getDocument().toObject(FlockModelData.class));
                                System.out.println("Postion clicked is: ======================>?" + flockList.get(0).getFlockId());
                            }

                            adapterFlock.notifyDataSetChanged();
                        }
                    }
                });
    }



    private void checkFlockName()
    {



        String userID = auth.getCurrentUser().getUid();


        firebaseFirestore.collection("flockMembers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if(userID.equals(document.get("userId").toString()))
                        {
                            createFlockButton.setVisibility(View.GONE);
                            createFlockButton.setOnClickListener(null);
                            myflock.setVisibility(View.VISIBLE);
                            flockImage.setVisibility(View.VISIBLE);
                            System.out.println("===========> in a flock");

                            getFlockName(document.getString("flockId").toString());
                            return;
                        }

                    }
                }
                else
                {
                    System.out.println("no document");
                }
            }
        });

        if(flockModelData == null)
        {
            myflock.setVisibility(View.GONE);
            flockImage.setVisibility(View.GONE);
            createFlockButton.setVisibility(View.VISIBLE);
            System.out.println("===========> not in a flock");
            System.out.println("no document");
        }
    }

    private void getFlockName(String flockId)
    {
        firebaseFirestore.collection("flocks").document(flockId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String,Object> myMap1 = new HashMap<>();
                    myMap1 = documentSnapshot.getData();

                    flockModelData = new FlockModelData();

                    //flockModelData.setUserId(myMap1.get("userId").toString());
                    flockModelData.setFlockId(myMap1.get("flockId").toString());
                    flockModelData.setName(myMap1.get("name").toString());
                    flockModelData.setMemberCount(Integer.parseInt(myMap1.get(("memberCount")).toString()));
                    flockModelData.setDescription(myMap1.get("description").toString());
                    flockModelData.setCreated_at(myMap1.get("created_at").toString());
                    flockModelData.setUserId(myMap1.get("updated_at").toString());

                    homePage.setFlockModelData(flockModelData);

                    textViewFlockName.setText(flockModelData.getName());

                }
            }
        });



    }
    private final View.OnClickListener createFlockFragement = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);
        }
    };

    private final View.OnClickListener flockLeaderboardMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Leaderboards);

        }
    };

    private final View.OnClickListener myFlockMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("flock", flockModelData);
            navController.navigate(R.id.nav_Flock_Info,bundle);

        }
    };




}
