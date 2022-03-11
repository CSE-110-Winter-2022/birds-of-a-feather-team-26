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
    Course getCourse(int courseId);

    @Transaction
    @Query("SELECT * FROM course WHERE person_id=:personId")
    List<Course> getCoursesForPerson(int personId);

    @Insert
    void insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT COUNT(*) from course")
    int count();
}
