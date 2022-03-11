package com.example.birdsofafeather.model.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons")
public class Person {

    // Person instance variables
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int personId;

    @ColumnInfo(name="person_name")
    public String personName;

    @ColumnInfo(name="photo_url")
    public String url;

    @ColumnInfo(name="Favorite")
    public boolean fav = false;

    // Session pointer
    @ColumnInfo(name="session_id")
    public int sessionId;

    public Person(int sessionId, String personName, String url){
        this.sessionId = sessionId;
        this.personName = personName;
        this.url = url;
    }

    // Person getter methods
    public int getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFav() {
        return fav;
    }

    // Session getter methods
    public int getSessionId() {
        return sessionId;
    }
}
