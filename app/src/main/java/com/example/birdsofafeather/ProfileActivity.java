package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.Model.Course;
import com.example.birdsofafeather.Model.Student;

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
        studentName = findViewById(R.id.profile_name);
        avatar = findViewById(R.id.avatar);
        courseList = findViewById(R.id.profile_course_list);
        //Button back =findViewById(R.id.back);

        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, back.getClass());
                startActivity(intent);
            }
        });*/

        //Student student = getIntent()....
        ArrayList<Course> cList = new ArrayList<>();
        cList.add(new Course("2022", "Winter", "CSE", "110"));
        cList.add(new Course("2023", "Spring", "ECE", "120"));
        cList.add(new Course("2024", "Fall", "MATH", "130"));
        //Student student = new Student("firstName","pictureURL",new ArrayList<>(cList));
        // Testing Student objects


        CourseItemAdapter courseItemAdapter = new CourseItemAdapter(cList);
        courseList.setAdapter(courseItemAdapter);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        courseList.setLayoutManager(lManager);
    }

    public void setStudentName() {
        //set student's name to textview
    }

    public void setAvatar() {
        //set student's avatar to imageview
    }

    public void getStudentCourse() {
        //load student's course list from db and initialize recyclerview.
    }

    public void onLaunchProfileClicked(View view) {
        Intent intentProfileActivityWorkflow = new Intent(this, HomeActivity.class);
        //Intent intentHomeActivityWorkflow = new Intent(this, ProfileActivity.class);
        startActivity(intentProfileActivityWorkflow);
    }

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