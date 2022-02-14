package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.Student;

import java.util.ArrayList;

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
 *          - startActivity(ProfileActivity)
 *
 **/

public class HomeActivity extends AppCompatActivity {

    Button myProfile;
    RecyclerView studentList;

    /**
     * This method creates the Home Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
         * C. Display list of Students with Common Courses (zzh)
         */

        myProfile = findViewById(R.id.my_profile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                intent.putExtra("Student name", "MY NAME");
                startActivity(intent);
            }
        });
        studentList = findViewById(R.id.student_list);

        ArrayList<Student> sList = new ArrayList<>();
        ArrayList<Course> cList = new ArrayList<>();
        cList.add(new Course("2022", "Winter", "CSE", "110"));
        cList.add(new Course("2023", "Spring", "ECE", "120"));
        cList.add(new Course("2024", "Fall", "MATH", "130"));
        Student student = new Student("firstName","pictureURL",new ArrayList<>(cList));
        sList.add(student);
        sList.add(new Student("firstName2", "pictureURL2", new ArrayList<>(cList)));
        sList.add(new Student("firstName3", "pictureURL3", new ArrayList<>(cList)));

        StudentItemAdapter studentItemAdapter = new StudentItemAdapter(sList);
        studentList.setAdapter(studentItemAdapter);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        studentList.setLayoutManager(lManager);
    }

    /**
     * Helper method to show messages for testing
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * temporary button to switch to Profile Activity
     * @param view
     */
    public void onProfileActivityClicked(View view) {
        Intent intentProfileActivityWorkflow = new Intent(this, ProfileActivity.class);
        startActivity(intentProfileActivityWorkflow);
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
        public StudentItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.student_item,parent,false);
            return new StudentItemAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentItemAdapter.ItemViewHolder holder, int position) {
            String studentName = mList.get(position).getFirstName();
            holder.name.setText(studentName);
            holder.findName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                    intent.putExtra("Student name", studentName);
                    startActivity(intent);
                }
            });
            //holder.pid.setText(mList.get(position).getPid());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public LinearLayout findName;
            //public TextView pid;
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.student_name);
                findName = itemView.findViewById(R.id.find_student);
                //findViewById()
            }
        }
    }
}