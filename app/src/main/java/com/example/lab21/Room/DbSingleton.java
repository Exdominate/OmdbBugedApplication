package com.example.lab21.Room;

import android.content.Context;

import androidx.room.Room;

public class DbSingleton {
    private AppDb moviesDb;
    private static DbSingleton instance;

    private DbSingleton(Context context){
        moviesDb = Room.databaseBuilder(context, AppDb.class, "MoviesDb")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static DbSingleton getInstance(Context context){
        if (instance==null){
            instance = new DbSingleton(context);
        }
        return instance;
    }

    public AppDb getAppDb(){
        return this.moviesDb;
    }
}
