package com.example.licenta.BottomNavigationView.FragmentNote;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.licenta.BottomNavigationView.FragmentNote.V2.MaterieModel;
import com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar.AOrar;
import com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar.MaterieOrar;
import com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar.ParentModelClass;
import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoteFragment extends Fragment implements  RecyclerViewAdapterMaterii.OnButtonClickListener, AdapterView.OnItemSelectedListener, DataCallback{

    ArrayList<Materie> materii = new ArrayList<Materie>();
    ArrayList<Float> listaNoteINDIVIDUALA;

    RecyclerViewAdapterMaterii adapterMaterii;

    LinearLayout layout_listaNote;
    RecyclerView recyclerView;
    FloatingActionButton btnOpenDialogFAB;

    Boolean RESETARE = false;

    Materie materiePreluata;
    Materie materie=new Materie();

    Spinner spinner;
    //    HashMap<ParentModelClass, ArrayList<MaterieOrar>> hashMap = new HashMap<>(); // fiecare zi a sapt are o lista de materii
    List<String> listaMateriiOrar = new ArrayList<>(); //lista cu toate materiile
    ArrayAdapter<String> adapter;

    private boolean isItemSelected = false;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private CollectionReference orarCollection;
    private Query query;
    private ListenerRegistration listenerRegistration;

    String collectionName = "NoteMaterii";
    DocumentReference documentReference;
    CollectionReference colectie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        db = FirebaseFirestore.getInstance(); //preaiu instanta de la BD
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        // Get the Firestore reference for the user's "Orar" collection
        documentReference = db.collection("users").document(userID);
        orarCollection = documentReference.collection("Orar"); //preiaueiau referinta ed la colectia ORAR

        recyclerView = view.findViewById(R.id.recycleViewNoteFragment);
        btnOpenDialogFAB = view.findViewById(R.id.btnOpenDialog);
        adapterMaterii = new RecyclerViewAdapterMaterii(getContext(), materii, this);
        recyclerView.setAdapter(adapterMaterii);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        showData(this); //mi se populeaza listaMateriiOrar


//        for (String element : listaMateriiOrar) {
//            Log.d(TAG, "Element: " + element);
//        }



       //preaiu notele din BD
        colectie = documentReference.collection(collectionName);
        if (materii.size() == 0) {
            colectie.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots) {
                        Materie materie = queryDocumentSnapshots1.toObject(Materie.class);
                        String materieID = queryDocumentSnapshots1.getId();
                        materie.setId(materieID);
                        materii.add(materie);
                        adapterMaterii.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("NOTOK", e.getMessage().toString());
                }
            });
        }

        //get pe stud
        //verifica sa nu adaugi aiurea - clear pe lisa de obiecte, adica materii
        //materii.size==0
        //doar asa faci get


        btnOpenDialogFAB.setOnClickListener(view1 -> {
//             showData();
            //se deschide AlertDialog
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.alertdialog_add);

            spinner = (Spinner) dialog.findViewById(R.id.spinner_materie);
            //set adapter pt spinner, the adapter holds the values for the spinner
//            adapter = new ArrayAdapter<String>(getContext(),
//                    android.R.layout.simple_spinner_item, listaMateriiOrar);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            Button btn_saveNote = dialog.findViewById(R.id.btn_saveNote);
            Button btn_add = dialog.findViewById(R.id.btn_add);

            layout_listaNote = dialog.findViewById(R.id.layout_listaNote);

            btn_add.setOnClickListener(view2 -> {
                addView();
            });

            btn_saveNote.setOnClickListener(view2 -> {
                if (creareListaNote()) {

//                    if (!edtNameMaterie.getText().toString().equals("")) {
//                        if(spinner.getSelectedItem()){

//                        materie = new Materie(edtNameMaterie.getText().toString(), listaNoteINDIVIDUALA);

//                    setDenumire e setata in metoda onItemSelected
                    materie.setListanote(listaNoteINDIVIDUALA);
                    materii.add(materie);
                    adapterMaterii.notifyItemInserted(materii.indexOf(materie));

                    Log.d("mesaj", colectie.getPath());

                    Map<String, Object> NoteMaterii = new HashMap<>();
                    NoteMaterii.put("denumire", materie.getDenumire());
                    NoteMaterii.put("listanote", listaNoteINDIVIDUALA);


                    colectie.add(NoteMaterii).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("OK", documentReference.getPath().toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("NOTOK", e.getMessage().toString());
                        }
                    });

//                Map<String ,Object> user= new HashMap<>();
//                user.put("notePeMaterie",materiePreluata.getDenumire());
//
//                String key=userRef.document().getId();
//                userRef.document(key).getId().


//                    } else {
//                        Toast.makeText(getContext(), "Nume materie lipsa!", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(getContext(), "Introdu nota!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();


            });

            dialog.show();
        });

        return view;
    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//
