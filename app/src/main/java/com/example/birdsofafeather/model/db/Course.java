package com.example.birdsofafeather.model.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="course")
public class Course {

    @PrimaryKey
    @ColumnInfo(name="id")
    public int courseId;

    @ColumnInfo(name="person_id")
    public int personId;

    @ColumnInfo(name="person_name")
    public String personName;

    @ColumnInfo(name="photo_url")
    public String url;

    @ColumnInfo(name="year")
    public String year;

    @ColumnInfo(name="quarter")
    public String quarter;

    @ColumnInfo(name="courseName")
    public String courseName;

    @ColumnInfo(name="courseNum")
    public String courseNum;

    @ColumnInfo(name="courseSize")
    public String courseSize;

    public Course(int courseId, int personId, String personName, String url, String year, String quarter, String courseName, String courseNum, String courseSize){
        this.courseId = courseId;
        this.personId = personId;
        this.personName = personName;
        this.url = url;
        this.year = year;
        this.quarter = quarter;
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.courseSize = courseSize;
    }

}
