package com.example.a321projectprototype.ui.Flock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

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
    private Button filterButton, createFlockButton;
    private TextView filterOkayTextView;
    private View root;
    private boolean reversed = false;
    private String s = "No Change", filterOrder = "o";
    private HomePage homePage;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        flockViewModel = new ViewModelProvider(this).get(FlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flock, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        final TextView textView = root.findViewById(R.id.text_flocks);
        createFlockButton = root.findViewById(R.id.flockCreateButton);


        flockViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        flockSearchView = root.findViewById(R.id.flock_search_bar);
        recyclerView = root.findViewById(R.id.recycleFlock);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);





        FlockModelData flock1 = new FlockModelData("Sydney Flockers",200);
        FlockModelData flock2 = new FlockModelData("Hunter Valley Flockers",88);
        FlockModelData flock3 = new FlockModelData("Gong Flockers",100);
        FlockModelData flock4 = new FlockModelData("Ctown Flockers",69);

        flockList = new ArrayList();
        flockList.add(flock1);
        flockList.add(flock2);
        flockList.add(flock3);
        flockList.add(flock4);

        adapterFlock = new  AdapterFlock(flockList,homePage);
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

    private final View.OnClickListener createFlockFragement = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);

        }
    };


}
