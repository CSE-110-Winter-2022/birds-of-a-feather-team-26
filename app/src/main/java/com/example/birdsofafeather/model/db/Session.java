package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {

    // Session instance variables
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="session_id")
    public int sessionId;

    @ColumnInfo(name="session_name")
    public String sessionName;

    public Session(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getSessionName() { return sessionName; }

    public void setName(String name) { sessionName = name; }

}
