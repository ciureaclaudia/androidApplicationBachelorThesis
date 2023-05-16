package com.example.licenta.NavigationDrawer.toDoList.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.licenta.NavigationDrawer.toDoList.AProgres;
import com.example.licenta.NavigationDrawer.toDoList.Add_new_task;
import com.example.licenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.example.licenta.NavigationDrawer.toDoList.Model.ToDoModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> todoList;
    private AProgres activity;
//    private Fragment fragmentActivity;


    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    String userID;
    CollectionReference todoTaskRef;

    //   constructor pt ProgresFragment
//    public ToDoAdapter(Fragment fragmentActivity, List<ToDoModel> todoList) {
//        this.fragmentActivity = fragmentActivity;
//        this.todoList = todoList;
//    }

    //constructor pt activitatea AProgres
    public ToDoAdapter(AProgres activity, List<ToDoModel> todoList) {
        this.activity=activity;
        this.todoList = todoList;
    }


    @NonNull
    @Override
    public ToDoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
//        View view = LayoutInflater.from(fragmentActivity.getContext()).inflate(R.layout.each_task, parent, false);
        View view = inflater.inflate(R.layout.each_task, parent, false);
        init();
        return new ToDoAdapter.MyViewHolder(view);
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid(); //preaiu ID ul de la user
        todoTaskRef = firestore.collection("users").document(userID).collection("Tasks");
    }


    public void editTask(int position) {
        ToDoModel toDoModel = todoList.get(position);

        //trebuie salvat in edittext pt editare
        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("id", toDoModel.TaskId);

        Add_new_task addNewTask = new Add_new_task();
        addNewTask.setArguments(bundle); //se incarca obiectul care sa apara in fragment
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag()); // sa apara pe ecran

//        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    //metoda care sterge un elem din lista de pe position
    public void deleteTask(int position) {
        ToDoModel toDoModel = todoList.get(position);
        todoTaskRef.document(toDoModel.TaskId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),   "Adapter-sters", Toast.LENGTH_SHORT).show();
                        todoList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ToDoAdapter", "Error deleting document", e);
                    }
                });
    }

    public float getCountCheckBox() {
        int count = 0;
        for (ToDoModel model : todoList) {
            if (model.getStatus() == 1) {
                count++;
            }
        }
        return ((float) count / todoList.size()) * 100;
    }
// pt fragemnt
//    public Context getContext() {
//        return fragmentActivity.getContext();
//    }

    public Context getContext() {
        return activity;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ToDoModel toDoModel = todoList.get(position); //elem din lista de pe pos
        holder.mCheckBox.setText(toDoModel.getTask());

        holder.mDueDateTv.setText("Due On " + toDoModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    todoTaskRef.document(toDoModel.TaskId).update("status", 1);
                } else {
                    todoTaskRef.document(toDoModel.TaskId).update("status", 0);
                }
            }
        });

//        Intent intent=new Intent("update_UI");
////        intent.putExtra("obiectul_meu",toDoModel);
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }


    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mDueDateTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }

}
