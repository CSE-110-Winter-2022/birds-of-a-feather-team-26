
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.Student;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.Student;

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

    private AppDatabase db;
    private Student myUser;

    Button myProfile;
    LinearLayoutManager lManager = new LinearLayoutManager(this);
    RecyclerView studentList;

    private List<Student> allStudents;
    private List<Student> filteredStudents;

    /**
     * This method creates the Home Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // myUser Student object
        myUser = getPersonFromDBAndReturnStudent(0);

        /**
         * My Profile button to display myUser data
         */
        myProfile = findViewById(R.id.my_profile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                // Insert myUser data
                intent.putExtra("Student", myUser);
                startActivity(intent);
            }
        });

        // All students is our fabricated list of students
        allStudents = Data.fabricate();

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

                /**
                 * When SEARCH button is toggled ON:
                 *      1. Ask user if they want to resume a previous session or start a new session (MS 2)
                 *      2. Start bluetooth search (MS 1)
                 *      3. Filter students by common courses (MS 1)
                 *      4. Display list of students with common courses (MS 1)
                 */
                if (isChecked) {
                    /**
                     * 1. Ask user if they want to resume a previous session or start a new session
                     */


                    /**
                     * 2. Start bluetooth search
                     */


                    /**
                     * 3. Filter Students with Common Courses (main Home Activity algorithm)
                     *
                     */
                    filteredStudents = filterStudentsWithCommonCourses(allStudents);


                    /**
                     * 4. Display list of Students with Common Courses
                     */
                    // Student Item Adapter to display list of students with common courses
                    StudentItemAdapter studentItemAdapter = new StudentItemAdapter(filteredStudents);
                    studentList = findViewById(R.id.student_list);
                    studentList.setAdapter(studentItemAdapter);
                    studentList.setLayoutManager(lManager);
                }

                /**
                 * When SEARCH button is toggled OFF:
                 *      1. Stop bluetooth search (MS 1)
                 *      2. Ask user to save session with <session_name> (MS 2)
                 *      3. Stop displaying list of students with common courses (MS 1)
                 */
                else {
                    /**
                     * 1. Stop bluetooth search
                     */


                    /**
                     * 2. Ask user to save session with <session_name>
                     */


                    /**
                     * 3. Stop displaying list of students with common courses
                     */
                    // Clear Student Item Adapter
                    StudentItemAdapter clear = new StudentItemAdapter(new ArrayList<>());
                    studentList = findViewById(R.id.student_list);
                    studentList.setAdapter(clear);
                    studentList.setLayoutManager(lManager);
                }

            }

        });

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
            Course c = new Course(course.year, course.quarter, course.courseName, course.courseNum, course.courseSize);
            myCourses.add(c);
        }

        return new Student(myPerson.getName(), myPerson.getUrl(), myCourses);
    }

    /**
     * B. Filter Students with Common Courses (main Home Activity algorithm)
     * @param allStudents
     * @return
     */
    public List<Student> filterStudentsWithCommonCourses(List<Student> allStudents) {
        // Map which records number of common courses each student takes with myUser
        Map<Student, Integer> frequencyStudents = new HashMap<>();

        // Count number of common courses each student takes with myUser
        for (Student student : allStudents) {
            int freq = 0;
            for (Course course : student.getCourses()) {
                for (Course c : myUser.getCourses()) {
                    if (course.equals(c))
                        freq++;
                }
            }

            // Add to frequency map if there is a course in common with student and myUser
            if (freq > 0)
                frequencyStudents.put(student, freq);

        }

        // Sort frequency map by value
        Map<Student, Integer> frequencyStudents_sorted = sortByValue(frequencyStudents);

        // Log frequency map student name and common course frequency
        for (Map.Entry<Student, Integer> entry : frequencyStudents_sorted.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(frequencyStudents_sorted.keySet());
    }

    /**
     * Helper function to sort HashMap by values
     * @param freq
     * @return HashMap sorted by values
     */
    public Map<Student, Integer> sortByValue(Map<Student, Integer> freq) {
        // Create a linked list from elements of HashMap
        List<Map.Entry<Student, Integer>> linkedFreq = new LinkedList<>(freq.entrySet());

        // Sort the linked list
        Collections.sort(linkedFreq, new Comparator<Map.Entry<Student, Integer>>() {
            @Override
            public int compare(Map.Entry<Student, Integer> freq1, Map.Entry<Student, Integer> freq2) {
                return freq2.getValue().compareTo(freq1.getValue());
            }
        });

        // Convert sorted linked list to HashMap
        Map<Student, Integer> freq_sorted = new LinkedHashMap<>();
        for (Map.Entry<Student, Integer> entry : linkedFreq)
            freq_sorted.put(entry.getKey(), entry.getValue());

        return freq_sorted;
    }

    /**
     * STUDENT ITEM ADAPTER CLASS
     */
    class StudentItemAdapter extends RecyclerView.Adapter<StudentItemAdapter.ItemViewHolder> {
        private List<Student> mList;
        private RecyclerView.ViewHolder holder;

        /**
         * StudentItemAdapter constructor
         * @param list
         */
        public StudentItemAdapter(List<Student> list) {
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
            Student student = mList.get(position);
            holder.name.setText(student.getFirstName());
            holder.findName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                    intent.putExtra("Student", student);
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
            }
        }
    }
}

/**
 * FABRICATE DATA HELPER CLASS
 */
class Data {
    /**
     * Fabricate student data method
     * @return ArrayList of fake Students
     */
    public static List<Student> fabricate() {
        // All students is our fabricated list of students
        List<Student> allStudents = new ArrayList<>();

        // Student Zehua
        List<Course> zehua_courses = new ArrayList<>();

        zehua_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "151B", "medium"));
        zehua_courses.add(new Course("2022", "Winter", "PHIL", "141", "small"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "152A", "small"));

        Student zehua = new Student("zehua", "zehua.png", zehua_courses);

        // Student Vishvesh
        List<Course> vishvesh_courses = new ArrayList<>();

        vishvesh_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));

        Student vishvesh = new Student("vishvesh", "vishesh.png", vishvesh_courses);

        // Student Derek
        List<Course> derek_courses = new ArrayList<>();

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "huge"));

        Student derek = new Student("derek", "derek.png", derek_courses);

        // Student Huaner
        List<Course> huaner_courses = new ArrayList<>();

        huaner_courses.add(new Course("2019", "Fall", "CSE", "110", "large"));
        huaner_courses.add(new Course("2022", "Winter", "CSE", "141", "large"));

        Student huaner = new Student("huaner", "huaner.png", huaner_courses);

        // Student Ivy
        List<Course> ivy_courses = new ArrayList<>();

        ivy_courses.add(new Course("2022", "Winter", "CSE", "151B", "medium"));
        ivy_courses.add(new Course("2022", "Winter", "PHIL", "141", "small"));

        Student ivy = new Student("ivy", "ivy.png", ivy_courses);

        // Add all Students into allStudents
        allStudents.add(zehua);
        allStudents.add(vishvesh);
        allStudents.add(derek);
        allStudents.add(huaner);
        allStudents.add(ivy);

        return allStudents;
    }
}