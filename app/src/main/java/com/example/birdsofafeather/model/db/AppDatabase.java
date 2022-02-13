package com.example.birdsofafeather.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class, Course.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PersonDao PersonDao();
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
                   if(singletonInstance==null){
                       singletonInstance= Room.databaseBuilder(context,AppDatabase.class,"persons.db")
                       .allowMainThreadQueries().build();
                   }
                   return singletonInstance;
    }

    public static void useTestSingleton(Context context) {
        singletonInstance = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public abstract PersonWithCourseDao personWithCourseDao();

    public abstract CourseDao CourseDao();
}
