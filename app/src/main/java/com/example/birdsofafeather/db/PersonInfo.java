package com.example.birdsofafeather.db;
public class PersonInfo {
    private final String name;
    private final String photo;
    public PersonInfo(String name, String photo){
        this.name=name;
        this.photo=photo;
    }
    public String getName(){
        return this.name;
    }
    public String getPhoto(){
        return this.photo;
    }
}

