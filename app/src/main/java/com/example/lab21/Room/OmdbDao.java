package com.example.lab21.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lab21.Record;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface OmdbDao {
    @Query("select * from Record where title like :title")
    Single<List<Record>> getByTitle(String title);

    @Query("select * from record")
    Single<List<Record>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Record record);
}
