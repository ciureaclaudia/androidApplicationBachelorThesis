package com.example.licenta.BottomNavigationView.ProfileFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;

public class ProfileFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        EditText nume = view.findViewById(R.id.numeProfil);
        EditText prenume = view.findViewById(R.id.prenumeProfil);
        EditText email = view.findViewById(R.id.emailProfil);
        EditText facultate = view.findViewById(R.id.facultateProfil);
        EditText dataN = view.findViewById(R.id.dataNProfil);

        //functie care imi preia notele din BD
        //initilizez instanta FIRESTORE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        new Thread(()->{
//            PozaProfil pozaProfil=DBManager.getInstance(getContext()).pozaProfilDao().getById(userID);

        }).start();

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Get the user's information
                            String numee = documentSnapshot.getString("nume");
                            String prenumee = documentSnapshot.getString("prenume");
                            String emaill = documentSnapshot.getString("email");
                            String facultatee = documentSnapshot.getString("facultate");
                            String dataNN = documentSnapshot.getString("dNastere");

                            // Set the editText fields in your fragment with the user's information
                            nume.setText(numee);
                            prenume.setText(prenumee);
                            email.setText(emaill);
                            facultate.setText(facultatee);
                            dataN.setText(dataNN);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NOTOK", e.getMessage().toString());
                    }
                });

        return view;
    }




}