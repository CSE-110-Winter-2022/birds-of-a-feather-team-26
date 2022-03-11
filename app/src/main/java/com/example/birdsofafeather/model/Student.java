package com.example.birdsofafeather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    public String firstName;
    public String pictureURL;
    public List<Course> courses;
    public Boolean fav;

    public Student(String firstName, String pictureURL, List<Course> courses, Boolean fav) {
        this.firstName = firstName;
        this.pictureURL = pictureURL;
        this.courses = courses;
        this.fav = fav;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public List<Course> getCourses() {
        return courses;
    }
}
