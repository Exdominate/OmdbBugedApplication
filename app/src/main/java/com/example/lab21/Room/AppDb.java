package com.example.lab21.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lab21.Record;

@Database(entities = {Record.class}, version = 2, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    public abstract OmdbDao omdbDao();
}
