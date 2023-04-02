package com.example.licenta.NavigationDrawer.ToDoListProgress.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.licenta.NavigationDrawer.ToDoListProgress.Model.ToDoModel;

@Database(entities = {ToDoModel.class}, version = 1,exportSchema = false)  //creez tabela
public abstract class DBManagerRoom extends RoomDatabase {

    public static DBManagerRoom instanta;

    //proiectez baza de date
    public static synchronized DBManagerRoom getInstance(Context context){
        if(instanta==null){
            instanta= Room.databaseBuilder(context, DBManagerRoom.class, "RommBD")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instanta;
    }

    //acces la operatii
    public abstract ToDoDao getDao();
}
