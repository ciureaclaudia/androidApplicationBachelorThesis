package com.example.licenta.NavigationDrawer.ToDoListProgress.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB.ToDoDao;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "TabelItemDate")
public class ToDoModel {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "status")
    public int status;

    @ColumnInfo(name = "task")
    public String task;

    public ToDoModel() {

    }

    @Ignore
    public ToDoModel(int id, int status, String task) {
        this.id = id;
        this.status = status;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
