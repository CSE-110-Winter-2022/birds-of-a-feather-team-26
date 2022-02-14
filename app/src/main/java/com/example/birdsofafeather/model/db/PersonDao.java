package com.example.birdsofafeather.model.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persons")
    List<Person> getAllPersons();

    @Insert
    void insertPerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT COUNT(*) FROM persons")
    int count();
}
