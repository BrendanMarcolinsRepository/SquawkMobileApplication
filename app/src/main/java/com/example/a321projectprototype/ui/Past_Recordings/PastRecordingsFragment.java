package com.example.a321projectprototype.ui.Past_Recordings;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.RecordingPathFileDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.Files;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.ItemDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
    private List<Files> filesList;
    private List<String> listItem2;
    private List<Date> dateList;
    private Files files;
    private PastRecordingsCardviewAdpator pastRecordingsCardviewAdpator;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pastRecordingsViewModel =
                new ViewModelProvider(this).get(PastRecordingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pastrecordings, container, false);
        View cardView = inflater.inflate(R.layout.record_data_retrival_cardview, container, false);

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

        dateList = getDates();

        listItem = new ArrayList<>();
        filesList = new ArrayList<>();


        for(int i = 0; i < dateList.size();i++)
        {
            dateString = formatter.format(dateList.get(i));
            listItem.add(String.format(dateString));
        }

        setAdaptor2(dateList.get(0));




        onClickInterface = new onClickInterface() {
            @Override
            public void setClick(int click, View view)
            {

                setAdaptor2(dateList.get(click));
                date.setText(listItem.get(click));


            }
        };



        MyRvAdapter rvAdapter = new MyRvAdapter(listItem, getContext(),onClickInterface,homePage);
        recyclerView.setAdapter(rvAdapter);

        pastRecordingsCardviewAdpator = new PastRecordingsCardviewAdpator(homePage, filesList);
        recyclerView2.setAdapter(pastRecordingsCardviewAdpator);


        return root;
    }

    private void setAdaptor2(Date tempDate)
    {


        if(filesList.size() > 0)
            filesList.clear();

        RecordingPathFileDatabase recordingPathFileDatabase = new RecordingPathFileDatabase(homePage);
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
        String stringDate= ft.format(tempDate);

        System.out.println(stringDate);

        List<Files> tempFiles = recordingPathFileDatabase.getAllFiles(ft.format(tempDate));

        filesList.addAll(tempFiles);

        if(pastRecordingsCardviewAdpator != null)
            pastRecordingsCardviewAdpator.notifyDataSetChanged();


//        System.out.println("+++++++++++++++++++++++ date: " + filesList.get(0).getCreated_at());
        /*
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        String userID = auth.getCurrentUser().getUid();
        System.out.println("here ================> ");



        firebaseFirestore.collection("files").orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override


                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            System.out.println("Error ==========?>" +  error);
                            return;
                        }

                        filesList.clear();

                        for(DocumentChange documentChange : value.getDocumentChanges())
                        {
                            if(documentChange.getType() == DocumentChange.Type.ADDED)
                            {
                                if(documentChange.getDocument().get("uploadedBy").equals(userID))
                                {
                                    Files f = documentChange.getDocument().toObject(Files.class);

                                    SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
                                    String stringDate= DateFor.format(tempDate);


                                    System.out.println("Date Today==========?>" + stringDate);



                                    if(stringDate.regionMatches(0,f.getCreated_at(),0,10))
                                    {
                                        System.out.println("Date Match==========?>" +  f.getCreated_at());



                                        filesList.add(f);

                                    }
                                    else
                                    {
                                        System.out.println("Date ==========?>" +  f.getCreated_at());
                                    }

                                }
                            }
                        }

                        pastRecordingsCardviewAdpator.notifyDataSetChanged();




                    }
                });

        */
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