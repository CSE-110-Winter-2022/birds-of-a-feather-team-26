package com.example.birdsofafeather.model;

import java.io.Serializable;
import java.util.List;
import java.util.Calendar;

public class Session implements Serializable {
    public String name;
    public List<Student> students;

    public Session() {
        this.name = Calendar.getInstance().getTime().toString();    // session name default
    }

    public String getName() { return this.name; }

    public List<Student> getStudents() { return this.students; }

    public void setName(String name) { this.name = name; }
}
