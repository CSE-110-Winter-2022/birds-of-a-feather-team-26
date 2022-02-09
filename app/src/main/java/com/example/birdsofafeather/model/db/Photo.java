package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="photo")
public class Photo {
    @PrimaryKey
    @ColumnInfo(name="id")
    public int photoId;
    @ColumnInfo(name="person_id")
    public int personId;
    @ColumnInfo(name="text")
    public String photo;
    public Photo(int photoId,int personId,String photo){
        this.photoId=photoId;
        this.personId=personId;
        this.photo=photo;

    }
}

