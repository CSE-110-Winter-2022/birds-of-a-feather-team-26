package com.example.birdsofafeather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.Course;
import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.Student;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * DESCRIPTION
 * The Home Activity module is the Birds of a Feather homepage and takes care of the application's main functionality.
 * Displays a list of all students who have taken a course in common with the user when bluetooth search is started.
 *
 * WORKFLOW
 * Home Activity workflow is as follows:
 *
 * I. Bluetooth Search Functionality
 *      - Triggered by the START SEARCH Toggle Button
 *      - Connect to all Bluetooth devices within range and try to fetch other BOF user's data
 *      - Person objects
 *
 * II. Filter Students with Common Courses (main Home Activity algorithm)
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
 * III. Display list of Students with Common Courses
 *      - Adapter
 *      - onClick
 *          - Intent.putExtra(Person.getName())
 *          - startActivity(ProfileActivity)
 *
 **/

public class HomeActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private AppDatabase db;
    private Student myUser;

    Button myProfile;
    LinearLayoutManager lManager = new LinearLayoutManager(this);
    RecyclerView studentList;

    private List<Student> allStudents;
    private List<Student> filteredStudents;

    private Session currSession;

    /**
     * Home Activity onCreate
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
        allStudents = Data.fab_students();

        /**
         * I. TOGGLE BUTTON WHICH TRIGGERS BLUETOOTH FUNCTIONALITY
         */
        ToggleButton startSearch = (ToggleButton) findViewById(R.id.start_search_btn);
        startSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * This method will execute the bluetooth search functionality when toggled on.
             * @param buttonView
             * @param isChecked boolean which represents state of startSearch ToggleButton
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /**
                 * A. When SEARCH button is toggled ON:
                 *      1. Ask user if they want to resume a previous session or start a new session (MS 2)
                 *      2. Start bluetooth search (MS 1)
                 *      3. Filter students by common courses (MS 1)
                 *      4. Display list of students with common courses (MS 1)
                 */
                if (isChecked) {
                    /**
                     * 1. Ask user if they want to resume a previous session or start a new session
                     */
                    createNewContactDialog();

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
                    // fill Student Item Adapter with list of students with common courses
                    fillStudentItemAdapter(filteredStudents);
                }

                /**
                 * B. When SEARCH button is toggled OFF:
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
                    fillStudentItemAdapter(new ArrayList<>());
                }
            }
        });
    }

    /**
     * Helper method for getting a person from BOF database with id and transforming Person data to Student object
     * @param id of Student we want to get from database
     * @return queried Student
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

        return new Student(myPerson.getName(), myPerson.getUrl(), myCourses, false);
    }

    /**
     * A.1. Ask user if they want to resume a previous session or start a new session
     */
    public void createNewContactDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View start_session = getLayoutInflater().inflate(R.layout.start_session, null);

        // Display Adapter of sessions
        LinearLayoutManager sessionManager = new LinearLayoutManager(this); //
        RecyclerView sessionList;

        List<Session> session_data = Data.fab_sessions();                           // fabricated session data

        SessionItemAdapter sessions = new SessionItemAdapter(session_data);         // create Session Item Adapter
        sessionList = start_session.findViewById(R.id.session_list);
        sessionList.setAdapter(sessions);                                           // set Session Item Adapter to hold session_data
        sessionList.setLayoutManager(sessionManager);                               // display LayoutManager with session list

        // Create and show start_session contact dialog
        dialogBuilder.setView(start_session);
        dialog = dialogBuilder.create();
        dialog.show();

        /**
         * When new_session Button is clicked, BoF will start a new blank session
         */
        Button new_session = (Button) start_session.findViewById(R.id.new_session_btn);
        new_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currSession = new Session(new ArrayList<>());   // start new blank Session

                dialog.dismiss();                               // close start_session dialog
            }
        });
    }

    /**
     * A.3. Filter Students with Common Courses (main Home Activity algorithm)
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
     * @return Map sorted by values
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
     * A.4. Method to fill Student Item Adapter
     */
    public void fillStudentItemAdapter(List<Student> student_data) {
        StudentItemAdapter students = new StudentItemAdapter(student_data);     // create Student Item Adapter
        studentList = findViewById(R.id.student_list);
        studentList.setAdapter(students);                                       // set Student Item Adapter to hold student_data
        studentList.setLayoutManager(lManager);                                 // display LayoutManager with student list
    }


    /**
     * DESCRIPTION
     * The StudentItemAdapter class sets up an Adapter interface for displaying Student information in Home Activity
     */
    class StudentItemAdapter extends RecyclerView.Adapter<StudentItemAdapter.ItemViewHolder> {
        private List<Student> mList;
        private RecyclerView.ViewHolder holder;

        public StudentItemAdapter(List<Student> list) {
            mList = list;
        }

        /**
         * Creates Student Item View Holder
         */
        public StudentItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.student_item, parent,false);
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
            holder.star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // set star icon to yellow once clicked
                    ((ImageButton)(view.findViewById(R.id.starButton))).setColorFilter(Color.YELLOW);

                    holder.star.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            // set star icon to gray if click again to cancel "favorite"
                            ((ImageButton)(view.findViewById(R.id.starButton))).setColorFilter(Color.GRAY);
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public LinearLayout findName;
            public ImageButton star;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.student_name);
                findName = itemView.findViewById(R.id.find_student);
                star = itemView.findViewById(R.id.starButton);
            }
        }
    }


    /**
     * DESCRIPTION
     * The SessionsItemAdapter class sets up an Adapter interface for displaying Session information in start_session popup
     */
    class SessionItemAdapter extends RecyclerView.Adapter<SessionItemAdapter.ItemViewHolder> {
        private List<Session> sessionList;
        private RecyclerView.ViewHolder holder;

        public SessionItemAdapter(List<Session> list) {
            sessionList = list;
        }

        /**
         * Creates Session Item View Holder
         */
        public SessionItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.session_item, parent,false);
            return new SessionItemAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SessionItemAdapter.ItemViewHolder holder, int position) {
            Session session = sessionList.get(position);
            holder.name.setText(session.getName());

            // When click on Session ViewHolder, load clicked Session
            holder.findName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeActivity.this, session.getName(), Toast.LENGTH_LONG).show();

                    /**
                     * When a Session ViewHolder is clicked, BoF will load the respective session
                     */
                    currSession = session;              // return and load clicked Session

                    dialog.dismiss();                   // close start_session dialog
                }
            });
        }

        @Override
        public int getItemCount() {
            return sessionList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public LinearLayout findName;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.session_name);
                findName = itemView.findViewById(R.id.find_session);
            }
        }
    }

    /**
     * Click on My Sessions button
     * @param view
     */
    public void goToSessionClick(View view) {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }
}


