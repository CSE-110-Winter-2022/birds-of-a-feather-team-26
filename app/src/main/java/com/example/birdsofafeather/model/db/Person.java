package com.example.birdsofafeather.model.db;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.birdsofafeather.model.IPerson;

import java.util.List;

@Entity(tableName = "persons")
public class Person implements IPerson {

    // Person instance variables
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int personId;

    @ColumnInfo(name="person_name")
    public String personName;

    @ColumnInfo(name="photo_url")
    public String url;

    @ColumnInfo(name="Favorite")
    public boolean fav = false;

    // Session pointer
    @ColumnInfo(name="session_id")
    public int sessionId;

    public Person(String personName, String url){
        this.personName = personName;
        this.url = url;
    }

    @Override
    public int getId() {
        return personId;
    }

    @Override
    public String getName() {
        return personName;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<Course> getCourseList() {
        return courseList;
    }

}
