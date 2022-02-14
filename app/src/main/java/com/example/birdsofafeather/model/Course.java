package com.example.birdsofafeather.model;

public class Course {
    String year;
    String quarter;
    String subject;
    String courseNumber;

    public Course(String year, String quarter, String subject, String courseNumber) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNumber = courseNumber;
    }

    /**
     * getter methods
     */
    public String getCourse() {
        return quarter + " " + year + " " + subject + " " + courseNumber;
    }
}
