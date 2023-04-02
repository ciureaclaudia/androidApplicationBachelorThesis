package com.example.licenta.NavigationDrawer.ToDoListProgress;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.licenta.NavigationDrawer.ToDoListProgress.DoToAdapter.ToDoAdapter;
import com.example.licenta.NavigationDrawer.ToDoListProgress.Model.ToDoModel;
import com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB.DBManagerRoom;
import com.example.licenta.NavigationDrawer.ToDoListProgress.Utils.DataBaseManager;
import com.example.licenta.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProgresFragment extends Fragment implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;

    private List<ToDoModel> taskList;
    //    private DataBaseMnager db;
    DBManagerRoom db;

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progres, container, false);

//       view.getSupportActionBar().hide();
//        db=new DataBaseManager(getContext());
//        db.openDatabase();

//        db = DBManagerRoom.getInstance(getContext());
        db=Room.databaseBuilder(getContext(), DBManagerRoom.class, "TabelItemDate").build();
        taskList = new ArrayList<ToDoModel>();

        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //INITILZIZARE ADAPTER
        tasksAdapter = new ToDoAdapter(db, ProgresFragment.this);


        //SWIPE STANGA SAU DREAPTA
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = view.findViewById(R.id.fab_TodoList);

        new Thread(() -> {
//            DBManagerRoom db = Room.databaseBuilder(getContext(), DBManagerRoom.class, "TabelItemDate").build();
            taskList = db.getDao().getAlltasks();
            Collections.reverse(taskList);
            getActivity().runOnUiThread(()->{
                tasksAdapter = new ToDoAdapter(db, ProgresFragment.this);
                tasksRecyclerView.setAdapter(tasksAdapter);
                tasksAdapter.setTasks(taskList);
                tasksAdapter.notifyDataSetChanged();
            });
        }).start();

//        taskList=db.getAllTasks();

        //se deschide un nou fragment
        fab.setOnClickListener(view1 -> {
            AddNewtask.newInstance().show(getChildFragmentManager(), AddNewtask.TAG);
            System.out.println("AI APASAT PE ADAUGA TASK");
        });

        return view;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        new Thread(() -> {
            taskList = db.getDao().getAlltasks();
            Collections.reverse(taskList);
            getActivity().runOnUiThread(()->{
                tasksAdapter = new ToDoAdapter(db, ProgresFragment.this);
               tasksRecyclerView.setAdapter(tasksAdapter);
                tasksAdapter.setTasks(taskList);
                tasksAdapter.notifyDataSetChanged();
            });
        }).start();

    }
}