package com.example.licenta.NavigationDrawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class GraficFragment extends Fragment {

    boolean existaMaterii;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        String collectionName = "NoteMaterii";

        DocumentReference documentReference = db.collection("users").document(userID);
        CollectionReference colectie = documentReference.collection(collectionName);
        colectie.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Collection exists and is not empty
                        existaMaterii = true;
                    } else {
                        existaMaterii = false;
                        // Collection does not exist or is empty
                    }
                } else {
                    // An error occurred while fetching the collection
                    Log.d("ERROR", documentReference.getPath().toString());
                }
            }
        });

        if (existaMaterii) {
            view = inflater.inflate(R.layout.fragment_grafic, container, false);

        } else {
            view = inflater.inflate(R.layout.fragment_grafic_fara_grafice, container, false);

        }


        return view;
    }
}