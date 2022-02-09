package com.example.birdsofafeather.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class, com.example.iteration1.model.db.Course.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract com.example.iteration1.model.db.PersonDao PersonDao();
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
                   if(singletonInstance==null){
                       singletonInstance= Room.databaseBuilder(context,AppDatabase.class,"persons.db")
                       .allowMainThreadQueries().build();
                   }
                   return singletonInstance;
    }

    public abstract com.example.iteration1.model.db.PersonWithCourseDao personWithCourseDao();

    public abstract CourseDao CourseDao();
}
