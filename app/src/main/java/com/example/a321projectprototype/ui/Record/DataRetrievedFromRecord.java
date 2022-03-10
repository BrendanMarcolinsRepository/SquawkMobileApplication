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
import com.example.a321projectprototype.User.ItemDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataRetrievedFromRecord extends Fragment
{
    private RecyclerView recyclerView;
    private HomePage homePage;
    private List<Integer> numberList;
    private List<ItemDataModel> listItem;
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
        recyclerView.setAdapter(new RecordDataCardViewAdapter(listItem,homePage));

        return  root;

    }

    private void setData()
    {
        int drawable1 = R.drawable.magpie;
        int drawable2 = R.drawable.australian_swiftlet;
        int drawable3 = R.drawable.australian_crake;
        int drawable4 = R.drawable.australian_brushturkey;
        int drawable5 = R.drawable.rainbow_lorikeet;

        ItemDataModel item1 = new ItemDataModel("Australian Magpie","https://ebird.org/species/ausmag2",drawable1);
        ItemDataModel item2 = new ItemDataModel("Australian Swiftlet","https://ebird.org/species/ausswi1",drawable2);
        ItemDataModel item3 = new ItemDataModel("Australian Crake","https://ebird.org/species/auscra1",drawable3);
        ItemDataModel item4 = new ItemDataModel("Australian Brushturkey","https://ebird.org/species/ausbrt1",drawable4);
        ItemDataModel item5 = new ItemDataModel("Rainbow Lorikeet","https://ebird.org/species/railor5",drawable5);


        listItem = new ArrayList<>();
        numberList = new ArrayList<>();

        listItem.add(item1);
        listItem.add(item2);
        listItem.add(item3);
        listItem.add(item4);
        listItem.add(item5);





        numberList = randomNumberGenerator();

        for(int i = 0; i < numberList.size(); i++)
        {
            int number = numberList.get(i);
            listItem.remove(number);
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
