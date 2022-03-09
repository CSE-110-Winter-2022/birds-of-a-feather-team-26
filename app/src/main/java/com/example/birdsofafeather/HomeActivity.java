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
import android.widget.EditText;
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

    private List<Student> fakeBluetoothStudents;
    private List<Student> filteredStudents;

    private Session currSession;
    private boolean new_sess = false;

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

        // fabricated list of bluetooth-searched students
        fakeBluetoothStudents = Data.fab_students();

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
                    startSession();

                    /**
                     * 2. Start bluetooth search
                     */


                    /**
                     * 3. Filter Students with Common Courses (main Home Activity algorithm)
                     *
                     */
                    filteredStudents = filterStudentsWithCommonCourses(fakeBluetoothStudents);


                    /**
                     * 4. Display list of Students with Common Courses
                     */
                    // fill Student Item Adapter with list of students with common courses
                    fillStudentItemAdapter(filteredStudents);
                }

                /**
                 * B. When SEARCH button is toggled OFF:
                 *      1. Stop bluetooth search (MS 1)
                 *      2. Ask user to save session with <session_name> (MS 2) if new session is created
                 *      3. Stop displaying list of students with common courses (MS 1)
                 */
                else {
                    /**
                     * 1. Stop bluetooth search (this is automatic with our bluetooth search functionality, I believe)
                     */

                    /**
                     * 2. Ask user to save session with <session_name> if new session is created
                     */
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;       // reset new_sess flag
                    }

                    /**
                     * 3. Stop displaying list of students with common courses
                     */
                    // Clear Student Item Adapter
                    fillStudentItemAdapter(new ArrayList<>());
                }
            }
        });

        ToggleButton startSearchBySmallClass = findViewById(R.id.start_search_btn_bySmallClass);
        startSearchBySmallClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * This method will execute the bluetooth search functionality for small classes when toggled on.
             * @param buttonView
             * @param isChecked boolean which represents state of startSearch ToggleButton
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    /**
                     * 1. Ask user if they want to resume a previous session or start a new session
                     */
                    startSession();
                    /**
                     * 2. Start bluetooth search
                     */
                    /**
                     * 3. Filter Students with Common Courses (main Home Activity algorithm)
                     *
                     */
                    filteredStudents = filterSmallCourse(fakeBluetoothStudents);
                    /**
                     * 4. Display list of Students with Common Courses
                     */
                    // fill Student Item Adapter with list of students with common courses
                    fillStudentItemAdapter(filteredStudents);
                }

                else {
                    /**
                     * 1. Stop bluetooth search
                     */
                    /**
                     * 2. Ask user to save session with <session_name>
                     */
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;       // reset new_sess flag
                    }
                    /**
                     * 3. Stop displaying list of students with common courses
                     */
                    // Clear Student Item Adapter
                    fillStudentItemAdapter(new ArrayList<>());
                }

            }
        });


        ToggleButton startSearchByQuarter = findViewById(R.id.start_search_btn_byQuarter);
        startSearchByQuarter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * This method will execute the bluetooth search functionality for this quarter only when toggled on.
             * @param buttonView
             * @param isChecked boolean which represents state of startSearch ToggleButton
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    /**
                     * 1. Ask user if they want to resume a previous session or start a new session
                     */
                    startSession();
                    /**
                     * 2. Start bluetooth search
                     */
                    /**
                     * 3. Filter Students with Common Courses (main Home Activity algorithm)
                     *
                     */
                    filteredStudents = filterStudentsWithThisQuarter(fakeBluetoothStudents);
                    /**
                     * 4. Display list of Students with Common Courses
                     */
                    // fill Student Item Adapter with list of students with common courses
                    fillStudentItemAdapter(filteredStudents);
                }

                else {
                    /**
                     * 1. Stop bluetooth search
                     */
                    /**
                     * 2. Ask user to save session with <session_name>
                     */
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;       // reset new_sess flag
                    }
                    /**
                     * 3. Stop displaying list of students with common courses
                     */
                    // Clear Student Item Adapter
                    fillStudentItemAdapter(new ArrayList<>());
                }

            }
        });

        ToggleButton startSearchByRecent = findViewById(R.id.start_search_btn_byRecent);
        startSearchByRecent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * This method will execute the bluetooth search functionality for most recent courses when toggled on.
             * @param buttonView
             * @param isChecked boolean which represents state of startSearch ToggleButton
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    /**
                     * 1. Ask user if they want to resume a previous session or start a new session
                     */
                    startSession();
                    /**
                     * 2. Start bluetooth search
                     */
                    /**
                     * 3. Filter Students with Common Courses (main Home Activity algorithm)
                     *
                     */
                    filteredStudents = filterStudentsWithRecent(fakeBluetoothStudents);
                    /**
                     * 4. Display list of Students with Common Courses
                     */
                    // fill Student Item Adapter with list of students with common courses
                    fillStudentItemAdapter(filteredStudents);
                }

                else {
                    /**
                     * 1. Stop bluetooth search
                     */
                    /**
                     * 2. Ask user to save session with <session_name>
                     */
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;       // reset new_sess flag
                    }
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
        IPerson myPerson = db.PersonDao().get(id);
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
    public void startSession() {
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
                new_sess = true;

                dialog.dismiss();                               // close start_session dialog
            }
        });
    }

    /**
     * B.1. Ask user if they want to resume a previous session or start a new session
     */
    public void saveNewSession() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View stop_session = getLayoutInflater().inflate(R.layout.stop_session, null);

        // Create and show start_session contact dialog
        dialogBuilder.setView(stop_session);
        dialog = dialogBuilder.create();
        dialog.show();

        /**
         * When new_session Button is clicked, BoF will start a new blank session
         */
        Button name_session = (Button) stop_session.findViewById(R.id.name_session_btn);
        name_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = stop_session.findViewById(R.id.name_session_edittext);
                currSession.setName(name.getText().toString());                         // name session

                dialog.dismiss();                                                       // close start_session dialog
            }
        });
    }

    /**
     * A.3. Filter Students with Common Courses (main Home Activity algorithm)
     * @param bluetoothStudents
     * @return List<Student> of students who've taken a course in common with myUser
     */
    public List<Student> filterStudentsWithCommonCourses(List<Student> bluetoothStudents) {
        // Map which records number of common courses each student takes with myUser
        Map<Student, Double> frequencyStudents = new HashMap<>();
        // Count number of common courses each student takes with myUser
        //traverse all student object
        for (Student student : bluetoothStudents) {
            double freq = 0; //set frequency level
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
        Map<Student, Double> frequencyStudents_sorted = sortByValue(frequencyStudents);

        // Log frequency map student name and common course frequency
        for (Map.Entry<Student, Double> entry : frequencyStudents_sorted.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(frequencyStudents_sorted.keySet());
    }

    /**
     * Filter Students with Common Courses with "prioritize small classes" priority
     * @param students
     * @return
     */
    public List<Student> filterSmallCourse(List<Student> students){

        Map<String, Double> courseSizeWeights = new HashMap<>();
        courseSizeWeights.put("tiny", 1.00);
        courseSizeWeights.put("small", 0.33);
        courseSizeWeights.put("medium", 0.18);
        courseSizeWeights.put("large", 0.10);
        courseSizeWeights.put("huge", 0.06);
        courseSizeWeights.put("gigantic", 0.03);

        Map<Student,Double> map=new HashMap<Student,Double>();

        for(Student stu:students){
            double score=0.0;
            for(Course course: stu.getCourses()){
                for (Course c: myUser.getCourses()) {
                    if (course.equals(c)) {
                        score += courseSizeWeights.get(course.getCourseSize());
                    }
                }
            }
            if(score>0) {
                map.put(stu, score);
            }
        }
        Map<Student,Double> rtmap=sortByValue(map);
        List<Student> rtlist=new ArrayList<>();
        for(Student student: rtmap.keySet()){
            rtlist.add(student);
        }
        return rtlist;
    }

    /**
     * Filter Students with Common Courses with "this quarter only" priority
     * @param bluetoothStudents
     * @return
     */
    public List<Student> filterStudentsWithThisQuarter(List<Student> bluetoothStudents) {
        // Map which records number of common courses each student takes with myUser
        Map<Student, Double> frequencyStudents = new HashMap<>();
        String year = "2022", quarter = "Winter";
        // String courseSize = "small";
        //traverse all student object
        for (Student student : bluetoothStudents) {
            double freq = 0; // set frequency level
            for (Course course : student.getCourses()) {
                //Traverse all the courses of the current student and judge the repetition rate of
                // your own courses
                /** I think this part can be written in this way
                if(myUser.getCourses().contains(course)) {
                    if(course.getCourseSize().equals(courseSize))
                        freq++;
                } **/
                for (Course c : myUser.getCourses()) {
                    if(course.equals(c)) {
                        if(course.getYear().equals(year) && course.getQuarter().equals(quarter)) {
                            freq++;
                        }
                    }
                }
            }

            // Add to frequency map if there is a course in common with student and myUser
            if (freq > 0)
                frequencyStudents.put(student, freq);
        }

        // Sort frequency map by value
        Map<Student, Double> frequencyStudents_sorted = sortByValue(frequencyStudents);

        // Log frequency map student name and common course frequency
        for (Map.Entry<Student, Double> entry : frequencyStudents_sorted.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(frequencyStudents_sorted.keySet());
    }


    public List<Student> filterStudentsWithRecent(List<Student> bluetoothStudents) {
        // Map which records number of common courses each student takes with myUser
        Map<Student, Double> frequencyStudents = new HashMap<>();
        String year2022 = "2022";
        String winterQuarter = "Winter";
        String year2021 = "2021", year2020 = "2020";
        String springQuarter = "Spring", summerQuarter = "Summer", fallQuarter = "Fall";
        //traverse all student object
        for (Student student : bluetoothStudents) {
            double freq = 0; // set frequency level
            for (Course course : student.getCourses()) {
                //Traverse all the courses of the current student and judge the repetition rate of
                // your own courses
                for (Course c : myUser.getCourses()) {
                    if (course.equals(c)) {
                        if (course.getYear().equals(year2022) && course.getQuarter().equals(winterQuarter)) {
                            continue;
                        }
                        else if (course.getYear().equals(year2021) && course.getQuarter().equals(fallQuarter)) {
                            freq = freq + 5;
                        }
                        else if (course.getYear().equals(year2021) && course.getQuarter().equals(summerQuarter)) {
                            freq = freq + 4;
                        }
                        else if (course.getYear().equals(year2021) && course.getQuarter().equals(springQuarter)) {
                            freq = freq + 3;
                        }
                        else if (course.getYear().equals(year2020) && course.getQuarter().equals(winterQuarter)) {
                            freq = freq + 2;
                        }
                        else if (course.getYear().equals(year2020) && course.getQuarter().equals(fallQuarter)) {
                            freq = freq + 1;
                        }
                        else {
                            freq = freq + 0;
                        }
                    }
                }
            }

            // Add to frequency map if there is a course in common with student and myUser
            if (freq > 0)
                frequencyStudents.put(student, freq);
        }

        // Sort frequency map by value
        Map<Student, Double> frequencyStudents_sorted = sortByValue(frequencyStudents);

        // Log frequency map student name and common course frequency
        for (Map.Entry<Student, Double> entry : frequencyStudents_sorted.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(frequencyStudents_sorted.keySet());
    }









    /**
     * Helper function to sort HashMap by values
     * @param freq
     * @return Map sorted by values
     */
    public Map<Student, Double> sortByValue(Map<Student, Double> freq) {
        // Create a linked list from elements of HashMap
        List<Map.Entry<Student, Double>> linkedFreq = new LinkedList<>(freq.entrySet());

        // Sort the linked list
        Collections.sort(linkedFreq, new Comparator<Map.Entry<Student, Double>>() {
            @Override
            public int compare(Map.Entry<Student, Double> freq1, Map.Entry<Student, Double> freq2) {
                return freq2.getValue().compareTo(freq1.getValue());
            }
        });

        // Convert sorted linked list to HashMap
        Map<Student, Double> freq_sorted = new LinkedHashMap<>();
        for (Map.Entry<Student, Double> entry : linkedFreq)
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
        List<Student> fakeBluetoothStudents = new ArrayList<>();

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

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "tiny"));

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

        // Add all Students into fakeBluetoothStudents
        fakeBluetoothStudents.add(zehua);
        fakeBluetoothStudents.add(vishvesh);
        fakeBluetoothStudents.add(derek);
        fakeBluetoothStudents.add(huaner);
        fakeBluetoothStudents.add(ivy);

        return fakeBluetoothStudents;
    }

    /**
     * Fabricate session data method
     *
     * @return ArrayList of fake Sessions
     */
    public static List<Session> fab_sessions() {
        // All sessions is our fabricated list of students
        List<Session> fakeSessions = new ArrayList<>();

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

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "tiny"));

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
        fakeSessions.add(session1);
        fakeSessions.add(session2);

        return fakeSessions;
    }
}