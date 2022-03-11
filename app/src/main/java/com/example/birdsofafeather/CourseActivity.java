package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Person;

import java.util.List;

/**
 * DESCRIPTION
 *
 * The course activity here asks the user to add his/her course he/she has taken. The format is like
 * the year, quarter, the course name and the course number. Once the user clicked the enter, the
 * name of the course will appear on the screen, like a list of courses. If you hit the exit button,
 * then the user will be directed to the home page for the next potential moves.
 */

public class CourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private Person person;

    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseViewAdapter courseViewAdapter;

    /**
     * This method creates the Course Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // Pull previous Intent data
        Intent intent = getIntent();
        // String name = intent.getStringExtra("person_name");
        int personId = intent.getIntExtra("person_id", 0);

        // Connect to BOF database
        db = AppDatabase.singleton(this);

        // Retrieve person object and course information of user from BOF database
        person = db.PersonDao().getPerson(personId);
        List<Course> course = db.CourseDao().getCoursesForPerson(personId);

        // Set up Recycler View for courses
        courseRecyclerView = findViewById(R.id.courses_view);
        courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);

        // Delete course functionality
        courseViewAdapter = new CourseViewAdapter(course, (Course) -> {
            db.CourseDao().deleteCourse(Course);
        });
        courseRecyclerView.setAdapter(courseViewAdapter);

    }

    /**
     * Finish User Information Workflow and return back to home when FINISH button is clicked
     */
    public void onExitClicked(View view){
         Intent intent = new Intent(this, HomeActivity.class);
         startActivity(intent);
    }

    /**
     * Adds typed-in course information to user profile and stores in BOF database
     */
    public void onEnterCourseClicked(View view) {
        // Index new course_id
        int newCourseId = db.CourseDao().count();

        // Retrieve user's id
        int personId = person.personId;

        // Fetches year of course typed in by user
        TextView yearView = findViewById(R.id.year_view);
        String newYearText = yearView.getText().toString();

        // Fetches quarter of course typed in by user
        TextView quarterView = findViewById(R.id.quarter_view);
        String newQuarterText = quarterView.getText().toString();

        // Fetches name of course typed in by user
        TextView courseNameView = findViewById(R.id.courseName_view);
        String newCourseNameText = courseNameView.getText().toString();

        // Fetches number of course typed in by user
        TextView courseNumView = findViewById(R.id.courseNum_view);
        String newCourseNumText = courseNumView.getText().toString();

        // Fetches the size of the course typed in by user
        TextView courseSizeView = findViewById(R.id.courseSize_view);
        String newCourseSizeText = courseSizeView.getText().toString();

        // Raise alerts if the information is incomplete
        if(newYearText.equals("") || newQuarterText.equals("") || newCourseNameText.equals("") || newCourseNumText.equals("") || newCourseSizeText.equals("")) {
            Utilities.showAlert(CourseActivity.this,"Please enter the full course information please");
            return;
        }

        // Create course object from course information
        Course newCourse = new Course(personId, newYearText, newQuarterText, newCourseNameText, newCourseNumText, newCourseSizeText);

        // Store course information into BOF database
        db.CourseDao().insertCourse(newCourse);

        courseViewAdapter.addCourse(newCourse); // Add course for display on Course Activity screen
    }
}