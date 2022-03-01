package com.example.birdsofafeather.model;


import com.example.birdsofafeather.model.db.Person;

import java.util.List;

public interface IPerson {

    public abstract int getId();
    public abstract String getName();
    public abstract String getUrl();
    //public abstract Boolean isFavOrNot();
    public abstract String getCourseInfo();
}
