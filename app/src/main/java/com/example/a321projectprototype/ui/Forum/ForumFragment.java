package com.example.a321projectprototype.ui.Forum;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.Database.ForumDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.ForumModel;
import com.example.a321projectprototype.ui.Discover.AdapterDiscover;
import com.example.a321projectprototype.ui.Flock.AdapterFlock;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;


public class ForumFragment extends Fragment
{
    private ForumViewModel forumViewModel;
    private SearchView forumSearchView;
    private RecyclerView recyclerView;
    private List<ForumModel> forumList;
    private AdapterForum adapterForum;
    private View root;
    private HomePage homePage;
    private NavController navController;
    private ForumDatabase forumDatabase;
    private Button filterButton, addButton;
    private String s = "No Change", filterOrder = "o";
    private boolean reversed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        forumViewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_forum, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        recyclerView = root.findViewById(R.id.recycleDiscover);
        filterButton = root.findViewById(R.id.forumFillterButton);
        addButton = root.findViewById(R.id.addFillterButton);
        forumDatabase = new ForumDatabase(this.homePage);
        forumList = forumDatabase.getAllUsers();
        forumSearchView = root.findViewById(R.id.forum_search_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        addButton.setOnClickListener(addForum);
        filterButton.setOnClickListener(filter);

//        System.out.println("Flock name 1 " + flockModelData.getName());

        adapterForum = new  AdapterForum(forumList,homePage,getContext(),forumDatabase, root);
        recyclerView.setAdapter(adapterForum);

        forumSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(forumList.contains(query))
                {
                    adapterForum.getFilter().filter(query);
                }
                else {
                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapterForum.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    private final View.OnClickListener addForum = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_forum_to_add);

        }
    };

    private final View.OnClickListener filter = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onButtonShowPopupWindowClick(v);
            Snackbar.make(v, s, Snackbar.LENGTH_LONG);

        }
    };


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
        popular.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                s = "Populer Order";
                filterOrder = "p";
                alertDialog.dismiss();
            }
        });


        alphabetical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                order();
                alertDialog.dismiss();
            }
        });
        reverseAlphabetical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reverseOrder();
                alertDialog.dismiss();
            }
        });
        exitPopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                s = "No Change";
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }



    private void reverseOrder()
    {
        if(!reversed)
        {
            reversed = true;
            theChange();
        }
    }

    private void order()
    {
        if(reversed)
        {
            reversed = false;
            theChange();
        }
    }

    private void theChange()
    {
        System.out.println("worked");
        Collections.reverse(forumList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterForum = new  AdapterForum(forumList,homePage,getContext(),forumDatabase, root);
        recyclerView.setAdapter(adapterForum);
    }
}

