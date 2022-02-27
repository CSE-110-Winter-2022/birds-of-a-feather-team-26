package com.example.birdsofafeather.model.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persons")
    List<Person> getAllPersons();

    @Query("SELECT * FROM persons WHERE id=:id")
    Person get(int id);

    @Insert
    void insertPerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT COUNT(*) FROM persons")
    int count();

    @Query("SELECT favorite FROM persons where person_name=:name")
    boolean checkFav(String name);

}
