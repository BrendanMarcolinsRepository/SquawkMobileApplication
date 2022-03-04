package com.example.a321projectprototype.ui.Flock;

import android.app.AlertDialog;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FlockFragment extends Fragment
{
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
    private FlockDatabase flockDatabase;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        flockViewModel = new ViewModelProvider(this).get(FlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flock, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        flockDatabase = new FlockDatabase(homePage);

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
        adapterFlock = new  AdapterFlock(flockList,homePage,getContext(),flockDatabase, root);
        recyclerView.setAdapter(adapterFlock);


//        System.out.println("Flock name 1 " + flockModelData.getName());


        progressBar.setVisibility(View.VISIBLE);
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

        firebaseFirestore.collection("Flock").orderBy("name", Query.Direction.ASCENDING)
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

                                flockList.add(documentChange.getDocument().toObject(FlockModelData.class));
                                progressBar.setVisibility(View.INVISIBLE);


                            }

                            adapterFlock.notifyDataSetChanged();
                        }
                    }
                });
    }



    private void checkFlockName()
    {
        DocumentReference  documentReference = firebaseFirestore.collection("Flock").document("4OIcTerZfrxWSLMZYX1O");

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
                        UserModel userModel = homePage.getUserModel();
                        if(userModel.getUsername() != task.getResult().getString("ownerUsername"))
                        {
                            userModel.setUserFlock(task.getResult().getString("name"));
                            textViewFlockName.setText(userModel.getUserFlock());
                            createFlockButton.setVisibility(View.GONE);
                            createFlockButton.setOnClickListener(null);
                            myflock.setVisibility(View.VISIBLE);
                            flockImage.setVisibility(View.VISIBLE);


                        }
                        else
                        {
                            myflock.setVisibility(View.GONE);
                            flockImage.setVisibility(View.GONE);
                            createFlockButton.setVisibility(View.VISIBLE);

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
            navController.navigate(R.id.nav_Flock_Info);

        }
    };




}
