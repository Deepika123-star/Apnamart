package com.smartwebarts.apnamart.database;

import android.content.Context;

import androidx.room.Room;

import com.smartwebarts.apnamart.R;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    public DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase= Room.databaseBuilder(mCtx, AppDatabase.class, mCtx.getString(R.string.db)).build();
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static DatabaseClient getmInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
}
