package com.example.birdsofafeather.model.db;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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

    @ColumnInfo(name="courseSize")
    public String courseSize;

    public Course(int courseId, int personId, String year, String quarter, String courseName, String courseNum, String courseSize){
        this.courseId = courseId;
        this.personId = personId;
        this.year = year;
        this.quarter = quarter;
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.courseSize = courseSize;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        com.example.birdsofafeather.model.db.Course course = (com.example.birdsofafeather.model.db.Course) obj;
        return this.year.equals(course.year) && this.quarter.equals(course.quarter) && this.courseName.equals(course.courseName) && this.courseNum.equals(course.courseNum) && this.courseSize.equals(course.courseSize);
    }
}
