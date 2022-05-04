package com.example.a321projectprototype;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUpdateDocumentField {

    public void updateFlockNameMethod(String field, String path, String data, HomePage homePage,
                                       FirebaseFirestore firebaseFirestore, Context context ) {


        firebaseFirestore.collection(path)
                .document(homePage.getFlockModelData().getFlockId())
                .update(field, data)
                .addOnCompleteListener(task -> {
                    homePage.getUserInformation();
                }).addOnFailureListener(e -> {
            FirebaseCustomFailure failure = new FirebaseCustomFailure();
            failure.onButtonShowPopupWindowClick(context);
        });
    }

}
