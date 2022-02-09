package com.example.birdsofafeather.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.model.IPerson;
import com.example.iteration1.model.IPerson;

public class PersonWithCourse implements IPerson {

    @Embedded
    public Person person;

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = Course.class, projection = {"year"})
    public String year;

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = Course.class, projection = {"quarter"})
    public String quarter;

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = Course.class, projection = {"courseName"})
    public String courseName;

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = Course.class, projection = {"courseNum"})
    public String courseNum;

    @Override
    public int getId() { return this.person.personId; }

    @Override
    public String getName() {
        return this.person.name;
    }

    @Override
    public String getCourseInfo() {
        String result = "";
        result = this.year+this.quarter+this.courseName+this.courseNum;
        return result;
    }
}
