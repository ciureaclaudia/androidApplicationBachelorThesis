package com.example.licenta.NavigationDrawer.toDoList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProgresFragment extends Fragment implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton mFab;

    private FirebaseFirestore firestore;
    private  FirebaseAuth mAuth;
    private String userID;
    private CollectionReference TodoTaskRef;

    private ToDoAdapter adapter;
    private List<ToDoModel> mList;//lista de taskuri
    private Query query;
    private ListenerRegistration listenerRegistration;

    private ImageView check_box_graf;
    private ImageView arrowUp;
    private ImageView arrowDown;
    private ProgressBar progressBar;
    private TextView textViewProgress;


/*

    //aici declar un obiect de tipul Receiver care primeste intentul de la ADAPTOR
    private BroadcastReceiver updateUIReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get extra data included in the Intent
//            ToDoModel obiectul_meu= (ToDoModel) intent.getSerializableExtra("obiectul_meu");
//
//            if(obiectul_meu!=null){
//                //am preluat obiectul si trebuie sa apara pe ecran
//            }

            List<ToDoModel> listaDinAdaptor=adapter.getTodoList();//preaaiu lista de obiecte din adaptor

            adapter.notifyDataSetChanged();//notifica adaptorul ca s au schimbat datele


            //pt a actualiza datele afisate in recyclerView trebuie sa notific adaptorul ca s au schimbat datele
        }
    };


 */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_progres, container, false);

        init(view);

        //apas pe buton de plus
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_new_task.newInstance().show(getChildFragmentManager() , Add_new_task.TAG);
            }

        });

        //in OnCreateView trebuie sa inregistrez BroadCastReceiverul
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateUIReceiver,new IntentFilter("update_UI"));

        mList = new ArrayList<>();
//        adapter = new ToDoAdapter(ProgresFragment.this, mList); //context si lista de taskuri

        //pt swipe stanga si dreapta
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        Toast.makeText(getContext(), "PRESHOW", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(adapter);
        showData();


        check_box_graf.setOnClickListener(view1 -> {
            int procent =(int) adapter.getCountCheckBox();
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.alertdialog_checkbox_progress);
            progressBar=dialog.findViewById(R.id.progress_bar);
            textViewProgress=dialog.findViewById(R.id.text_view_progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(procent);
            textViewProgress.setText(procent +"%");
            dialog.show();
//            dialog.dismiss();
//            progressBar.setVisibility(View.GONE);
        });

        arrowUp=view.findViewById(R.id.arrow_up);
        arrowUp.setOnClickListener(view1 -> {
            //se ordoneza lista crescator: de la apropiat la indepartat

        });
        arrowDown=view.findViewById(R.id.arrow_down);
        arrowDown.setOnClickListener(view1 -> {
            //se ordoneaza descrescator: de la indepartat la apropiat
        });


        return view;
    }

    public void showData(){
        Toast.makeText(getContext(), "APELATA CITIREA", Toast.LENGTH_SHORT).show();
        query= TodoTaskRef.orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange: value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModel toDoModel=documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                        Toast.makeText(getContext(), value.size()+"", Toast.LENGTH_SHORT).show();
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

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycerlview);
        mFab = view.findViewById(R.id.floatingActionButton);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        TodoTaskRef = firestore.collection("users").document(userID).collection("Tasks");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //pt progress bar
        check_box_graf=view.findViewById(R.id.check_box_graf);


    }

    //deregistrez BroadCastReceiver in metoda onDestryView
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateUIReceiver);
//    }
}