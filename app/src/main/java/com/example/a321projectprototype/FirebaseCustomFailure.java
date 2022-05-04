package com.example.a321projectprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirebaseCustomFailure extends AppCompatActivity {

    public void onButtonShowPopupWindowClick(Context context) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.failure_pop,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.popUpFailureButton);


        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
