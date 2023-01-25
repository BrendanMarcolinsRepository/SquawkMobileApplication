package com.example.a321projectprototype.ui.Past_Recordings;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private RelativeLayout relativeLayout;
    private int mYear, mMonth, mDay;
    private String stringDate;
    private Button offline, online;
    private NavController navigation;
    private final boolean CLOUD = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pastRecordingsViewModel =
                new ViewModelProvider(this).get(PastRecordingsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_pastrecordings, container, false);
        View cardView = inflater.inflate(R.layout.record_data_retrival_cardview, container, false);

        homePage = (HomePage) getActivity();
        navigation = homePage.getNav();

        offline = root.findViewById(R.id.offlineButton);
        online = root.findViewById(R.id.onlineButton);
        recyclerView2 = root.findViewById(R.id.recycleRecordings2);
        date = root.findViewById(R.id.past_date_textview);
        relativeLayout = root.findViewById(R.id.imageButtonCalendar);


        offline.setOnClickListener(offlineButtomMethod);
        online.setOnClickListener(onlineButtomMethod);
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

                if (pastRecordingsCardviewAdpator != null) {
                    pastRecordingsCardviewAdpator.notifyDataSetChanged();
                }

            }
        }).attachToRecyclerView(recyclerView2);


        return root;
    }

    private void updateRecyclerView(Date tempDate) {


        if (filesList.size() > 0)
            filesList.clear();


        RecordingPathFileDatabase recordingPathFileDatabase = new RecordingPathFileDatabase(homePage);
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String stringDate = ft.format(tempDate);

        date.setText(stringDate);
        System.out.println("Date Here: " + stringDate);

        List<Files> tempFiles = recordingPathFileDatabase.getAllFiles(ft.format(tempDate));

        filesList.addAll(tempFiles);

        if (pastRecordingsCardviewAdpator != null)
            pastRecordingsCardviewAdpator.notifyDataSetChanged();


    }

    private final View.OnClickListener calendarLauncher = new View.OnClickListener() {
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
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date = c.getTime();
                            updateRecyclerView(date);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
    };

    private final View.OnClickListener onlineButtomMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            navigation.navigate(R.id.action_nav_online);

        }
    };

    private final View.OnClickListener offlineButtomMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(homePage,"Already in the Offline Section ", Toast.LENGTH_LONG).show();

        }
    };
}