package com.example.birdsofafeather.model.db;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.birdsofafeather.model.IPerson;

import java.util.List;

@Entity(tableName = "persons")
public class Person implements IPerson{


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int personId;

    @ColumnInfo(name="session_id")
    public int sessionId;

    @ColumnInfo(name="person_name")
    public String personName;

    @ColumnInfo(name="photo_url")
    public String url;

    @TypeConverters(Convertor.class)
    public final List<Course> courseList;

    @ColumnInfo(name="Favorite")
    public boolean fav = false;

    public Person(int personId, String personName, String url, List<Course> courseList, boolean fav){
        this.personId = personId;
      //  this.sessionId = sessionId;
        this.personName = personName;
        this.url = url;
        this.fav = fav;
        this.courseList = courseList;
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

//    @Override
//    public List<com.example.birdsofafeather.model.Course> getCourseInfo() {
//
//    }

}
