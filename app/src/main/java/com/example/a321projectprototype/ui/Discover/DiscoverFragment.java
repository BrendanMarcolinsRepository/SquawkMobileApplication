package com.example.a321projectprototype.ui.Discover;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.API.BirdInterface;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdModel;
import com.example.a321projectprototype.User.ItemDataModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DiscoverFragment extends Fragment
{

    //Variables Needed
    private DiscoverViewModel discoverViewModel;
    private SearchView discoverSearchView;
    private RecyclerView recyclerView;
    private List<BirdModel> birdList;
    private AdapterDiscover adapterDiscover;
    private ImageView filterButton;
    private View root;
    private boolean reversed = false;
    private String s = "No Change", filterOrder = "o";
    private HomePage homePage;
    private NavController navigation;
    private Retrofit retrofit;
    private ProgressBar progressBar;

    

    @SuppressLint("RtlHardcoded")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Variables Initailised
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        root = inflater.inflate(R.layout.fragement_discover, container, false);

        homePage = (HomePage) getActivity();
        navigation = homePage.getNav();

        discoverSearchView = root.findViewById(R.id.discover_search_bar);
        recyclerView = root.findViewById(R.id.recycleDiscover);

        filterButton = root.findViewById(R.id.discoverFillterButton);
        filterButton.setOnClickListener(filterButtonMethod);

        progressBar = root.findViewById(R.id.discoverProgressBar);

        callApi();



        //Search View Input from user
        discoverSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(birdList.contains(query)) {
                    adapterDiscover.getFilter().filter(query);
                }
                else {
                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterDiscover.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    //Runs the api request
    private void callApi() {
        progressBar.setVisibility(View.VISIBLE);
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://api.ebird.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        BirdInterface service = retrofit.create(BirdInterface.class);

        Call<List<BirdModel>> repos = service.listRepos();

        //Retrieves the api data
        repos.enqueue(new Callback<List<BirdModel>>() {
            @Override
            public void onResponse(Call<List<BirdModel>> call, Response<List<BirdModel>> response) {
                if(response.code() != 200) {
                    return;
                }

                birdList = response.body();
                progressBar.setVisibility(View.INVISIBLE);
                recyclerViewMethod();


            }

            @Override
            public void onFailure(Call<List<BirdModel>> call, Throwable t) {

            }
        });


    }

    //Filter Button
    private final View.OnClickListener filterButtonMethod = new View.OnClickListener()
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v)
        {
            onButtonShowPopupWindowClick(v);
            Snackbar.make(v, s, Snackbar.LENGTH_LONG);
        }
    };

    //Used to provide certain filters
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onButtonShowPopupWindowClick(View view)
    {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.discover_popup_filter,null);
        alert.setView(mView);

        Button popular  = (Button) mView.findViewById(R.id.discoverFilter1Button);
        Button alphabetical = (Button) mView.findViewById(R.id.discoverFilter2Button);
        Button reverseAlphabetical = (Button) mView.findViewById(R.id.discoverFilter3Button);
        Button exitPopup = (Button) mView.findViewById(R.id.exitFilterDiscover);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        popular.setOnClickListener(v -> {
            s = "Populer Order";
            filterOrder = "p";
            alertDialog.dismiss();
        });


        alphabetical.setOnClickListener(v -> {
            orderAlgorithm();
            alertDialog.dismiss();
        });

        reverseAlphabetical.setOnClickListener(v -> {
            reverseOrderAlgorithm();
            alertDialog.dismiss();
        });

        exitPopup.setOnClickListener(v -> {
            s = "No Change";
            alertDialog.dismiss();
        });

        alertDialog.show();


    }

    //Alphabetical Algorithm
    private void orderAlgorithm()
    {
        System.out.println("worked");
        Collections.sort(birdList, Comparator.comparing(BirdModel::getComName));

        recyclerViewMethod();
    }

    //Reverse Alphabetical Algorithm

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reverseOrderAlgorithm() {
        System.out.println("worked");
        Collections.sort(birdList, Comparator.comparing(BirdModel::getComName));

        Collections.reverse(birdList);
        recyclerViewMethod();
    }

    //Recycle view  intialisation
    public void recyclerViewMethod() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterDiscover = new  AdapterDiscover(birdList,homePage);
        recyclerView.setAdapter(adapterDiscover);
    }



}