/**
 * FABRICATE DATA HELPER CLASS
 *
 * DESCRIPTION
 * The Data class fabricates data for Student Lists and Session Lists
 */
class Data {
    /**
     * Fabricate student data method
     *
     * @return ArrayList of fake Students
     */
    public static List<Student> fab_students() {
        // All students is our fabricated list of students
        List<Student> allStudents = new ArrayList<>();

        // Student Zehua
        List<Course> zehua_courses = new ArrayList<>();

        zehua_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "151B", "medium"));
        zehua_courses.add(new Course("2022", "Winter", "PHIL", "141", "small"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "152A", "small"));

        Student zehua = new Student("zehua", "zehua.png", zehua_courses, false);

        // Student Vishvesh
        List<Course> vishvesh_courses = new ArrayList<>();

        vishvesh_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));

        Student vishvesh = new Student("vishvesh", "vishesh.png", vishvesh_courses, false);

        // Student Derek
        List<Course> derek_courses = new ArrayList<>();

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "huge"));

        Student derek = new Student("derek", "derek.png", derek_courses, false);

        // Student Huaner
        List<Course> huaner_courses = new ArrayList<>();

        huaner_courses.add(new Course("2019", "Fall", "CSE", "110", "large"));
        huaner_courses.add(new Course("2022", "Winter", "CSE", "141", "large"));

        Student huaner = new Student("huaner", "huaner.png", huaner_courses, false);

        // Student Ivy
        List<Course> ivy_courses = new ArrayList<>();

        ivy_courses.add(new Course("2022", "Winter", "CSE", "151B", "medium"));
        ivy_courses.add(new Course("2022", "Winter", "PHIL", "141", "small"));

        Student ivy = new Student("ivy", "ivy.png", ivy_courses, false);

        // Add all Students into allStudents
        allStudents.add(zehua);
        allStudents.add(vishvesh);
        allStudents.add(derek);
        allStudents.add(huaner);
        allStudents.add(ivy);

        return allStudents;
    }

    /**
     * Fabricate session data method
     *
     * @return ArrayList of fake Sessions
     */
    public static List<Session> fab_sessions() {
        // All sessions is our fabricated list of students
        List<Session> allSessions = new ArrayList<>();

        // All students is our fabricated list of students
        List<Student> students1 = new ArrayList<>();

        // Student Zehua
        List<Course> zehua_courses = new ArrayList<>();

        zehua_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "151B", "medium"));
        zehua_courses.add(new Course("2022", "Winter", "PHIL", "141", "small"));
        zehua_courses.add(new Course("2022", "Winter", "CSE", "152A", "small"));

        Student zehua = new Student("zehua", "zehua.png", zehua_courses, false);

        // Student Derek
        List<Course> derek_courses = new ArrayList<>();

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "huge"));

        Student derek = new Student("derek", "derek.png", derek_courses, false);

        // Add session1 Students into students1
        students1.add(zehua);
        students1.add(derek);

        // Set up session1 to have student1 as Student List
        Session session1 = new Session("session 1", students1);

        // All students is our fabricated list of students
        List<Student> students2 = new ArrayList<>();

        // Student Vishvesh
        List<Course> vishvesh_courses = new ArrayList<>();

        vishvesh_courses.add(new Course("2022", "Winter", "CSE", "110", "large"));

        Student vishvesh = new Student("vishvesh", "vishesh.png", vishvesh_courses, false);

        // Student Huaner
        List<Course> huaner_courses = new ArrayList<>();

        huaner_courses.add(new Course("2019", "Fall", "CSE", "110", "large"));
        huaner_courses.add(new Course("2022", "Winter", "CSE", "141", "large"));

        Student huaner = new Student("huaner", "huaner.png", huaner_courses, false);

        // Add session2 Students into students2
        students2.add(vishvesh);
        students2.add(huaner);

        // Set up session1 to have student1 as Student List
        Session session2 = new Session("session 2", students2);

        // Add sessions to allSessions
        allSessions.add(session1);
        allSessions.add(session2);

        return allSessions;
    }
}