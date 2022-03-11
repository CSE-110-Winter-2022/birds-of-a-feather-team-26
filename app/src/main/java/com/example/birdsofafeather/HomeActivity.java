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
import com.example.birdsofafeather.model.Student;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.Session;
import com.example.birdsofafeather.model.db.CourseDao;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonDao;
import com.example.birdsofafeather.model.db.SessionDao;

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
    private SessionDao sessionDao;
    private PersonDao personDao;
    private CourseDao courseDao;

    private Student myUser;

    Button myProfile;
    LinearLayoutManager lManager = new LinearLayoutManager(this);
    RecyclerView studentList;

//    private List<Student> fakeBluetoothStudents;
//    private List<Student> filteredStudents;

    private Session currSession;
    private int currSessionID;
    private boolean new_sess = false;

    /**
     * Home Activity onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Connect to BOF database
        db = AppDatabase.singleton(this);

        // DAO for storing current Session, all Student data, and Course data into BOF database
        sessionDao = db.SessionDao();
        personDao = db.PersonDao();
        courseDao = db.CourseDao();

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
                    search("default");
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
                        new_sess = false;                               // reset new_sess flag
                    }
                    else
                        uploadPersonsAndCoursesCurrentSessionIntoDB(currSessionID);  // Upload all newly searched Persons and Courses into BOF db, under currSession

                    /**
                     * 3. Stop displaying list of students with common courses
                     */
                    fillStudentItemAdapter(new ArrayList<>());          // Clear Student Item Adapter

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
                    startSession();
                    search("small_course");
                }

                else {
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;                               // reset new_sess flag
                    }
                    else
                        uploadPersonsAndCoursesCurrentSessionIntoDB(currSessionID);  // Upload all newly searched Persons and Courses into BOF db, under currSession

                    fillStudentItemAdapter(new ArrayList<>());          // Clear Student Item Adapter
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
                    startSession();
                    search("this_quarter");
                }

                else {
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;                               // reset new_sess flag
                    }
                    else
                        uploadPersonsAndCoursesCurrentSessionIntoDB(currSessionID);  // Upload all newly searched Persons and Courses into BOF db, under currSession

                    fillStudentItemAdapter(new ArrayList<>());          // Clear Student Item Adapter
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
                    startSession();
                    search("recent");
                }

                else {
                    if (new_sess) {
                        saveNewSession();
                        new_sess = false;                               // reset new_sess flag
                    }
                    else
                        uploadPersonsAndCoursesCurrentSessionIntoDB(currSessionID);  // Upload all newly searched Persons and Courses into BOF db, under currSession

                    fillStudentItemAdapter(new ArrayList<>());          // Clear Student Item Adapter
                }

            }
        });

    }

    /**
     * A.2 NEARBY API BLUETOOTH SEARCH
     * @return
     */
    public List<Student> bluetoothSearch() { return Data.fab_students(); }

    /**
     * HOME ACTIVITY MAIN FUNCTIONALITY FUNCTION
     * @param filter specifies what filtering to display
     */
    public void search(String filter) {
        /**
         * 2. Start bluetooth search for new Session
         */
        List<Student> fakeBluetoothStudents = bluetoothSearch();

        /**
         * 3. Filter Students with Common Courses (main Home Activity algorithm)
         *
         */
        List<Student> filteredStudents = new ArrayList<>();

        // Default filtering
        if (filter.equals("default"))
            filteredStudents = filterStudentsWithCommonCourses(fakeBluetoothStudents);

        // Small course filtering
        else if (filter.equals("small_course"))
            filteredStudents = filterSmallCourse(fakeBluetoothStudents);

        // This quarter only filtering
        else if (filter.equals("this_quarter"))
            filteredStudents = filterStudentsWithThisQuarter(fakeBluetoothStudents);

        // Recent classes filtering
        else if (filter.equals("recent"))
            filteredStudents = filterStudentsWithRecent(fakeBluetoothStudents);

        // THIS WILL ADD ALL SEARCHED & FILTERED STUDENTS TO CURRENT SESSION
        currSession.students.addAll(filteredStudents);

        /**
         * 4. Display list of Students with Common Courses
         */
        fillStudentItemAdapter(filteredStudents);
    }


    /**
     * Helper method for getting a person from BOF database with id and transforming Person data to Student object
     * @param id of Student we want to get from database
     * @return queried Student
     */
    public Student getPersonFromDBAndReturnStudent(int id) {

        // Retrieve my user's information from BOF database
        Person myPerson = personDao.getPerson(id);
        List<com.example.birdsofafeather.model.db.Course> myCoursesRaw = courseDao.getCoursesForPerson(id);

        // Convert List<db.Course> to List<Course>
        List<Course> myCourses = new ArrayList<>();
        for (com.example.birdsofafeather.model.db.Course course : myCoursesRaw) {
            Course c = new Course(course.year, course.quarter, course.courseName, course.courseNum, course.courseSize);
            myCourses.add(c);
        }

        return new Student(myPerson.personName, myPerson.url, myCourses, false);
    }


    /**
     * Helper method for uploading all newly searched Persons and Courses into BOF db, under currSession
     */
    public void uploadPersonsAndCoursesCurrentSessionIntoDB(int sess_id) {
        // Upload all Students and Courses within current session
        for (Student s : currSession.getStudents()) {
            com.example.birdsofafeather.model.db.Person person = new com.example.birdsofafeather.model.db.Person(sess_id, s.firstName, s.pictureURL);
            personDao.insertPerson(person);         // Insert Person who was seen in this session into Person db

            for (Course c : s.getCourses()) {
                com.example.birdsofafeather.model.db.Course course = new com.example.birdsofafeather.model.db.Course(person.personId, c.year, c.quarter, c.subject, c.courseNumber, c.courseSize);
                courseDao.insertCourse(course);     // Insert Course of a Person who was seen in this session into Course db
            }
        }
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
                currSession = new Session();            // start new blank Session
                new_sess = true;

                dialog.dismiss();                       // close start_session dialog
            }
        });
    }

    /**
     * B.1. Name a new Session and store session into database
     */
    public void saveNewSession() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View stop_session = getLayoutInflater().inflate(R.layout.stop_session, null);

        // Create and show start_session contact dialog
        dialogBuilder.setView(stop_session);
        dialog = dialogBuilder.create();
        dialog.show();

        /**
         * When name_session Button is clicked, the current new Session will be named and all Session data, Person data, and Course data will be uploaded to BOF db
         */
        Button name_session = (Button) stop_session.findViewById(R.id.name_session_btn);
        name_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = stop_session.findViewById(R.id.name_session_edittext);
                currSession.setName(name.getText().toString());                         // name session

                /**
                 * Upload new Session data, Person data, and Course data into BOF database
                 */
                // Create db Session object from new Session. Upload new db Session object to BOF database.
                com.example.birdsofafeather.model.db.Session newSession = new com.example.birdsofafeather.model.db.Session(currSession);
                sessionDao.insertSession(newSession);

                currSessionID = newSession.getSessionId();                              // current Session ID of new Session

                uploadPersonsAndCoursesCurrentSessionIntoDB(currSessionID);             // Upload all newly searched Persons and Courses into BOF db, under newSession

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
        for (Student student : bluetoothStudents) {

            double freq = 0;        // Initialize frequency level
            for (Course course : student.getCourses())
                for (Course c : myUser.getCourses())
                    if (course.equals(c))
                        freq++;

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

        // Map of course size with weights in filter Small Course algorithm
        Map<String, Double> courseSizeWeights = new HashMap<>();
        courseSizeWeights.put("tiny", 1.00);
        courseSizeWeights.put("small", 0.33);
        courseSizeWeights.put("medium", 0.18);
        courseSizeWeights.put("large", 0.10);
        courseSizeWeights.put("huge", 0.06);
        courseSizeWeights.put("gigantic", 0.03);

        // Map which records Small Course algorithm score each student has in comparison with myUser
        Map<Student,Double> map = new HashMap<>();

        for(Student stu:students) {

            double score = 0.0;     // Initialize frequency level
            for (Course course: stu.getCourses())
                for (Course c: myUser.getCourses())
                    if (course.equals(c))
                        score += courseSizeWeights.get(course.getCourseSize());

            // Add score to map if there is a course in common with student and myUser
            if (score > 0)
                map.put(stu, score);

        }

        // Sort score map by value
        Map<Student,Double> rtmap = sortByValue(map);

        // Log each key-value pair in map of student name and Small Course score
        for (Map.Entry<Student, Double> entry : rtmap.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(rtmap.keySet());         // Return ArrayList of students sorted in Small Course algorithm order
    }

    /**
     * Filter Students with Common Courses with "this quarter only" priority
     * @param bluetoothStudents
     * @return
     */
    public List<Student> filterStudentsWithThisQuarter(List<Student> bluetoothStudents) {
        // Map which records number of common courses each student takes with myUser in this quarter
        Map<Student, Double> frequencyStudents = new HashMap<>();

        String year = "2022", quarter = "Winter";   // This quarter

        // Traverse all Student objects
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

        // Log frequency map student name and common course frequency for this quarter
        for (Map.Entry<Student, Double> entry : frequencyStudents_sorted.entrySet())
            Log.i("HashMap", "key=" + entry.getKey().getFirstName() + ", value=" + entry.getValue());

        return new ArrayList<>(frequencyStudents_sorted.keySet());
    }


    public List<Student> filterStudentsWithRecent(List<Student> bluetoothStudents) {
        // Map which records recency algorithm score each student has in comparison with myUser
        Map<Student, Double> frequencyStudents = new HashMap<>();

        String year2022 = "2022", year2021 = "2021", year2020 = "2020";
        String winterQuarter = "Winter", fallQuarter = "Fall", summerQuarter = "Summer", springQuarter = "Spring";

        // Map which records recency algorithm score of each quarter and year
        Map<String, Double> recencyMap = new HashMap<>();
        recencyMap.put(winterQuarter+year2022, 0.0);      // This quarter
        recencyMap.put(fallQuarter+year2021, 5.0);        // A quarter ago (Fall '21)
        recencyMap.put(summerQuarter+year2021, 4.0);      // 2 quarters ago (Summer '21)
        recencyMap.put(springQuarter+year2021, 3.0);      // 3 quarters ago (Spring '21)
        recencyMap.put(winterQuarter+year2021, 2.0);      // 4 quarters ago (Winter '21)
        recencyMap.put(fallQuarter+year2020, 1.0);        // 5 quarters ago (Fall '20)
                                                                // Else too old, no added score

        // Traverse all Student objects
        for (Student student : bluetoothStudents) {

            double freq = 0; // set frequency level
            for (Course course : student.getCourses()) {
                // Traverse all the courses of the current student and judge the repetition rate of
                // your own courses
                for (Course c : myUser.getCourses()) {
                    if (course.equals(c)) {
                        String quarter_year = course.getQuarter()+course.getYear();     // Quarter and year of current course

                        // If course has a quarter and year within recencyMap, add recency score to current Student
                        if (recencyMap.containsKey(quarter_year))
                            freq += recencyMap.get(quarter_year);
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

                    currSessionID = sessionDao.getSessionIDFromName(currSession.name);  // Set currrent Session ID to clicked session

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
        vishvesh_courses.add(new Course("2020", "Fall", "CSE", "12", "large"));


        Student vishvesh = new Student("vishvesh", "vishesh.png", vishvesh_courses, false);

        // Student Derek
        List<Course> derek_courses = new ArrayList<>();

        derek_courses.add(new Course("2022", "Winter", "COGS", "10", "tiny"));
        derek_courses.add(new Course("2021", "Fall", "CSE", "100", "large"));

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