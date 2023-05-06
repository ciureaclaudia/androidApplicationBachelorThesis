package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;


import static android.content.ContentValues.TAG;

import static com.example.licenta.R.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.grpc.okhttp.internal.Util;

public class Incercare2_FragmentOrar extends Fragment implements AdapterView.OnItemSelectedListener {

    String ziSapt;
    String denumire;
    String profesor;
    String sala;
    int oraInceput, minutIneput, oraSfarsit, minutSfarsit; //PT TIME PIKER

    TextView txtOraInceput;
    TextView oraAleasaInceput;
    TextView txtOraSfarsit;
    TextView oraAleasaSfarsit;


    RecyclerView recyclerView;

    HashMap<ParentModelClass, ArrayList<MaterieOrar>> hashMap; // fiecare zi a sapt are o lista de materii

    ParentModelClass parentModelClassMonday;
    ParentModelClass parentModelClassTuesday;
    ParentModelClass parentModelClassWednesday;
    ParentModelClass parentModelClassThursday;
    ParentModelClass parentModelClassFriday;

    ParentAdapter parentAdapter;
    FloatingActionButton introducereMaterieNouaFAB;

    // zilele sapt pt spinner din alertDialog
    String[] listaSaptamana = {"Luni", "Marti", "Miercuri", "Joi", "Vineri"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_incercare2__orar, container, false);

        recyclerView = view.findViewById(id.rv_parent_orar);

        parentModelClassMonday = new ParentModelClass("Luni", 0);
        parentModelClassTuesday = new ParentModelClass("Marti", 1);
        parentModelClassWednesday = new ParentModelClass("Miercuri", 2);
        parentModelClassThursday = new ParentModelClass("Joi", 3);
        parentModelClassFriday = new ParentModelClass("Vineri", 4);

        hashMap = new HashMap<>();
        hashMap.put(parentModelClassMonday, new ArrayList<>());
        hashMap.put(parentModelClassThursday, new ArrayList<>());
        hashMap.put(parentModelClassWednesday, new ArrayList<>());
        hashMap.put(parentModelClassTuesday, new ArrayList<>());
        hashMap.put(parentModelClassFriday, new ArrayList<>());

