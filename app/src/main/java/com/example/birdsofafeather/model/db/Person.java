package com.example.birdsofafeather.model.db;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons")
public class Person{

    @PrimaryKey
    @ColumnInfo(name="id")
    public int personId;

    @ColumnInfo(name="person_name")
    public String personName;

    @ColumnInfo(name="photo_url")
    public String url;

    @ColumnInfo(name="Favorite")
    public boolean fav = false;

    public Person(int personId, String personName, String url, boolean fav){
        this.personId = personId;
        this.personName = personName;
        this.url = url;
        this.fav = fav;
    }

}