//        // Retrieve the JSON string from SharedPreferences
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("your_pref_name", Context.MODE_PRIVATE);
//        String hashMapJson = sharedPreferences.getString("hash_map_key", null);
//        // Convert the JSON string back to a HashMap
//        hashMap= new Gson().fromJson(hashMapJson, new TypeToken<HashMap<ParentModelClass, ArrayList<MaterieOrar>>>(){}.getType());
//        // Now you can use yourHashMap
//        if(hashMap!=null){
//            for (Map.Entry<ParentModelClass, ArrayList<MaterieOrar>> entry : hashMap.entrySet()) {
//                ParentModelClass key = entry.getKey(); //KEY - NUME SAPT
//                ArrayList<MaterieOrar> value = entry.getValue(); //VALUE-LISTA MATERII
//
//                for (MaterieOrar m : value) {
//                    listaMateriiOrar.add(m.getDenumire());
//                }
//            }
//        } else {
//            Toast.makeText(getContext(), "GOL", Toast.LENGTH_SHORT).show();
//
//        }
//    }


    public void showData(final DataCallback callback) {
//        Toast.makeText(getContext(), "INTRA", Toast.LENGTH_SHORT).show();
        orarCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                Log.d("NOTE", task.isSuccessful()+""+task.getResult().size());
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d("NOTE", document.toObject(ParentModelClass.class).getWeekDay());
                        //ParentModelClass parentModelClass = new ParentModelClass();
                        //parentModelClass.setId(document.getLong("id").intValue());
                        //parentModelClass.setWeekDay(document.getString("weekDay"));

//                        ParentModelClass parentModelClass = document.toObject(ParentModelClass.class);//pt hashmap
//                        ArrayList<MaterieOrar> materieOrarList = new ArrayList<>();//pt hashmap

                        CollectionReference materiiRef = document.getReference().collection("Materii");
                        materiiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                                        Log.d("ZILE", doc.toString());
//                                        MaterieOrar materieOrar = new MaterieOrar();
//                                        materieOrar.setDenumire(doc.getString("denumire"));
//                                        materieOrar.setSala(doc.getString("sala"));
//                                        materieOrar.setProfesor(doc.getString("profesor"));
//                                        materieOrar.setZiSapt(doc.getString("ziSapt"));
//                                        materieOrar.setInceput(doc.getString("inceput"));
//                                        materieOrar.setSfarsit(doc.getString("sfarsit"))
                                        MaterieOrar materieOrar = doc.toObject(MaterieOrar.class);
//                                        Log.d("NOTE", materieOrar.getDenumire());
//                                        materieOrarList.add(materieOrar);
                                        listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
//                                        Log.d("LIST", materieOrarList.size()+" ");
                                    }
//                                    hashMap.put(parentModelClass, materieOrarList);
//                                    Log.d("HASHMAP", hashMap.size()+" ");
                                    adapter.notifyDataSetChanged();
