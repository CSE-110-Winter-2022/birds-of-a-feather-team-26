package com.example.birdsofafeather.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM course WHERE id=:courseId")
    Course get(int courseId);

    @Query("SELECT COUNT(*) from course")
    int count();

    @Query("SELECT courseSize FROM course WHERE person_id=:personId")
    String getCourseSize(int personId);

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course WHERE person_id=:personId")
    List<Course> getAllCourses(int personId);
}
