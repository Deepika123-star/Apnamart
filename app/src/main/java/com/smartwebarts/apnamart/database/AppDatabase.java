package com.smartwebarts.apnamart.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class, WishItem.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract WishDao wishDao();
}
