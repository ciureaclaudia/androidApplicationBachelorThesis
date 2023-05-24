package com.example.licenta.NavigationDrawer.toDoList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.MainActivity;
import com.example.licenta.NavigationDrawer.toDoList.Adapter.ToDoAdapter;
import com.example.licenta.NavigationDrawer.toDoList.Model.ToDoModel;
import com.example.licenta.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AProgres extends AppCompatActivity implements OnDialogCloseListener{

    private RecyclerView recyclerView;
    private FloatingActionButton mFab;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userID;
    private CollectionReference TodoTaskRef;

    private ToDoAdapter adapter;
    private List<ToDoModel> mList;//lista de taskuri
    private Query query;
    private ListenerRegistration listenerRegistration;

    private ImageView check_box_graf;
    private ImageView go_to_home;
    private ImageView arrowUp;
    private ImageView arrowDown;
    private ProgressBar progressBar;
    private TextView textViewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progres);

        init();

        //apas pe buton de plus
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_new_task.newInstance().show(getSupportFragmentManager(), Add_new_task.TAG);
            }
        });

        //in OnCreateView trebuie sa inregistrez BroadCastReceiverul
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateUIReceiver,new IntentFilter("update_UI"));

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(this, mList); //context si lista de taskuri

        //pt swipe stanga si dreapta
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        Toast.makeText(this, "PRESHOW", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(adapter);
        showData();
        check_box_graf.setOnClickListener(view1 -> {
            int procent = (int) adapter.getCountCheckBox();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alertdialog_checkbox_progress);
            progressBar = dialog.findViewById(R.id.progress_bar);
            textViewProgress = dialog.findViewById(R.id.text_view_progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(procent);
            textViewProgress.setText(procent + "%");
            dialog.show();
//            dialog.dismiss();
//            progressBar.setVisibility(View.GONE);
        });

        arrowUp = findViewById(R.id.arrow_up);
        arrowUp.setOnClickListener(view1 -> {
            //se ordoneza lista crescator: de la apropiat la indepartat
            sortListAscending();
            Toast.makeText(AProgres.this,  " ascendent", Toast.LENGTH_SHORT).show();

        });
        arrowDown = findViewById(R.id.arrow_down);
        arrowDown.setOnClickListener(view1 -> {
            //se ordoneaza descrescator: de la indepartat la apropiat
            sortListDescending();
            Toast.makeText(AProgres.this,  " descendent", Toast.LENGTH_SHORT).show();


        });
        go_to_home.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        });
    }

    private void sortListAscending() {
        Collections.sort(mList, new Comparator<ToDoModel>() {
            @Override
            public int compare(ToDoModel model1, ToDoModel model2) {
                return model1.getDue().compareTo(model2.getDue());
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void sortListDescending() {
        Collections.sort(mList, new Comparator<ToDoModel>() {
            @Override
            public int compare(ToDoModel model1, ToDoModel model2) {
                return model2.getDue().compareTo(model1.getDue());
            }
        });

        adapter.notifyDataSetChanged();
    }

    private boolean isDateToday(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the date format according to your string representation
        String todayDate = dateFormat.format(new Date());

        return dateString.equals(todayDate);
    }

    public void showData(){
        Toast.makeText(AProgres.this, "APELATA CITIREA", Toast.LENGTH_SHORT).show();
        query= TodoTaskRef.orderBy("time", Query.Direction.DESCENDING);
        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange: value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModel toDoModel=documentChange.getDocument().toObject(ToDoModel.class).withId(id);


                        mList.add(toDoModel);
                        adapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });
    }




    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
    private void init(){
        recyclerView = findViewById(R.id.recycerlview);
        mFab = findViewById(R.id.floatingActionButton);
        check_box_graf=findViewById(R.id.check_box_graf);
        go_to_home=findViewById(R.id.btn_go_home);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        TodoTaskRef = firestore.collection("users").document(userID).collection("Tasks");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AProgres.this));

        getSupportActionBar().hide();

    }
}