package com.example.birdsofafeather;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.example.birdsofafeather.model.db.AppDatabase.useTestSingleton;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.CourseDao;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonDao;


import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {

    private CourseDao courseDao;
    private PersonDao personDao;
    private PersonWithCourseDao personWithCourseDao;
    private AppDatabase db;
    private List<Person> personList;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        useTestSingleton(context);
        db = AppDatabase.singleton(context);
        personDao = db.PersonDao();
        courseDao = db.CourseDao();
        personWithCourseDao=db.personWithCourseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
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
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110", "Huge");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.delete(c);
        assertEquals(1, courseDao.count());
    }

    @Test
    public void testPersonWithCourse(){
        Person p = new Person(8,"Huaner","JPG");
        Person p1 = new Person(2,"chenhan","thisisthelink");
        Person p2 = new Person(3,"zehua","alinkagain");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110", "Large");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        Course c2 = new Course(2,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120", "Huge");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        PersonWithCourse n = personWithCourseDao.get(8);
        assertEquals(8, n.getId());
    }

    @Test
    public void testPersonWithCourse1(){
        Person p = new Person(8,"Huaner","JPG");
        Person p1 = new Person(2,"chenhan","thisisthelink");
        Person p2 = new Person(3,"zehua","alinkagain");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110", "Large");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        Course c2 = new Course(2,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120", "Large");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        PersonWithCourse n = personWithCourseDao.get(3);
        assertEquals("zehua",n.getName());
    }
    @Test
    public void testPersonWithCourse2(){
        Person p = new Person(8,"Huaner","JPG");
        Person p1 = new Person(2,"chenhan","thisisthelink");
        Person p2 = new Person(3,"zehua","alinkagain");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
//        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110");
//        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110");
//        Course c2 = new Course(2,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120");
//        courseDao.insert(c);
//        courseDao.insert(c1);
//        courseDao.insert(c2);
        PersonWithCourse n = personWithCourseDao.get(2);
        assertEquals("thisisthelink",n.getUrl());
    }

    @Test
    public void testPersonWithCourse3(){
        Person p = new Person(8,"Huaner","JPG");
        Person p1 = new Person(2,"chenhan","thisisthelink");
        Person p2 = new Person(3,"zehua","alinkagain");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        Course c = new Course(0,8,"Huaner","JPG","2022","WI","CSE","110", "Large");
        Course c1  = new Course(1,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        Course c2 = new Course(2,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120","Large");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        PersonWithCourse n = personWithCourseDao.get(8);
        assertEquals("2022WICSE110 Large",n.getCourseInfo());
    }

    @Test
    public void testPersonWithCourse4(){
        Person p = new Person(1,"Huaner","JPG");
        Person p1 = new Person(2,"chenhan","thisisthelink");
        Person p2 = new Person(3,"zehua","alinkagain");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        Course c = new Course(4,1,"Huaner","JPG","2022","WI","CSE","110", "Large");
        Course c1  = new Course(5,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        Course c2 = new Course(6,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120", "Large");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        assertEquals(3, personWithCourseDao.count());
    }

    @Test
    public void testCourseSize(){
        Course c = new Course(4,1,"Huaner","JPG","2022","WI","CSE","110", "Huge");
        Course c1  = new Course(5,2,"chenhan", "thisisthelink", "2021", "FA", "CSE", "110", "Large");
        Course c2 = new Course(6,3, "zehua", "alinkagain", "2022", "WI", "CSE", "120", "Gigantic");
        courseDao.insert(c);
        courseDao.insert(c1);
        courseDao.insert(c2);
        assertEquals("Gigantic", courseDao.getCourseSize(3));
    }

}