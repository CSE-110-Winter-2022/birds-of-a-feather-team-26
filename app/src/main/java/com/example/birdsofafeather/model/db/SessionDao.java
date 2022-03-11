package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM sessions")
    List<Session> getAllSessions();

    @Query("SELECT session_name FROM sessions WHERE session_id=:sessionId")
    String getSessionName(int sessionId);

    @Query("SELECT session_id FROM sessions WHERE session_name=:sessionName")
    int getSessionIDFromName(String sessionName);

    @Insert
    void insertSession(Session session);

    @Delete
    void deleteSession(Session session);

    @Transaction
    @Query("UPDATE sessions SET session_name=:newSessionName WHERE session_id=:sessionId")
    String setSessionName(int sessionId, String newSessionName);
}