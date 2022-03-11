package com.example.birdsofafeather.model.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persons WHERE id=:id")
    Person getPerson(int id);

    @Query("SELECT * FROM persons WHERE session_id=:sessionId")
    List<Person> getPersonsForSession(int sessionId);

    @Query("SELECT * FROM persons")
    List<Person> getAllPersons();

    @Query("SELECT * FROM persons where Favorite")
    List<Person> getAllFav();

    @Insert
    void insertPerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT COUNT(*) FROM persons")
    int count();

}
