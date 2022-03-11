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
import com.example.birdsofafeather.model.db.Session;
import com.example.birdsofafeather.model.db.SessionDao;


import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {

    private CourseDao courseDao;
    private PersonDao personDao;
    private SessionDao sessionDao;
    private AppDatabase db;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        useTestSingleton(context);
        db = AppDatabase.singleton(context);
        personDao = db.PersonDao();
        courseDao = db.CourseDao();
        sessionDao = db.SessionDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertPerson(){
        Person p = new Person(8,"Huaner","JPG");
        personDao.insertPerson(p);
        assertEquals(1, personDao.count());
    }

    @Test
    public void testInsertCourse(){
        Course c = new Course(0, "2022", "Fall","CSE", "110", "large");
        Course c1  = new Course(1, "2022", "Fall","CSE", "120", "large");
        courseDao.insertCourse(c);
        courseDao.insertCourse(c1);
        assertEquals(2, courseDao.count());
    }

    @Test
    public void testFavPerson(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        p.fav = true;
        assertEquals(p.isFav(), true);
    }

    @Test
    public void testNamePerson(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        assertEquals("ivy", p.personName);
    }

    @Test
    public void testgetAllFavPerson(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        Person p1 = new Person(2, "huaner", "linkofthephoto");
        Person p2 = new Person(3, "zehua", "linkofthephoto");
        p2.fav = true;
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        int size = personDao.getAllFav().size();
        assertEquals(1, size);
    }

    @Test
    public void testgetPersonForSession(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        Person p1 = new Person(2, "huaner", "linkofthephoto");
        Person p2 = new Person(3, "zehua", "linkofthephoto");
        Person p3 = new Person(3, "zehua1", "linkofthephoto1");
        Person p4 = new Person(3, "zehua2", "linkofthephoto2");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        personDao.insertPerson(p3);
        personDao.insertPerson(p4);
        assertEquals(personDao.getPersonsForSession(3).size(), 3);
    }

    @Test
    public void testgetPersons(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        Person p1 = new Person(2, "huaner", "linkofthephoto");
        Person p2 = new Person(3, "zehua", "linkofthephoto");
        Person p3 = new Person(3, "zehua1", "linkofthephoto1");
        Person p4 = new Person(3, "zehua2", "linkofthephoto2");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        personDao.insertPerson(p3);
        personDao.insertPerson(p4);
        assertEquals(5, personDao.getAllPersons().size());
    }

    @Test
    public void testgetCourseForPerson(){
        Person p = new Person(1, "ivy", "linkofthephoto");
        p.personId = 2;
        Course c = new Course(2, "2022", "Fall", "CSE", "110", "large");
        courseDao.insertCourse(c);
        Person p1 = new Person(2, "huaner", "linkofthephoto");
        Person p2 = new Person(3, "zehua", "linkofthephoto");
        Person p3 = new Person(3, "zehua1", "linkofthephoto1");
        Person p4 = new Person(3, "zehua2", "linkofthephoto2");
        personDao.insertPerson(p);
        personDao.insertPerson(p1);
        personDao.insertPerson(p2);
        personDao.insertPerson(p3);
        personDao.insertPerson(p4);
        assertEquals(1, courseDao.getCoursesForPerson(2).size());
    }

    @Test
    public void testSessionName(){
        Session session1 = new Session("CSE110");
        sessionDao.insertSession(session1);
        assertEquals("CSE110", session1.sessionName);
    }

    @Test
    public void testAllSessions(){
        Session session1 = new Session("CSE110");
        Session session2 = new Session("CSE120");
        Session session3 = new Session("CSE130");
        sessionDao.insertSession(session1);
        sessionDao.insertSession(session2);
        sessionDao.insertSession(session3);
        assertEquals(3, sessionDao.getAllSessions().size());
    }

    @Test
    public void testgetSessionName(){
        Session session1 = new Session("CSE110");
        session1.sessionId = 1;
        Session session2 = new Session("CSE120");
        sessionDao.insertSession(session1);
        sessionDao.insertSession(session2);
        assertEquals("CSE110", sessionDao.getSessionName(1));
    }

    @Test
    public void testSetSessionName(){
        Session session1 = new Session("CSE110");
        session1.sessionId = 1;
        Session session2 = new Session("CSE120");
        session2.sessionId = 2;
        sessionDao.insertSession(session1);
        sessionDao.insertSession(session2);
        sessionDao.setSessionName(1,"CSE111");
        assertEquals("CSE111", sessionDao.getSessionName(1));
    }




}