//                                    Toast.makeText(getContext(), hashMap.size()+" ", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getContext(), "Eroare", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }

                    callback.onDataLoaded(listaMateriiOrar);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }

    @Override
    public void onDataLoaded(List<String> listaMateriiOrar) {
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listaMateriiOrar);

    }




    private boolean creareListaNote() {
        listaNoteINDIVIDUALA = new ArrayList<>();

        boolean result = true;
        for (int i = 0; i < layout_listaNote.getChildCount(); i++) {
            View notaview = layout_listaNote.getChildAt(i);
            EditText editTextNota = notaview.findViewById(R.id.edit_add_nota);

            if (!editTextNota.getText().toString().equals("")) {
                listaNoteINDIVIDUALA.add(Float.valueOf(editTextNota.getText().toString()));


            } else {
                result = false;
                listaNoteINDIVIDUALA.clear();
            }
        }

//        if(materii.size()==0){
//            result=false;
//            Toast.makeText(getContext(), "Introdu notele", Toast.LENGTH_SHORT).show();
//        }
//        else if(result){
//            Toast.makeText(getContext(), "Introdu toate detaliile corect", Toast.LENGTH_SHORT).show();
//        }
        return result;
    }

    private void addView() {
        final View notaview = getLayoutInflater().inflate(R.layout.row_add_nota, null, false);
        EditText editText = notaview.findViewById(R.id.edit_add_nota);
        ImageView imageClose = notaview.findViewById(R.id.img_sterge_nota);

        imageClose.setOnClickListener(view -> {
            removeView(notaview);
        });

        layout_listaNote.addView(notaview);
    }

    private void removeView(View view) {
        layout_listaNote.removeView(view);
    }

    @Override
    public void onEditClick(int position) {
        //------------------------------------------nu merge

        //se deschide alertDialog -trebuie sa aiba datele in el
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.alertdialog_add);


        Button btn_saveNote = dialog.findViewById(R.id.btn_saveNote);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        layout_listaNote = dialog.findViewById(R.id.layout_listaNote);


        materiePreluata = materii.get(position); //asta e obiectul pe care am apasat
        int index = materii.indexOf(materiePreluata); //salvez pozitia obiectului pe care am apasat


        for (int i = 0; i < materiePreluata.getListanote().size(); i++) {
            addView();
//            View notaview = layout_listaNote.getChildAt(i); //reference to the parent view group
            EditText editTextNota = new EditText(getContext());
            editTextNota.setText(materiePreluata.getListanote().toString());
            layout_listaNote.addView(editTextNota);
//            listaNoteINDIVIDUALA.add(Float.valueOf(editTextNota.getText().toString()));
        }


//        if(listaNoteINDIVIDUALA!=null){
//            for (int i = 0; i < materiePreluata.getListanote().size(); i++) {
//
////            View notaview = layout_listaNote.getChildAt(i); //reference to the parent view group
//                EditText editTextNota = new EditText(getContext());
//                editTextNota.setText(materiePreluata.getListanote().toString());
//                layout_listaNote.addView(editTextNota);
////            listaNoteINDIVIDUALA.add(Float.valueOf(editTextNota.getText().toString()));
//            }
//        }


//        for (int i = 0; i < layout_listaNote.getChildCount(); i++) {

//            if (!editTextNota.getText().toString().equals("")) {
//                listaNoteINDIVIDUALA.add(Float.valueOf(editTextNota.getText().toString()));
////                listaNote.add(Float.valueOf(editTextNota.getText().toString()));
//            } else {
//                listaNoteINDIVIDUALA.clear();
//            }
//        }


        btn_add.setOnClickListener(view2 -> {
            addView();
        });

        btn_saveNote.setOnClickListener(view2 -> {

            if (creareListaNote()) {

//                if (!edtNameMaterie.getText().toString().equals("")) {
//                    Materie materie = new Materie(edtNameMaterie.getText().toString(), listaNoteINDIVIDUALA);
//                    materii.set(index, materie);
//                    adapterMaterii.notifyItemChanged(index);
//                } else {
//                    Toast.makeText(getContext(), "Nume materie lipsa!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getContext(), "Introdu nota!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });


        dialog.show();

    }

    @Override
    public void onDeleteClick(int position) {
        if (materii.size() > 0) {

            if (position >= 0 && position < materii.size()) {
                Materie materie = materii.get(position);
                adapterMaterii.notifyItemRemoved(materii.indexOf(materie));
                materii.remove(materie);

                System.out.println(position);


                //stergere in firestore a materiei din coletie
                //functie care imi preia notele din BD initilizez instanta FIRESTORE
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userID = mAuth.getCurrentUser().getUid();
                String collectionName = "NoteMaterii";

                DocumentReference documentReference = db.collection("users").document(userID);
                CollectionReference colectie = documentReference.collection(collectionName);

                DocumentReference elemSters = colectie.document(materie.getId());
                elemSters.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

            }

        }

    }

    //cand se apeleaza ceva din spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        isItemSelected = true;
        this.materie.setDenumire(listaMateriiOrar.get(i)); //setez denumirea materiei din spinner
        Toast.makeText(getContext(), listaMateriiOrar.get(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (!isItemSelected) {
            Toast.makeText(requireContext(), "Nothing selected", Toast.LENGTH_SHORT).show();

        }
    }
}