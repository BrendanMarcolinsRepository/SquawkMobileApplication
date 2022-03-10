package com.example.a321projectprototype.ui.Past_Recordings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ItemDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PastRecordingsFragment extends Fragment {

    private PastRecordingsViewModel pastRecordingsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private onClickInterface onClickInterface;
    private TextView date;
    private SimpleDateFormat formatter;
    private String dateString;
    private HomePage homePage;
    private List<String> listItem;

    ItemDataModel item1 = new ItemDataModel("Australian Magpie");
    ItemDataModel item2 = new ItemDataModel("Australian Swiftlet");
    ItemDataModel item3 = new ItemDataModel("Australian Crake");
    ItemDataModel item4 = new ItemDataModel("Australian Brushturkey");
    ItemDataModel item5 = new ItemDataModel("Rainbow Lorikeet");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pastRecordingsViewModel =
                new ViewModelProvider(this).get(PastRecordingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pastrecordings, container, false);
        recyclerView = root.findViewById(R.id.recycleRecordings);
        recyclerView2 = root.findViewById(R.id.recycleRecordings2);
        date = root.findViewById(R.id.past_date_textview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        formatter = new SimpleDateFormat("E, MMM dd yyyy");

        homePage = (HomePage) getActivity();

        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        List<Date> dateList = getDates();

        listItem = new ArrayList<>();



        for(int i = 0; i < dateList.size();i++)
        {
            dateString = formatter.format(dateList.get(i));
            listItem.add(String.format(dateString));
        }





        onClickInterface = new onClickInterface() {
            @Override
            public void setClick(int click, View view)
            {
                date.setText(listItem.get(click));


                setAdaptor2();

            }
        };


        MyRvAdapter rvAdapter = new MyRvAdapter(listItem, getContext(),onClickInterface,homePage);

        recyclerView.setAdapter(rvAdapter);

        setAdaptor2();

        return root;
    }

    private void setAdaptor2()
    {
        List<String> listItem2 = new ArrayList<>();
        listItem2.add(String.format(item1.getTxtname()));
        listItem2.add(String.format(item2.getTxtname()));
        listItem2.add(String.format(item3.getTxtname()));
        listItem2.add(String.format(item4.getTxtname()));
        listItem2.add(String.format(item5.getTxtname()));

        recyclerView2.setAdapter(new PastRecordingsCardviewAdpator(listItem2,homePage));

    }

    public List<Date> getDates()
    {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        List<Date> dateList = new ArrayList<>();
        dateList.add(today);
        for(int i = 1; i < 7; i++)
        {
           
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date date = cal.getTime();
            dateList.add(date);

        }

        return dateList;
    }
}