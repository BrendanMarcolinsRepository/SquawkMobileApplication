package com.example.a321projectprototype.ui.Discover;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;


public class DiscoverFragment extends Fragment
{
    private DiscoverViewModel discoverViewModel;
    private SearchView discoverSearchView;
    private RecyclerView recyclerView;
    private ArrayList<ItemDataModel> list;
    private AdapterDiscover adapterDiscover;

    @SuppressLint("RtlHardcoded")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.fragement_discover, container, false);

        discoverSearchView = root.findViewById(R.id.discover_search_bar);
        recyclerView = root.findViewById(R.id.recycleDiscover);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        ItemDataModel item1 = new ItemDataModel("Australian Magpie");
        ItemDataModel item2 = new ItemDataModel("Australian Swiftlet");
        ItemDataModel item3 = new ItemDataModel("Australian Crake");
        ItemDataModel item4 = new ItemDataModel("Australian Brushturkey");
        ItemDataModel item5 = new ItemDataModel("Rainbow Lorikeet");




        list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);

        adapterDiscover = new  AdapterDiscover(list);
        recyclerView.setAdapter(adapterDiscover);



        discoverSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(list.contains(query)){
                    adapterDiscover.getFilter().filter(query);
                }else{
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





}
