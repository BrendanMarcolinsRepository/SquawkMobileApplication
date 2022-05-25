package com.example.a321projectprototype.ui.Discover;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DiscoverChoice extends Fragment implements OnMapReadyCallback
{

    //Variables needed
    private View root;
    private TextView birdNameTextView,sciNameTextView,dateTextView,lngTextView,latTextView,visitTextView;
    private Button moreInfo;
    private HomePage homePage;
    private GoogleMap map;
    private String comBirdName,sciName,date,lng,lat,visit,code;
    private double latitude, longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        //Variables initialised
        root = inflater.inflate(R.layout.fragment_discover_choice, container, false);
        homePage = (HomePage) getActivity();
        DrawerLayout drawerLayout = homePage.getDrawer();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        comBirdName = getArguments().getString("birdName");
        sciName = getArguments().getString("sciName");
        date = getArguments().getString("date");
        lng = getArguments().getString("logatude");
        lat = getArguments().getString("latitude");
        visit = getArguments().getString("visit");
        code = getArguments().getString("code");
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lng);

        birdNameTextView = root.findViewById(R.id.BirdTextview);
        sciNameTextView = root.findViewById(R.id.birdScientificNameTextview);
        dateTextView = root.findViewById(R.id.birdDateTextview);
        visitTextView = root.findViewById(R.id.birdVisitTextview);
        moreInfo = root.findViewById(R.id.moreInfoDiscover);

        birdNameTextView.setText(comBirdName);
        sciNameTextView.setText("Scientific Name: " + sciName);
        dateTextView.setText("Obervation Date: " + date);
        visitTextView.setText("Recent Visit: " + visit);


        //Institating Google Map

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.mapDiscover, mapFragment).commit();
        mapFragment.getMapAsync(this);


        //User Inputs
        moreInfo.setOnClickListener(moreInfoClick);

        return root;
    }


    //Setups the google map fragment
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.mapDiscover);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    //Provides data for the googlemap and displays the details
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng birdPosition = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(birdPosition).title("Last Chirp");
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLng(birdPosition));
        map.addMarker(markerOptions);

    }

    //Two methods below openeds a link from an api to provide futher information of the bird selected

    private final View.OnClickListener moreInfoClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onBrowseClick(v);
        }
    };

    public void onBrowseClick(View v) {
        String url = "https://ebird.org/species/"+code;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        homePage.startActivity(intent);

    }


}













