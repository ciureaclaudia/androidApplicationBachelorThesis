package com.example.licenta.NavigationDrawer.toDoList;

import static android.content.ContentValues.TAG;

import com.example.licenta.BottomNavigationView.FragmentNote.DataCallback;
import com.example.licenta.BottomNavigationView.FragmentNote.Materie;
import com.example.licenta.BottomNavigationView.OrarFragment.NesterRecyclerViewOrar.MaterieOrar;
import com.example.licenta.NavigationDrawer.toDoList.Adapter.ToDoAdapter;
import com.example.licenta.NavigationDrawer.toDoList.Model.ToDoModel;
import com.example.licenta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_new_task extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "AddNewTask";

    private TextView setDueDate;
    private EditText mTaskEdit;
    private Button mSaveBtn;

    //nou
    private RadioGroup radioGroupDificultate;
    private RadioButton radioButton1_usor;
    private RadioButton radioButton2_mediu;
    private RadioButton radioButton3_dificil;
    private Spinner spinnerMaterii;
    List<String> listaMateriiOrar = new ArrayList<>(); //lista cu toate materiile
    ArrayAdapter<String> adapter;
    private boolean isItemSelected = false;


    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userID;
    private CollectionReference todoTaskRef;
    private CollectionReference orarCollection;

    private Context context;

    private String dueDate = "";
    private String id = ""; //pt update task
    private String dueDateUpdate = ""; //pt update task
    private String materieDenumire;
    private int dificultate=1; //1-usor, 2-mediu, 3-dificil
    private int dificultateUpdate;

//    private ProgresFragment progresFragment;



    public static Add_new_task newInstance() {
        return new Add_new_task();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);

    }

    //preiau datele introduse in orar si le salvez in:  List<String> listaMateriiOrar
    // populez lista listaMateriiOrar doar cu denumirile de materii din orar PT SPINNER
    public void showData() {
        orarCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CollectionReference materiiRef = document.getReference().collection("Materii");
                        materiiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        MaterieOrar materieOrar = doc.toObject(MaterieOrar.class);
                                        listaMateriiOrar.add(materieOrar.getDenumire()); //populez lista mea de denumiri de materii cu denumirile preluate din bd
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "Eroare", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
//                    callback.onDataLoaded(listaMateriiOrar);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        mTaskEdit = view.findViewById(R.id.task_edittext);
        mSaveBtn = view.findViewById(R.id.save_btn);

        //nou
        spinnerMaterii=view.findViewById(R.id.spinner_materii_toDo);
        radioGroupDificultate=view.findViewById(R.id.radioGroup);
        radioButton1_usor = view.findViewById(R.id.radioButton1_usor);
        radioButton2_mediu=view.findViewById(R.id.radioButton2_mediu);
        radioButton3_dificil=view.findViewById(R.id.radioButton3_dificil);


        //---------------------RADIO BUTTON

        radioGroupDificultate.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton radioButton = view.findViewById(checkedId);
//                Toast.makeText(getContext(), "Selected option: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
            if(radioButton.getText().equals("usor")) {
                dificultate=1;
            } else if(radioButton.getText().equals("mediu"))
                dificultate=2;
            else dificultate=3;
            Toast.makeText(getContext(), "Selected option: " + dificultate, Toast.LENGTH_SHORT).show();
        });



        //---------------------SPINNER MATERII
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        todoTaskRef = firestore.collection("users").document(userID).collection("Tasks");
        orarCollection = firestore.collection("users").document(userID).collection("Orar"); //preiaueiau referinta de la colectia ORAR

        showData();//creez lista pt adapter de materii: listaMaterii note
        //set adapter pt spinner, the adapter holds the values for the spinner
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, listaMateriiOrar);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterii.setAdapter(adapter);
        spinnerMaterii.setOnItemSelectedListener( this);



        boolean isUpdate = false; //check if the user wants to edit the task or save it for the first time

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true; ///user updates the task
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");
            dificultateUpdate= Integer.parseInt(bundle.getString("dificultate"));


            if(dificultateUpdate==1){
                radioButton1_usor.setChecked(true);
            } else if(dificultateUpdate==2){
                radioButton2_mediu.setChecked(true);
            } else radioButton3_dificil.setChecked(true);

            mTaskEdit.setText(task);
            setDueDate.setText(dueDateUpdate);

            // selectare spinner
            materieDenumire=bundle.getString("materie")  ;
            for(int i=0; i<listaMateriiOrar.size();i++) {
                if(listaMateriiOrar.get(i).equals(materieDenumire)){
                    spinnerMaterii.setSelection(i);
                }
            }

            //disable the button save
            if (task.length() > 0 || dueDateUpdate.length() > 0) {
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        dueDate = dayOfMonth + "/" + month + "/" + year;

                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });


        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String task = mTaskEdit.getText().toString();


                if (finalIsUpdate) {
                    //asta se executa cand este editatat taskul
                    todoTaskRef.document(id).update("task", task, "due", dueDate,"materie",materieDenumire,"dificultate",dificultate);
                    Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();
                } else {
                    //asta se executa cand userul salveza taskul pt prima data
                    if (task.isEmpty() || dueDate.isEmpty()) {
                        Toast.makeText(context, "CAMP GOL !!", Toast.LENGTH_SHORT).show();
                    } else {

                        Map<String, Object> taskMap = new HashMap<>();

                        taskMap.put("task", task);
                        taskMap.put("due", dueDate);
                        taskMap.put("status", 0);
                        taskMap.put("time", FieldValue.serverTimestamp());
                        taskMap.put("materie",materieDenumire);
                        taskMap.put("dificultate",dificultate); //1 , 2 sau 3

                        todoTaskRef.add(taskMap)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();


                                            //dupa ce salvez obiectul in FS , trebuie sa obtin lista actualizata de obiecte
                                            //si sa setez in adaptorul RecyclerViewului
                                            //apoi, notific adaptorul ca  s au schimbat datele
/*
                                            todoTaskRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    //obtin lista
                                                    List<ToDoModel> lista=queryDocumentSnapshots.toObjects(ToDoModel.class);
                                                    progresFragment=new ProgresFragment();
                                                    adapter =new ToDoAdapter(progresFragment,lista);
                                                    //Setez lista in adaptor
                                                    adapter.setList(lista);
                                                    //notific adaptorul ca s-au schi,bat datele
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG,"Eroare in adaugare document",e);
                                                }
                                            }); */



                                        } else {
                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                dismiss();
            }
        });
    }

    //attach the context
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        isItemSelected = true;
        this.materieDenumire=listaMateriiOrar.get(i);
        Toast.makeText(getContext(), this.materieDenumire, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (!isItemSelected) {
            Toast.makeText(requireContext(), "Nothing selected", Toast.LENGTH_SHORT).show();

        }
    }
}
