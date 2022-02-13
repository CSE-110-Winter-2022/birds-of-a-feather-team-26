package com.example.birdsofafeather;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.example.birdsofafeather.model.db.AppDatabase.useTestSingleton;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.CourseDao;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonDao;
import com.example.birdsofafeather.model.db.PersonWithCourse;
import com.example.birdsofafeather.model.db.PersonWithCourseDao;


import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {

    private CourseDao courseDao;
    private PersonDao personDao;
    private PersonWithCourseDao personcourseDao;
    private AppDatabase db;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        useTestSingleton(context);
        db = AppDatabase.singleton(context);
        personDao = db.PersonDao();
        courseDao = db.CourseDao();
        personcourseDao=db.personWithCourseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertPerson(){
        personDao.insertPerson(new Person(8,"Huaner","JPG"));
        assertEquals(1, personDao.count());
    }

    @Test
    public void testDeletePerson(){
        Person p = new Person(8,"Huaner","JPG");
        personDao.insertPerson(p);
        personDao.deletePerson(p);
        assertEquals(0, personDao.count());
    }

    @Test
    public void testInsertDeleteCourse(){
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.delete(c);
        assertEquals(1, courseDao.count());
    }

    @Test
    public void testPersonWithCourse(){
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110");
        Course c2 = new Course(2,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        assertEquals("2022 WI CSE 110", personcourseDao.get(0));
    }

}