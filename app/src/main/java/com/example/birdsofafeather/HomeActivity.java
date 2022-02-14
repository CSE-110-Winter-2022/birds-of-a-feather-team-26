package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.Student;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;


import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIPTION
 * The Home Activity module is the Birds of a Feather homepage and takes care of the application's main functionality.
 * Displays a list of all students who have taken a course in common with the user when bluetooth search is started.
 *
 * WORKFLOW
 * Home Activity workflow is as follows:
 *
 * A. Bluetooth Search Functionality
 *      - Triggered by the START SEARCH Toggle Button
 *      - Connect to all Bluetooth devices within range and try to fetch other BOF user's data
 *      - Person objects
 *
 * B. Filter Students with Common Courses (main Home Activity algorithm)
 *      - Person myPerson = new Person("Rick", "rick.png", ...);
 *
 *      personsInCommon = []
 *      for person in Persons:
 *          if (myPerson.course == person.course)
 *              personsInCommon.append(person)
 *
 *      return personsInCommon
 *
 *      - return Person(s) (List<Person>) that have taken a course in common with myPerson (myUser)
 *
 * C. Display list of Students with Common Courses
 *      - Adapter
 *      - onClick
 *          - Intent.putExtra(Person.getName())
 *          - startActvity(ProfileActivity)
 *
 **/

public class HomeActivity extends AppCompatActivity {

    private AppDatabase db;
    private Student myUser;

    /**
     * This method creates the Home Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Student object of my user
        myUser = getPersonFromDBAndReturnStudent(0);

        /**
         * A. TOGGLE BUTTON WHICH TRIGGERS BLUETOOTH FUNCTIONALITY
         */
        ToggleButton startSearch = (ToggleButton) findViewById(R.id.start_search_btn);
        startSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * This method will execute the bluetooth search functionality when toggled on.
             *
             * @param buttonView
             * @param isChecked boolean which represents state of startSearch ToggleButton
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    showMessage("Start search toggle is on");
                }

                else {
                    // The toggle is disabled
                    showMessage("Start search toggle is off");
                }

            }

        });

        /**
         * B. Filter Students with Common Courses (main Home Activity algorithm)
         *
         */



        /**
         * C. Display list of Students with Common Courses
         */

    }

    /**
     * Helper method for getting a person from BOF database with id and transforming Person data to Student object
     * @param id of person we want to get from database
     * @return Student object of queried Person
     */
    public Student getPersonFromDBAndReturnStudent(int id) {

        // Connect to BOF database
        db = AppDatabase.singleton(this);

        // Retrieve my user's information from BOF database
        IPerson myPerson = db.personWithCourseDao().get(id);
        List<com.example.birdsofafeather.model.db.Course> myCoursesRaw = db.CourseDao().getForPerson(id);

        // Convert List<db.Course> to List<Course>
        List<Course> myCourses = new ArrayList<>();
        for (com.example.birdsofafeather.model.db.Course course : myCoursesRaw) {
            Course c = new Course(course.year, course.quarter, course.courseName, course.courseNum);
            myCourses.add(c);
        }

        return new Student(myPerson.getName(), myPerson.getUrl(), myCourses);
    }

    public void listenAndFetchOtherStudentsData() {}

    public List<Student> transformStudentData() { return null; }

    /**
     * B. Filter Students with Common Courses (main Home Activity algorithm)
     * @param allStudents
     * @return
     */
    public List<Student> filterStudentsWithCommonCourses(List<Student> allStudents) {
        List<Student> filteredStudents = new ArrayList<>();

        // O(n^3) comparison as main algorithm
        for (Student student : allStudents)
            for (Course course : student.getCourses())
                for (Course c : myUser.getCourses())
                    if (course.equals(c))
                        filteredStudents.add(student);

        return filteredStudents;
    }

    public void stopListenAndFetchOtherStudentsData() {}

    /**
     * temporary button to switch to Profile Activity
     * @param view
     */
    public void onProfileActivityClicked(View view) {
        Intent intentProfileActivityWorkflow = new Intent(this, ProfileActivity.class);
        startActivity(intentProfileActivityWorkflow);
    }

    /**
     * Helper method to show messages for testing
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}