package com.example.birdsofafeather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    String firstName;
    String pictureURL;
    List<Course> courses;
    Boolean fav;

    public Student(Student s) {
        firstName = s.getFirstName();
        pictureURL = s.getPictureURL();
        courses = s.getCourses();
    }
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
    public boolean fav() { return fav = true; }
}
