package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {

    // Session instance variables
    @PrimaryKey
    @ColumnInfo(name="session_id")
    public int sessionId;

    @ColumnInfo(name="session_name")
    public String sessionName;

    /**
     * Session (db) constructor
     * @param s Session object (model)
     */
    public Session(com.example.birdsofafeather.model.Session s) {
        this.sessionName = s.getName();
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getSessionName() { return sessionName; }

    public void setName(String name) { sessionName = name; }

}
