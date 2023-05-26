package com.example.licenta.NavigationDrawer.toDoList.Model;

public class ToDoModel extends TaskId {
    //date din baza de date
    private String task , due, materie;
    private int status, dificultate;

    public String getTask() {
        return this.task;
    }

    public String getDue() {
        return this.due;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMaterie() {
        return materie;
    }

    public int getDificultate() {
        return dificultate;
    }
}
