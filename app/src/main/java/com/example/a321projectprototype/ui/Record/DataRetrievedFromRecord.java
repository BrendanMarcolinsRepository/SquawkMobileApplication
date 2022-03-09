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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DataRetrievedFromRecord extends Fragment
{
    private RecyclerView recyclerView;
    private HomePage homePage;
    private List<Integer> numberList;
    private List<String> listItem;
    private ArrayList image;
    private final int MAX_IDENTIFIER = 2;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragement_record_retrieved_data, container, false);

        recyclerView = root.findViewById(R.id.recycleRecordData);
        homePage = (HomePage) getActivity();
        setData();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecordDataCardViewAdapter(listItem,homePage,image));

        return  root;

    }

    private void setData()
    {

        ItemDataModel item1 = new ItemDataModel("Australian Magpie");
        ItemDataModel item2 = new ItemDataModel("Australian Swiftlet");
        ItemDataModel item3 = new ItemDataModel("Australian Crake");
        ItemDataModel item4 = new ItemDataModel("Australian Brushturkey");
        ItemDataModel item5 = new ItemDataModel("Rainbow Lorikeet");


        listItem = new ArrayList<>();
        numberList = new ArrayList<>();

        listItem.add(String.format(item1.getTxtname()));
        listItem.add(String.format(item2.getTxtname()));
        listItem.add(String.format(item3.getTxtname()));
        listItem.add(String.format(item4.getTxtname()));
        listItem.add(String.format(item5.getTxtname()));

        image = new ArrayList<>(Arrays.asList(R.drawable.magpie,R.drawable.australian_swiftlet
        ,R.drawable.australian_crake,R.drawable.australian_brushturkey,R.drawable.rainbow_lorikeet));



        numberList = randomNumberGenerator();

        for(int i = 0; i < numberList.size(); i++)
        {
            int number = numberList.get(i);
            listItem.remove(number);
            image.remove(number);

        }
    }

    public List<Integer> randomNumberGenerator()
    {
        Random randomObject = new Random();
        List<Integer> numberBirds = new ArrayList<>();

        int counter = 0;
        while(counter < MAX_IDENTIFIER)
        {
            int randomInteger = randomObject.nextInt(4);

            if(counter == 0)
            {
                numberBirds.add(randomInteger);
            }

            for(int i = 0; i < numberBirds.size(); i++)
            {
                if(numberBirds.get(i) != randomInteger)
                {
                    numberBirds.add(randomInteger);
                }
            }

            counter++;
        }

        return numberBirds;
    }
}
