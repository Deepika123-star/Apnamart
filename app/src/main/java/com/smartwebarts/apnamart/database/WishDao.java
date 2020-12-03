package com.smartwebarts.apnamart.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WishDao {
    @Query("SELECT * FROM WishItem")
    List<WishItem> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishItem task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<WishItem> task);

    @Delete
    void delete(WishItem task);

    @Update
    void update(WishItem task);

    @Query("DELETE FROM WishItem")
    void deleteAll();

//    @Query("DELETE FROM Classs WHERE class_name = :userId")
//    void deleteByUserId(long userId);
}

