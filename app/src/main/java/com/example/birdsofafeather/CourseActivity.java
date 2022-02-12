package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;

import java.util.List;


public class CourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private IPerson person;

    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseViewAdapter courseViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        //String name = intent.getStringExtra("person_name");
        int personId = intent.getIntExtra("person_id", 0);

        db = AppDatabase.singleton(this);
        person = db.personWithCourseDao().get(personId);
        List<Course> course = db.CourseDao().getForPerson(personId);

        courseRecyclerView = findViewById(R.id.courses_view);
        courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);

        courseViewAdapter = new CourseViewAdapter(course, (Course) -> {
            db.CourseDao().delete(Course);
        });
        courseRecyclerView.setAdapter(courseViewAdapter);

    }

    public void onGoBackClicked(View view){
        finish();
    }

    public void onEnterCourseClicked(View view) {
        int newCourseId = db.CourseDao().count() + 1;
        int personId = person.getId();
        String name = person.getName();
        String url = person.getUrl();

        TextView yearView = findViewById(R.id.year_view);
        String newYearText = yearView.getText().toString();

        TextView quarterView = findViewById(R.id.quarter_view);
        String newQuarterText = quarterView.getText().toString();

        TextView courseNameView = findViewById(R.id.courseName_view);
        String newCourseNameText = courseNameView.getText().toString();

        TextView courseNumView = findViewById(R.id.courseNum_view);
        String newCourseNumText = courseNumView.getText().toString();

        Course newCourse = new Course(newCourseId, personId, name, url, newYearText, newQuarterText, newCourseNameText, newCourseNumText);
        db.CourseDao().insert(newCourse);

        courseViewAdapter.addCourse(newCourse);
    }

    public void onEditClicked(View view) {
    }
}