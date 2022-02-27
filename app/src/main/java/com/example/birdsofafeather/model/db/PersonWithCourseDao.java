package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
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

    @Query("SELECT count(*) FROM course")
    int count();

    @Query("SELECT courseSize FROM course WHERE person_id=:personId")
    String getCourseSize(int personId);
//
//    @Query("SELECT favorite FROM persons WHERE person_name=:name")
//    Boolean checkFav(String name);

}
