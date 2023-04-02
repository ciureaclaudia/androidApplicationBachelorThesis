package com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.licenta.NavigationDrawer.ToDoListProgress.Model.ToDoModel;

import java.util.List;

@Dao
public abstract class ToDoDao {


    @Query("Select * from TabelItemDate")
    public abstract List<ToDoModel> getAlltasks();

    @Insert
    public abstract long insertTask(ToDoModel task);

    @Query("UPDATE TabelItemDate SET status=:statusTask WHERE id=:idTask")
    public abstract void updateStatus(int idTask, int statusTask);

    @Query("UPDATE TabelItemDate SET task=:newTask WHERE id=:idTask  ")
    public abstract void updateTask(int idTask, String newTask);

    //NU MERGE
//    @Delete
//    public abstract void deleteTask(int id);

}