        parentAdapter = new ParentAdapter(hashMap, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();

        //Inainte sa apas pe butonul FAB pt a introduce o noua materie,
        // trebuie sa preiau datele din BD pt a afisa ceea ce era deja introdus

//        FirebaseFirestore db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        String userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
//        // Get the Firestore reference for the user's "Orar" collection
//        CollectionReference orarCollection  = db.collection("users").document(userID).collection("Orar"); //preiau referinta ed la colectia ORAR
//
//        // Query the Orar collection to get all documents
//        orarCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> orarList = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        // Get the weekDay field from the document
//                        String weekDay = document.getString("weekDay");
//                        orarList.add(weekDay + ":"); // Add the weekDay to the list
//
//                        // Get the Materii subcollection from the document
//                        CollectionReference materiiRef = document.getReference().collection("Materii");
//                        materiiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        // Get the fields from the Materii document
//                                        String denumire = document.getString("denumire");
//                                        String sala = document.getString("sala");
//                                        String profesor = document.getString("profesor");
//                                        String ziSapt = document.getString("ziSapt");
//                                        String inceput = document.getString("inceput");
//                                        String sfarsit = document.getString("sfarsit");
//
//                                        // Add the Materii fields to the list
////                                        orarList.add("- " + denumire + ", " + sala + ", " + profesor + ", " + ziSapt + ", " + inceput + " - " + sfarsit);
//                                    }
//
//                                    // Show the list on screen
//                                    TextView TV_denumire = view.findViewById(id.cv_child_item_tv_Materie);
//                                    TextView TV_inceput = view.findViewById(id.cv_child_item_tv_startTime);
//                                    TextView TV_sfarsit = view.findViewById(id.cv_child_item_tv_endTime);
//                                    TextView TV_sala = view.findViewById(id.cv_child_item_tv_sala);
//                                    TextView TV_prof = view.findViewById(id.cv_child_item_tv_profesor);
//
////                                    textView.setText(TextUtils.join("\n", orarList));
//                                } else {
//                                    Log.d(TAG, "Error getting Materii documents: ", task.getException());
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Log.d(TAG, "Error getting Orar documents: ", task.getException());
//                }
//            }
//        });

        //apasare pe buton pt a introduce o noua materie in orar
        introducereMaterieNouaFAB = view.findViewById(id.add_new_subject_FAB);
        introducereMaterieNouaFAB.setOnClickListener(view1 -> {
            //se deschide o noua fereastra de dialog
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(layout.alertdialog_add_materie_orar);

            EditText edtMaterie = dialog.findViewById(id.edtMaterie);
            EditText edtSala = dialog.findViewById(id.edtSala);
            EditText edtProfesor = dialog.findViewById(id.edtProfesor);
            txtOraInceput = dialog.findViewById(id.txtOraInceput);
            oraAleasaInceput = dialog.findViewById(id.oraAleasaInceput);
            txtOraSfarsit = dialog.findViewById(id.txtOraSfarsit);
            oraAleasaSfarsit = dialog.findViewById(id.oraAleasaSfarsit);

            txtOraInceput.setOnClickListener(view2 -> {
                popTimePickerInceput(view);
            });

            txtOraSfarsit.setOnClickListener(view2 -> {
                popTimePickerSfarsit(view);
            });


            Spinner spinner = dialog.findViewById(id.spinnerZiSapt);
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listaSaptamana);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(ad);

            Button save = dialog.findViewById(id.btn_saveMaterie);
            save.setOnClickListener(view2 -> {

                boolean verifCampuri = true;
                //preiau campurile introduse
                this.denumire = edtMaterie.getText().toString();
                this.profesor = edtProfesor.getText().toString();
                this.sala = edtSala.getText().toString();

                if (TextUtils.isEmpty(denumire)) {
                    Toast.makeText(getContext(), "Nume materie!", Toast.LENGTH_SHORT).show();
                    edtMaterie.setError("Camp gol");
                    edtMaterie.requestFocus();
                    verifCampuri = false;
                }
                if (TextUtils.isEmpty(profesor)) {
                    Toast.makeText(getContext(), "Profesor!", Toast.LENGTH_SHORT).show();
                    edtProfesor.setError("Camp gol");
                    edtProfesor.requestFocus();
                    verifCampuri = false;
                }
                if (TextUtils.isEmpty(sala)) {
                    Toast.makeText(getContext(), "Introdu numele", Toast.LENGTH_SHORT).show();
                    edtSala.setError("Camp gol");
                    edtSala.requestFocus();
                    verifCampuri = false;
                }

                if (oraAleasaInceput.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Introdu ora", Toast.LENGTH_SHORT).show();
                    oraAleasaInceput.setError("ora");
                    verifCampuri = false;
                }
                if (oraAleasaSfarsit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Introdu ora", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                }
                if (oraInceput > oraSfarsit) {
                    Toast.makeText(getContext(), "Ora invalida", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                } else if (minutIneput > minutSfarsit && oraInceput == oraSfarsit) {
                    Toast.makeText(getContext(), "Ora invalida", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                }

                String oraInceputString = String.valueOf(this.oraInceput) + ":" + String.valueOf(this.minutIneput);
                String oraFinalString = String.valueOf(this.oraSfarsit) + ":" + String.valueOf(this.minutSfarsit);

                if (verifCampuri) {
                    MaterieOrar materieOrar = new MaterieOrar(this.denumire, this.sala, this.profesor, this.ziSapt, oraInceputString, oraFinalString);

                    switch (this.ziSapt) {
                        case "Luni":
                            ArrayList<MaterieOrar> valoare = hashMap.get(parentModelClassMonday);
                            valoare.add(materieOrar);
                            hashMap.put(parentModelClassMonday, valoare);
                            break;

                        case "Marti":
                            ArrayList<MaterieOrar> valoare1 = hashMap.get(parentModelClassTuesday);
                            valoare1.add(materieOrar);
                            hashMap.put(parentModelClassTuesday, valoare1);
                            break;


                        case "Miercuri":
                            ArrayList<MaterieOrar> valoare2 = hashMap.get(parentModelClassWednesday);
                            valoare2.add(materieOrar);
                            hashMap.put(parentModelClassWednesday, valoare2);
                            break;


                        case "Joi":
                            ArrayList<MaterieOrar> valoare3 = hashMap.get(parentModelClassThursday);
                            valoare3.add(materieOrar);
                            hashMap.put(parentModelClassThursday, valoare3);
                            break;


                        case "Vineri":
                            ArrayList<MaterieOrar> valoar4 = hashMap.get(parentModelClassFriday);
                            valoar4.add(materieOrar);
                            hashMap.put(parentModelClassFriday, valoar4);
                            break;
                    }

                    saveDatainFireStore(); //SALVEZ IN BD

                    dialog.dismiss();
                    parentAdapter.notifyDataSetChanged();
                }
            });


            Toast.makeText(getContext(), "apasat", Toast.LENGTH_SHORT).show();
            dialog.show();
        });


        return view;
    }

    public void fetchDataFromFireStore(){

    }


    public void saveDatainFireStore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        // Get the Firestore reference for the user's "Orar" collection
        CollectionReference orarCollection = db.collection("users").document(userID).collection("Orar"); //preiau referinta ed la colectia ORAR

        //parcurg hashmapul
        for (Map.Entry<ParentModelClass, ArrayList<MaterieOrar>> entry : hashMap.entrySet()) {
            ParentModelClass key = entry.getKey(); //KEY - NUME SAPT
            ArrayList<MaterieOrar> value = entry.getValue(); //VALUE-LISTA MATERII

            // creez un document pt fiecare key-weekday
            DocumentReference weekdayDocRef = orarCollection.document(key.weekDay); //DOCUMENT PT FIECARE ZI A SAPT

            // Add the id and weekDay fields to the weekday document
            Map<String, Object> weekdayData = new HashMap<>();
            weekdayData.put("id", key.id);
            weekdayData.put("weekDay", key.weekDay);
            weekdayDocRef.set(weekdayData);

            // Create a collection named "Materii" within the weekday document
            CollectionReference materiiRef = weekdayDocRef.collection("Materii");

            // Add a document for each subject in the ArrayList
            for (MaterieOrar m : value) {//parcurg lista de obiecte din hashmap-values
                Map<String, Object> materieOrarData = new HashMap<>(); //preaiu un elem din lista si in salvez sub forma de hashmap
                materieOrarData.put("denumire", m.getDenumire());
                materieOrarData.put("sala", m.getSala());
                materieOrarData.put("profesor", m.getProfesor());
                materieOrarData.put("ziSapt", m.getZiSapt());
                materieOrarData.put("inceput", m.getInceput());
                materieOrarData.put("sfarsit", m.getSfarsit());
                materiiRef.document(m.getDenumire()).set(materieOrarData);
            }
        }
    }


    //AlertDialog- de ales ora
    public void popTimePickerInceput(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                oraInceput = selectedHour;
                minutIneput = selectedMinute;
                oraAleasaInceput.setText(String.format(Locale.getDefault(), "%02d:%02d", oraInceput, minutIneput, true));

            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, oraInceput, minutIneput, true);
        timePickerDialog.setTitle("Ora Incepere");
        timePickerDialog.show();
    }

    public void popTimePickerSfarsit(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                oraSfarsit = selectedHour;
                minutSfarsit = selectedMinute;
                oraAleasaSfarsit.setText(String.format(Locale.getDefault(), "%02d:%02d", oraSfarsit, minutSfarsit, true));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, oraInceput, minutSfarsit, true);
        timePickerDialog.setTitle("Ora Finalizare");
        timePickerDialog.show();
    }


    //AlertDialog -spinner de ales zi saptamana
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //cand se selecteaza ceva din spinner
        this.ziSapt = listaSaptamana[position];
        Toast.makeText(getContext(), listaSaptamana[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
