package com.example.birdsofafeather.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    String firstName;
    String pictureURL;
    List<Course> courses;

    public Student(String firstName, String pictureURL, List<Course> courses) {
        this.firstName = firstName;
        this.pictureURL = pictureURL;
        this.courses = courses;
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
