package com.example.a321projectprototype.ui.Record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Discover.ItemDataModel;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataRetrievedFromRecord extends Fragment
{
    private RecyclerView recyclerView;
    private HomePage homePage;
    private List<String> listItem;

    ItemDataModel item1 = new ItemDataModel("Australian Magpie");
    ItemDataModel item2 = new ItemDataModel("Rainbow Lorikeet");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragement_record_retrieved_data, container, false);

        recyclerView = root.findViewById(R.id.recycleRecordData);
        homePage = (HomePage) getActivity();
        setAdaptor();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        return  root;

    }

    private void setAdaptor()
    {
        listItem = new ArrayList<>();
        listItem.add(String.format(item1.getTxtname()));
        listItem.add(String.format(item2.getTxtname()));


        recyclerView.setAdapter(new RecordDataCardViewAdapter(listItem,homePage));

    }
}
