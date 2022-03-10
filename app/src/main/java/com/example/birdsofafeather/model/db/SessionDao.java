package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM sessions")
    List<Session> getAllSessions();

    @Query("SELECT session_name FROM sessions WHERE person_id=:sessionId")
    String getSessionName(int sessionId);

    @Insert
    void insertSession(Session session);

    @Delete
    void deleteSession(Session session);

    @Query("SELECT favorite FROM persons WHERE id=:personId")
    boolean isFav(int personId);

}