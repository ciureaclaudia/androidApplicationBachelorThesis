package com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.licenta.BottomNavigationView.FragmentNote.NoteFragment;
import com.example.licenta.MainActivity;
import com.example.licenta.NavigationDrawer.toDoList.AProgres;
import com.example.licenta.NavigationDrawer.toDoList.Model.ToDoModel;
import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AOrar extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String ziSapt;
    String denumire;
    String profesor;
    String sala;

    //PT TIME PIKER
    int oraInceput;
    int minutIneput;
    int oraSfarsit;
    int minutSfarsit;
    Button save;

    TextView txtOraInceput;
    TextView oraAleasaInceput;
    TextView txtOraSfarsit;
    TextView oraAleasaSfarsit;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private  CollectionReference orarCollection;
    private Query query;
    private ListenerRegistration listenerRegistration;

    RecyclerView recyclerView;
    ParentAdapter parentAdapter;
    HashMap<ParentModelClass, ArrayList<MaterieOrar>> hashMap; // fiecare zi a sapt are o lista de materii
    ParentModelClass parentModelClassMonday;
    ParentModelClass parentModelClassTuesday;
    ParentModelClass parentModelClassWednesday;
    ParentModelClass parentModelClassThursday;
    ParentModelClass parentModelClassFriday;

    FloatingActionButton introducereMaterieNouaFAB;
    ImageView btn_go_home_orar;
    String[] listaSaptamana = {"Luni", "Marti", "Miercuri", "Joi", "Vineri"};// zilele sapt pt spinner din alertDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incercare2__orar);
        init();
        initFireStore();
        showData();

        //iesire din activitate
        btn_go_home_orar.setOnClickListener(view -> {
            Intent intent=new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        //apasare pe FAB pt a introduce o noua materie in orar
        introducereMaterieNouaFAB.setOnClickListener(view1 -> {
            //se deschide o noua fereastra de dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alertdialog_add_materie_orar);

            EditText edtMaterie = dialog.findViewById(R.id.edtMaterie);
            EditText edtSala = dialog.findViewById(R.id.edtSala);
            EditText edtProfesor = dialog.findViewById(R.id.edtProfesor);
            txtOraInceput = dialog.findViewById(R.id.txtOraInceput);
            oraAleasaInceput = dialog.findViewById(R.id.oraAleasaInceput);
            txtOraSfarsit = dialog.findViewById(R.id.txtOraSfarsit);
            oraAleasaSfarsit = dialog.findViewById(R.id.oraAleasaSfarsit);

            txtOraInceput.setOnClickListener(view2 -> {
                popTimePickerInceput();
            });

            txtOraSfarsit.setOnClickListener(view2 -> {
                popTimePickerSfarsit();
            });


            Spinner spinner = dialog.findViewById(R.id.spinnerZiSapt);
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSaptamana);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(ad);

            save = dialog.findViewById(R.id.btn_saveMaterie);
            save.setOnClickListener(view2 -> {

                boolean verifCampuri = true;
                //preiau campurile introduse
                this.denumire = edtMaterie.getText().toString();
                this.profesor = edtProfesor.getText().toString();
                this.sala = edtSala.getText().toString();

                if (TextUtils.isEmpty(denumire)) {
                    Toast.makeText(this, "Nume materie!", Toast.LENGTH_SHORT).show();
                    edtMaterie.setError("Camp gol");
                    edtMaterie.requestFocus();
                    verifCampuri = false;
                }
                if (TextUtils.isEmpty(profesor)) {
                    Toast.makeText(this, "Profesor!", Toast.LENGTH_SHORT).show();
                    edtProfesor.setError("Camp gol");
                    edtProfesor.requestFocus();
                    verifCampuri = false;
                }
                if (TextUtils.isEmpty(sala)) {
                    Toast.makeText(this, "Introdu numele", Toast.LENGTH_SHORT).show();
                    edtSala.setError("Camp gol");
                    edtSala.requestFocus();
                    verifCampuri = false;
                }

                if (oraAleasaInceput.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Introdu ora", Toast.LENGTH_SHORT).show();
                    oraAleasaInceput.setError("ora");
                    verifCampuri = false;
                }
                if (oraAleasaSfarsit.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Introdu ora", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                }
                if (oraInceput > oraSfarsit) {
                    Toast.makeText(this, "Ora invalida", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                } else if (minutIneput > minutSfarsit && oraInceput == oraSfarsit) {
                    Toast.makeText(this, "Ora invalida", Toast.LENGTH_SHORT).show();
                    oraAleasaSfarsit.setError("ora");
                    verifCampuri = false;
                }

                String oraInceputString = String.valueOf(this.oraInceput) + ":" + String.valueOf(this.minutIneput);
                String oraFinalString = String.valueOf(this.oraSfarsit) + ":" + String.valueOf(this.minutSfarsit);

                //verificare campuri si salvare obiect in BD
                if (verifCampuri) {
                    MaterieOrar materieOrar = new MaterieOrar(this.denumire, this.sala, this.profesor, this.ziSapt, oraInceputString, oraFinalString);

                    switch (this.ziSapt) {
                        case "Luni":
                            ArrayList<MaterieOrar> valoare = hashMap.get(parentModelClassMonday);
                            valoare.add(materieOrar);
                            sortareListaMaterieOrar(valoare);
                            hashMap.put(parentModelClassMonday, valoare);
                            break;

                        case "Marti":
                            ArrayList<MaterieOrar> valoare1 = hashMap.get(parentModelClassTuesday);
                            valoare1.add(materieOrar);
                            sortareListaMaterieOrar(valoare1);
                            hashMap.put(parentModelClassTuesday, valoare1);
                            break;


                        case "Miercuri":
                            ArrayList<MaterieOrar> valoare2 = hashMap.get(parentModelClassWednesday);
                            valoare2.add(materieOrar);
                            sortareListaMaterieOrar(valoare2);
                            hashMap.put(parentModelClassWednesday, valoare2);
                            break;


                        case "Joi":
                            ArrayList<MaterieOrar> valoare3 = hashMap.get(parentModelClassThursday);
                            valoare3.add(materieOrar);
                            sortareListaMaterieOrar(valoare3);
                            hashMap.put(parentModelClassThursday, valoare3);
                            break;


                        case "Vineri":
                            ArrayList<MaterieOrar> valoar4 = hashMap.get(parentModelClassFriday);
                            valoar4.add(materieOrar);
                            sortareListaMaterieOrar(valoar4);
                            hashMap.put(parentModelClassFriday, valoar4);
                            break;
                    }


                    saveDatainFireStore(); //SALVEZ IN BD

                    dialog.dismiss();
                    parentAdapter.notifyDataSetChanged();
                }
            });


            Toast.makeText(this, "apasat", Toast.LENGTH_SHORT).show();
            dialog.show();
        });

//        Toast.makeText(this, hashMap.size(), Toast.LENGTH_SHORT).show();


//        // Convert the HashMap to a JSON string
//        String hashMapJson = new Gson().toJson(hashMap);
//        // Store the JSON string in SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("your_pref_name", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("hash_map_key", hashMapJson);
//        editor.apply();


    } //end of OnCreate()

    private void init(){
        getSupportActionBar().hide();
        introducereMaterieNouaFAB = findViewById(R.id.add_new_subject_FAB);
        btn_go_home_orar=findViewById(R.id.btn_go_home_orar);
        recyclerView = findViewById(R.id.rv_parent_orar);

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

        parentAdapter = new ParentAdapter(hashMap, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();


    }

    private void initFireStore(){
        db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        // Get the Firestore reference for the user's "Orar" collection
        orarCollection = db.collection("users").document(userID).collection("Orar"); //preiaueiau referinta ed la colectia ORAR
    }

    public void showData(){
//        Toast.makeText(AOrar.this, "APELATA CITIREA", Toast.LENGTH_SHORT).show();
        orarCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                        hashMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ParentModelClass parentModelClass = new ParentModelClass();
                        parentModelClass.setId(document.getLong("id").intValue());
                        parentModelClass.setWeekDay(document.getString("weekDay"));

                        ArrayList<MaterieOrar> materieOrarList = new ArrayList<>();
                        CollectionReference materiiRef = document.getReference().collection("Materii");
                        materiiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        MaterieOrar materieOrar = new MaterieOrar();
                                        materieOrar.setDenumire(doc.getString("denumire"));
                                        materieOrar.setSala(doc.getString("sala"));
                                        materieOrar.setProfesor(doc.getString("profesor"));
                                        materieOrar.setZiSapt(doc.getString("ziSapt"));
                                        materieOrar.setInceput(doc.getString("inceput"));
                                        materieOrar.setSfarsit(doc.getString("sfarsit"));
                                        materieOrarList.add(materieOrar);
                                    }

                                    sortareListaMaterieOrar(materieOrarList);

                                    //dupa sortarea listei, se introduce lista cu materii in hashmap la ziua corespunzatoare
                                    hashMap.put(parentModelClass, materieOrarList);

                                    parentAdapter.notifyDataSetChanged();
                                } else {
                                    Log.e(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
                    // Update the data source of the parent adapter with the retrieved data
//                    parentAdapter.updateData(hashMap);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void sortareListaMaterieOrar(ArrayList<MaterieOrar> materieOrarList){
        // Sort the materieOrarList based on the inceput property as time values >sortare dupa ora de inceput
        Collections.sort(materieOrarList, new Comparator<MaterieOrar>() {
            @Override
            public int compare(MaterieOrar o1, MaterieOrar o2) {
                String inceput1 = o1.getInceput();
                String inceput2 = o2.getInceput();
                // Assuming the format is "HH:mm"
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                try {
                    Date time1 = timeFormat.parse(inceput1);
                    Date time2 = timeFormat.parse(inceput2);
                    return time1.compareTo(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    public void saveDatainFireStore() {
//        initFireStore();

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
    public void popTimePickerInceput() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                oraInceput = selectedHour;
                minutIneput = selectedMinute;
                oraAleasaInceput.setText(String.format(Locale.getDefault(), "%02d:%02d", oraInceput, minutIneput, true));

            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, oraInceput, minutIneput, true);
        timePickerDialog.setTitle("Ora Incepere");
        timePickerDialog.show();
    }

    public void popTimePickerSfarsit() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                oraSfarsit = selectedHour;
                minutSfarsit = selectedMinute;
                oraAleasaSfarsit.setText(String.format(Locale.getDefault(), "%02d:%02d", oraSfarsit, minutSfarsit, true));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, oraInceput, minutSfarsit, true);
        timePickerDialog.setTitle("Ora Finalizare");
        timePickerDialog.show();
    }


    //AlertDialog -spinner de ales zi saptamana
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //cand se selecteaza ceva din spinner
        this.ziSapt = listaSaptamana[position];
        Toast.makeText(this, listaSaptamana[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}