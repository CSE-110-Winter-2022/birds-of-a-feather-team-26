package com.example.birdsofafeather;

import static com.example.birdsofafeather.model.db.AppDatabase.useTestSingleton;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.CourseDao;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonDao;
import com.example.birdsofafeather.model.db.PersonWithCourseDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest1 {

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
    public void testInsertCourse(){
        courseDao.insert(new Course(0,8,"Huaner","JPG","2022","WI","CSE","110"));
        assertEquals(1, courseDao.count());
    }
//
//    @Test
//    public void test2(){
//        personDao.insertPerson(new Person(1,"Chenhan","JPG"));
//        Course cc=new Course(0,1,"Chenhan","JPG","2022","Winter","CSE","110");
//        courseDao.insert(cc);
//        courseDao.delete(cc);
//        assertEquals(0, courseDao.count());
//
//    }

}