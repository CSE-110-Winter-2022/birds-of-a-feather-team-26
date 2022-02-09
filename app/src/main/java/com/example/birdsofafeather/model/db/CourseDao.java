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
    @Transaction
    @Query("SELECT * FROM course where person_id=:personId")
    List<com.example.iteration1.model.db.Course> getForPerson(int personId);

    @Query("SELECT * FROM course WHERE id=:id")
    com.example.iteration1.model.db.Course get(int id);

    @Query("SELECT COUNT(*) from course")
    int count();

    @Insert
    void insert(com.example.iteration1.model.db.Course course);

    @Delete
    void delete(com.example.iteration1.model.db.Course course);

    @Query("SELECT * FROM course")
    LiveData<List<com.example.iteration1.model.db.Course>> getAllCourses();
}
