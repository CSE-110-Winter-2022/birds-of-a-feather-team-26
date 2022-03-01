package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "sessions")
public class Session {

    @PrimaryKey
    @ColumnInfo(name="session_name")
    public String sessionName;

    @ColumnInfo(name="person_id")
    public int personId;


    public Session(String sessionName){
        this.sessionName = sessionName;
    }

}
