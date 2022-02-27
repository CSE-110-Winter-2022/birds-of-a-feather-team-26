package com.example.birdsofafeather.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.model.IPerson;


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

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = Course.class, projection = {"courseSize"})
    public String courseSize;

    @Override
    public String getUrl() { return this.person.url; }

    @Override
    public int getId() { return this.person.personId; }

    @Override
    public String getName() { return this.person.personName;}

//    @Override
//    public Boolean isFavOrNot(){ return this.person.fav;}

    @Override
    public String getCourseInfo() {
        String result = "";
        result = this.year+this.quarter+this.courseName+this.courseNum+" "+this.courseSize;
        return result;
    }
}
