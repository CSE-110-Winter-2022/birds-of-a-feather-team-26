package com.example.birdsofafeather.model;
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

