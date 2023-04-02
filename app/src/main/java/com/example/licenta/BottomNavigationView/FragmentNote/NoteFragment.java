package com.example.licenta.BottomNavigationView.FragmentNote;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoteFragment extends Fragment implements RecyclerViewAdapterMaterii.OnButtonClickListener {

    ArrayList<Materie> materii = new ArrayList<Materie>();
    List<Float> listaNote = new ArrayList<Float>();
    ArrayList<Float> listaNoteINDIVIDUALA;

    RecyclerViewAdapterMaterii adapterMaterii;

    LinearLayout layout_listaNote;
    RecyclerView recyclerView;
    FloatingActionButton btnOpenDialogFAB;

    Boolean RESETARE = false;

    Materie materiePreluata;
    Materie materie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView = view.findViewById(R.id.recycleViewNoteFragment);
        btnOpenDialogFAB = view.findViewById(R.id.btnOpenDialog);

        adapterMaterii = new RecyclerViewAdapterMaterii(getContext(), materii, this);
        recyclerView.setAdapter(adapterMaterii);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //functie care imi preia notele din BD
        //initilizez instanta FIRESTORE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        String collectionName = "NoteMaterii";

        DocumentReference documentReference = db.collection("users").document(userID);
        CollectionReference colectie=documentReference.collection(collectionName);

        if(materii.size()==0){
            colectie.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot queryDocumentSnapshots1: queryDocumentSnapshots){
                                Materie materie=queryDocumentSnapshots1.toObject(Materie.class);
                                String materieID= queryDocumentSnapshots1.getId();
                                materie.setId(materieID);
                                materii.add(materie);
                                adapterMaterii.notifyDataSetChanged();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
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
            //se deschide AlertDialog
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.alertdialog_add);
            EditText edtNameMaterie = dialog.findViewById(R.id.edtNameMaterie);
            Button btn_saveNote = dialog.findViewById(R.id.btn_saveNote);
            Button btn_add = dialog.findViewById(R.id.btn_add);

            layout_listaNote = dialog.findViewById(R.id.layout_listaNote);

            btn_add.setOnClickListener(view2 -> {
                addView();
            });

            btn_saveNote.setOnClickListener(view2 -> {
                if (creareListaNote()) {

                    if (!edtNameMaterie.getText().toString().equals("")) {
                         materie = new Materie(edtNameMaterie.getText().toString(), listaNoteINDIVIDUALA);
                        materii.add(materie);
                        adapterMaterii.notifyItemInserted(materii.indexOf(materie));

                        Log.d("mesaj",colectie.getPath());

                        Map<String, Object> NoteMaterii = new HashMap<>();
                        NoteMaterii.put("denumire",materie.getDenumire());
                        NoteMaterii.put("listanote", listaNoteINDIVIDUALA);


                        colectie.add(NoteMaterii)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("OK",documentReference.getPath().toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("NOTOK",e.getMessage().toString());
                                    }
                                });

//                Map<String ,Object> user= new HashMap<>();
//                user.put("notePeMaterie",materiePreluata.getDenumire());
//
//                String key=userRef.document().getId();
//                userRef.document(key).getId().


                    } else {
                        Toast.makeText(getContext(), "Nume materie lipsa!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Introdu nota!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();


            });

            dialog.show();
        });
        return view;
    }

    private boolean creareListaNote() {
        listaNoteINDIVIDUALA = new ArrayList<>();
//        materii.clear();
        boolean result = true;
        for (int i = 0; i < layout_listaNote.getChildCount(); i++) {
            View notaview = layout_listaNote.getChildAt(i);
            EditText editTextNota = notaview.findViewById(R.id.edit_add_nota);

            if (!editTextNota.getText().toString().equals("")) {
                listaNoteINDIVIDUALA.add(Float.valueOf(editTextNota.getText().toString()));
//                listaNote.add(Float.valueOf(editTextNota.getText().toString()));


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


        EditText edtNameMaterie = dialog.findViewById(R.id.edtNameMaterie);
        Button btn_saveNote = dialog.findViewById(R.id.btn_saveNote);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        layout_listaNote = dialog.findViewById(R.id.layout_listaNote);


        materiePreluata = materii.get(position); //asta e obiectul pe care am apasat
        int index = materii.indexOf(materiePreluata); //salvez pozitia obiectului pe care am apasat
        edtNameMaterie.setText(materiePreluata.getDenumire()); // preiau denumirea lui initiala


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

//            if (creareListaNote()) {

            if (!edtNameMaterie.getText().toString().equals("")) {
                Materie materie = new Materie(edtNameMaterie.getText().toString(), listaNoteINDIVIDUALA);
                materii.set(index, materie);
                adapterMaterii.notifyItemChanged(index);
            } else {
                Toast.makeText(getContext(), "Nume materie lipsa!", Toast.LENGTH_SHORT).show();
            }
//            } else {
//                Toast.makeText(getContext(), "Introdu nota!", Toast.LENGTH_SHORT).show();
//            }
            dialog.dismiss();
        });


        dialog.show();

    }

    @Override
    public void onDeleteClick(int position) {
//-------------------------face stergere in firestore si pe ecran, insa da eroare cand positie==materii.size()

//        if(position < materii.size()){
//            materii.remove(position);
//            adapterMaterii.notifyItemRemoved(position);
//        }

        if (materii.size() > 0) {
            Materie materie = materii.get(position);
            materii.remove(materie);
            adapterMaterii.notifyItemRemoved(materii.indexOf(materie));

            //stergere in firestore a materiei din coletie
            //functie care imi preia notele din BD initilizez instanta FIRESTORE
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userID = mAuth.getCurrentUser().getUid();
            String collectionName = "NoteMaterii";

            DocumentReference documentReference = db.collection("users").document(userID);
            CollectionReference colectie=documentReference.collection(collectionName);

            DocumentReference elemSters=colectie.document(materie.getId());
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

//        Materie materie=materii.get(position);
//        materii.remove(materie);
//        adapterMaterii.notifyItemRemoved(position);
    }
}