package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Person;

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
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        // Intent
        Intent i = getIntent();
        Person user = (Person) i.getSerializableExtra("Student");

        // Set user profile name
        String name = user.personName;
        studentName = findViewById(R.id.profile_name);
        studentName.setText(name);

        // Set user picture
        String pictureURL = user.url;
        avatar = findViewById(R.id.avatar);

        // Course Item Adapter to display courses of user
        courseList = findViewById(R.id.profile_course_list);
        Log.wtf("course", user.getCourseList().get(0).getCourse());
        CourseItemAdapter courseItemAdapter = new CourseItemAdapter((ArrayList<Course>) user.getCourseList());
        courseList.setAdapter(courseItemAdapter);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        courseList.setLayoutManager(lManager);
    }

    /**
     * Finish Profile Activity and return to home when BACK button is clicked
     */
    public void onBackClicked(View view) {
        finish();
    }

    /**
     * EXPLAIN COURSE ITEM ADAPTER CLASS BRIEFLY PLEASE
     */
    class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.ItemViewHolder> {
        private ArrayList<Course> mList;
        private RecyclerView.ViewHolder holder;

        /**
         * Course Item Adapter constructor
         */
        public CourseItemAdapter(ArrayList<Course> list) {
            mList = list;
        }

        /**
         * Creates Course Item View Holder
         */
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.course_item,parent,false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.course.setText(mList.get(position).getCourse());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView course;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                course = itemView.findViewById(R.id.course_name);
            }
        }
    }

}