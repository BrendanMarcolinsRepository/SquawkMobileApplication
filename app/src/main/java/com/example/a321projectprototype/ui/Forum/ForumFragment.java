package com.example.a321projectprototype.ui.Forum;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ForumModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Coding Done By Neil Mediarito
public class ForumFragment extends Fragment {
    private ForumViewModel forumViewModel;
    private SearchView forumSearchView;
    private RecyclerView recyclerView;
    private List<ForumModel> forumList;
    private AdapterForum adapterForum;
    private View root;
    private HomePage homePage;
    private NavController navController;

    private ImageView filterButton, addButton;
    private String s = "No Change", filterOrder = "o";
    private boolean reversed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        forumViewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_forum, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        DrawerLayout drawerLayout = homePage.getDrawer();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        recyclerView = root.findViewById(R.id.recycleDiscover);
        filterButton = root.findViewById(R.id.forumFillterButton);
        addButton = root.findViewById(R.id.addFillterButton);

        forumSearchView = root.findViewById(R.id.forum_search_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        forumList = new ArrayList<>();
        setAdaptor1();

        addButton.setOnClickListener(addForum);
        filterButton.setOnClickListener(filter);






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

    //Navigator
    private final View.OnClickListener addForum = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_forum_to_add);

        }
    };

    //Logic for popup
    private final View.OnClickListener filter = v -> {
        onButtonShowPopupWindowClick(v);
        Snackbar.make(v, s, Snackbar.LENGTH_LONG);

    };

    //Logic for popup, similar to other filter method in discover section

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
            order();
            alertDialog.dismiss();
        });

        reverseAlphabetical.setOnClickListener(v -> {
            reverseOrder();
            alertDialog.dismiss();
        });

        exitPopup.setOnClickListener(v -> {
            s = "No Change";
            alertDialog.dismiss();
        });
        alertDialog.show();


    }

    //sets the adaptor with data from firebase
    private void setAdaptor1() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        //retrieves data of posts from firebase in recent order
        firebaseFirestore.collection("posts").orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for(DocumentChange documentChange : value.getDocumentChanges()) {
                            if(documentChange.getType() == DocumentChange.Type.ADDED) {
                                ForumModel forumModel = documentChange.getDocument().toObject(ForumModel.class);
                                forumList.add(forumModel);
                            }
                        }

                        setRecyclerView();
                    }
                });
    }


//Checks if reversed or not
    private void reverseOrder() {
        if(!reversed) {
            reversed = true;
            theChange();
        }
    }

    private void order() {
        if(reversed) {
            reversed = false;
            theChange();
        }
    }

    //updates the changes of order to the recycle view
    private void theChange() {
        System.out.println("worked");
        Collections.reverse(forumList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterForum = new  AdapterForum(forumList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterForum);
    }

    //sets the recycle view
    private void setRecyclerView(){
        adapterForum = new  AdapterForum(forumList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterForum);
    }
}

