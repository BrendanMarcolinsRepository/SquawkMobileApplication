package com.example.a321projectprototype.ui.Past_Recordings;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.Files;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PastRecordingsOnlineFragment extends Fragment {

    private PastRecordingsOnlineFragment pastRecordingsOnlineFragment;
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
    private RelativeLayout relativeLayout;
    private int mYear,mMonth,mDay;
    private String stringDate;
    private final boolean CLOUD = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_pastrecordings_online, container, false);
        View cardView = inflater.inflate(R.layout.record_data_retrival_cardview, container, false);
        homePage = (HomePage) getActivity();

        recyclerView2 = root.findViewById(R.id.recycleRecordings2);
        date = root.findViewById(R.id.past_date_textview);
        relativeLayout = root.findViewById(R.id.imageButtonCalendar);

        relativeLayout.setOnClickListener(calendarLauncher);




        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setHasFixedSize(true);



        listItem = new ArrayList<>();
        filesList = new ArrayList<>();


        pastRecordingsCardviewAdpator = new PastRecordingsCardviewAdpator(homePage, filesList,CLOUD);
        updateRecyclerView(new Date());
        recyclerView2.setAdapter(pastRecordingsCardviewAdpator);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(homePage, "File Deleted  ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                filesList.remove(viewHolder.getAbsoluteAdapterPosition());

                if(pastRecordingsCardviewAdpator != null) {
                    pastRecordingsCardviewAdpator.notifyDataSetChanged();
                }

            }
        }).attachToRecyclerView(recyclerView2);


        return root;
    }

    private void updateRecyclerView(Date tempDate)
    {


        if(filesList.size() > 0)
            filesList.clear();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        String userID = auth.getCurrentUser().getUid();
        System.out.println("here ================> ");


        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        String stringDate= DateFor.format(tempDate);
        date.setText(stringDate);

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


    }


    private final View.OnClickListener calendarLauncher = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            final Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(homePage,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR,year);
                            c.set(Calendar.MONTH,month);
                            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                            Date date = c.getTime();
                            updateRecyclerView(date);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
    };

}