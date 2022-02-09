package com.example.birdsofafeather.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PersonWithCourseDao {

    @Transaction
    @Query("SELECT * FROM persons")
    List<PersonWithCourse> getAll();

    @Transaction
    @Query("SELECT * FROM persons WHERE id=:id")
    PersonWithCourse get(int id);

    @Query("SELECT * FROM course")
    int count();

}
