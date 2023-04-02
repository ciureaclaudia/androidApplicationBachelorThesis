package com.example.licenta.NavigationDrawer.ToDoListProgress.DoToAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.NavigationDrawer.ToDoListProgress.AddNewtask;
import com.example.licenta.NavigationDrawer.ToDoListProgress.Model.ToDoModel;
import com.example.licenta.NavigationDrawer.ToDoListProgress.ProgresFragment;
import com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB.DBManagerRoom;
import com.example.licenta.NavigationDrawer.ToDoListProgress.Utils.DataBaseManager;
import com.example.licenta.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> toDoList;
    private ProgresFragment progresFragment;
    private DBManagerRoom db;

    public ToDoAdapter(DBManagerRoom db, ProgresFragment progresFragment) {
        this.db = db;
        this.progresFragment = progresFragment;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        ToDoModel item = toDoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    new Thread(() -> {
                        db.getDao().updateStatus(item.getId(), 1);
                    }).start();
                } else {
                    new Thread(() -> {
                        db.getDao().updateStatus(item.getId(), 0);
                    }).start();
                }
            }
        });
    }

    public int getItemCount() {
        return toDoList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public void setTasks(List<ToDoModel> toDoList) {
        this.toDoList = toDoList;
        //notifyDataSetChanged(); //recyclerview is updated
    }

    public Context getContext() {
        return progresFragment.getContext();
    }

    //--- MU MERGE
    public void deleteItem(int position) {
        ToDoModel item = toDoList.get(position);
//        db.getDao().deleteTask(item.getId()); //-> CRAPA de la Interfata DAO
        toDoList.remove(item);
        notifyItemRemoved(position); //automatically update the recycler view
    }

    //NU MERGE
    //Edit item- have to update it
    public void editItem(int position) {
        ToDoModel item = toDoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewtask fragment = new AddNewtask();
        fragment.setArguments(bundle);
        fragment.show(fragment.getChildFragmentManager(), AddNewtask.TAG);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
