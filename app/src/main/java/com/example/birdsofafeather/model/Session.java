package com.example.birdsofafeather.model;

import java.io.Serializable;
import java.util.List;
import java.util.Calendar;

public class Session implements Serializable {
    String name;
    List<Student> students;

    public Session(List<Student> students) {
        this.name = Calendar.getInstance().getTime().toString();    // session name default
        this.students = students;
    }

    public Session(String name, List<Student> students) {
        this.name = name;
        this.students = students;
    }

    public String getName() { return this.name; }

    public List<Student> getStudents() { return this.students; }

    public void setName(String name) { this.name = name; }
}
