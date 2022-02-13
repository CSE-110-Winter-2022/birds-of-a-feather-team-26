package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.Student;

import java.util.ArrayList;

/**
 * DESCRIPTION
 * The Profile Activity module allows displays a user's name, picture, and prior course information
 *
 * WORKFLOW
 * The Profile Activity workflow is as follows:
 *
 * (Derek, Vishvesh, and Zehua please fill this out)
 * (Refer to Main Activity for a template on commenting)
 * (its like pseudocode)
 *
 **/

public class ProfileActivity extends AppCompatActivity {

    TextView studentName;
    ImageView avatar;
    RecyclerView courseList;

    /**
     * This method creates the Profile Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set pointers to main attributes of Profile Activity page
        studentName = findViewById(R.id.profile_name);
        avatar = findViewById(R.id.avatar);
        courseList = findViewById(R.id.profile_course_list);

        /**
         * Student data to display
         *
         * A. My user's profile
         *      - Information taken from BOF database
         *
         * B. Another user's profile
         *      - Information retrieved from Bluetooth transmissions
         *      - Information stored in SharedPreferences in Home Activity, must be extracted in Profile Activity
         */
        //Student student = getIntent()....
        ArrayList<Course> cList = new ArrayList<>();
        cList.add(new Course("2022", "Winter", "CSE", "110"));
        cList.add(new Course("2023", "Spring", "ECE", "120"));
        cList.add(new Course("2024", "Fall", "MATH", "130"));
        Student student = new Student("firstName","pictureURL",new ArrayList<>(cList));
        // Testing Student objects

        CourseItemAdapter courseItemAdapter = new CourseItemAdapter((ArrayList<Course>) student.getCourses());
        courseList.setAdapter(courseItemAdapter);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        courseList.setLayoutManager(lManager);
    }

    public void setStudentName(Student student) {
        //set student's name to textview
    }

    public void setAvatar(Student student) {
        //set student's avatar to imageview
    }

    public void getStudentCourse(Student student) {
        //load student's course list from db and initialize recyclerview.
    }

    /**
     * Finish Profile Activity and return to home when BACK button is clicked
     * @param view
     */
    public void onBackClicked(View view) {
        finish();   // Finish Profile Activity and return to home
    }

    /**
     * EXPLAIN COURSE ITEM ADAPTER CLASS BRIEFLY PLEASE
     */
    class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.ItemViewHolder> {
        private ArrayList<Course> mList;
        private RecyclerView.ViewHolder holder;

        /**
         * StudentItemAdapter constructor
         * @param list
         */
        public CourseItemAdapter(ArrayList<Course> list) {
            mList = list;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return
         */
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.course_item,parent,false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.course.setText(mList.get(position).getCourse());
            //holder.pid.setText(mList.get(position).getPid());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView course;
            //public TextView pid;
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                course = itemView.findViewById(R.id.course_name);
                //findViewById()
            }
        }
    }

}