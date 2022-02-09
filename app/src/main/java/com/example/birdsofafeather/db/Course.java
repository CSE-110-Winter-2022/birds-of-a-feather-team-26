package com.example.birdsofafeather.db;
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

    @ColumnInfo(name="year")
    public String year;

    @ColumnInfo(name="quarter")
    public String quarter;

    @ColumnInfo(name="courseName")
    public String courseName;

    @ColumnInfo(name="courseNum")
    public String courseNum;

    public Course(int courseId, int personId, String year, String quarter, String courseName, String courseNum){
        this.courseId = courseId;
        this.personId = personId;
        this.year = year;
        this.quarter = quarter;
        this.courseName = courseName;
        this.courseNum = courseNum;
    }

}
