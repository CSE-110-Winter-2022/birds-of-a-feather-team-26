package com.example.birdsofafeather.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Course implements Serializable {
    public String year;
    public String quarter;
    public String subject;
    public String courseNumber;
    public String courseSize;

    public Course(String year, String quarter, String subject, String courseNumber, String courseSize) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.courseSize = courseSize;
    }

    /**
     * getter methods
     */
    public String getCourse() {
        return quarter + " " + year + " " + subject + " " + courseNumber + " " + courseSize;
    }

    public String getYear() {
        return year;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getCourseSize() {
        return courseSize;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Course course = (Course) obj;
        return this.year.equals(course.year) && this.quarter.equals(course.quarter) && this.subject.equals(course.subject) && this.courseNumber.equals(course.courseNumber) && this.courseSize.equals(course.courseSize);
    }
}
