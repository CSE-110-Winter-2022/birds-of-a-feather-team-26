package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "sessions")
public class Session {

    @Embedded
    Person person;

    @Embedded
    PersonDao personDao;

    @PrimaryKey
    @ColumnInfo(name="session_id")
    public int sessionId;

    @ColumnInfo(name="session_name")
    public String sessionName;


    public Session(List<Person> persons){
        List<Person> list = personDao.getAllPersons();
        list = persons;
    }
    public Session(String sessionName, List<Person> persons){
        this.sessionName = sessionName;
        List<Person> list = personDao.getAllPersons();
        list = persons;
    }

    public String getSessionName() { return this.sessionName; }

    public List<Person> getStudents() { return this.personDao.getAllPersons(); }

    public void setName(String name) { this.sessionName = name; }

}
