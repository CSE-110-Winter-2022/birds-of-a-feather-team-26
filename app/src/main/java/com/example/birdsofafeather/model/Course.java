package com.example.birdsofafeather.model;

import androidx.annotation.Nullable;

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

//    /**
//     * getter methods
//     */
//    public String getCourse() {
//        return quarter + " " + year + " " + subject + " " + courseNumber;
//    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Course course = (Course) obj;
        return this.year.equals(course.year) && this.quarter.equals(course.quarter) && this.subject.equals(course.subject) && this.courseNumber.equals(course.courseNumber);
    }
}
