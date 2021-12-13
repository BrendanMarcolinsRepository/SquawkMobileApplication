package com.example.a321projectprototype.ui.Flock;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private Button filterButton, createFlockButton,myflock,settings;
    private TextView filterOkayTextView, textViewFlockName;
    private ImageView flockImage;
    private View root;
    private boolean reversed = false;
    private String s = "No Change", filterOrder = "o";
    private HomePage homePage;
    private NavController navController;
    private FlockDatabase flockDatabase;

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
        settings = root.findViewById(R.id.flockSettings);
        flockImage = root.findViewById(R.id.flockImage);


        checkFlockName();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        flockList = flockDatabase.getAllUsers();
        FlockModelData flockModelData = flockDatabase.getFlock(homePage.getUserModel().getUserFlock());
        System.out.println("Flock name 1 " + flockModelData.getName());

        adapterFlock = new  AdapterFlock(flockList,homePage,getContext(),flockDatabase, root);
        recyclerView.setAdapter(adapterFlock);

        createFlockButton.setOnClickListener(createFlockFragement);


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

    private void checkFlockName()
    {
        UserModel userModel = homePage.getUserModel();
        System.out.println(userModel.getUserFlock());

        if(userModel.getUserFlock() != null)
        {
            textViewFlockName.setText(userModel.getUserFlock());
            createFlockButton.setVisibility(View.GONE);
            createFlockButton.setOnClickListener(null);
            myflock.setVisibility(View.VISIBLE);
            settings.setVisibility(View.VISIBLE);
            flockImage.setVisibility(View.VISIBLE);

        }
        else
        {
            myflock.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            flockImage.setVisibility(View.GONE);
            createFlockButton.setVisibility(View.VISIBLE);
        }
    }

    private final View.OnClickListener createFlockFragement = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);

        }
    };




}
