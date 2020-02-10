package com.yulin.myapplication.database;

import android.content.Context;

import com.yulin.myapplication.DramaBean;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DramaBean.class}, version = 1)
public abstract class ChocoDatabase extends RoomDatabase {

    private static final String DB_NAME = "Choco.db";

    public abstract DramaDao getDramaDao();

    private static volatile ChocoDatabase instance;

    public static ChocoDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (ChocoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ChocoDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }
}
