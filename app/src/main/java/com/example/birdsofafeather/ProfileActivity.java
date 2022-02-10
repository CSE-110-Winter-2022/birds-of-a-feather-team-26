package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     * This method creates the Profile Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecyclerView recyclerView = findViewById(R.id.profile_student_list);

        // Testing Student objects
        ArrayList<Student> students = new ArrayList<>();

        Student student1 = new Student("first", "student1.png", new ArrayList<Course>({
                new Course("2022", "Winter", "CSE", "110")
        }));

        students.add(new Student("second"));
        students.add(new Student("third"));
        students.add(new Student("fourth"));

        StudentItemAdapter studentItemAdapter = new StudentItemAdapter(students);
        recyclerView.setAdapter(studentItemAdapter);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);
    }


    class StudentItemAdapter extends RecyclerView.Adapter<StudentItemAdapter.ItemViewHolder> {
        private ArrayList<Student> mList;
        private RecyclerView.ViewHolder holder;

        /**
         * StudentItemAdapter constructor
         * @param list
         */
        public StudentItemAdapter(ArrayList<Student> list) {
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
            View view = inflater.inflate(R.layout.student_item,parent,false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.firstName.setText(mList.get(position).getFirstName());
            //holder.pid.setText(mList.get(position).getPid());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView firstName;
            //public TextView pid;
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                firstName = itemView.findViewById(R.id.student_name);
                //findViewById()
            }
        }
    }

}