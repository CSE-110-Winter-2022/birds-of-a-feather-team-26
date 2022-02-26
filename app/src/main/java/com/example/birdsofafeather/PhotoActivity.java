package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;

import java.util.Random;

/**
 * DESCRIPTION
 *
 * The photo activity needs the user to input his/her url of the photo they want to upload. If the
 * user prefer not to upload, the app we design will automatically upload a default image for them.
 * After click the CONFIRM button, the user will be directed to the adding course page.
 */

public class PhotoActivity extends AppCompatActivity {

    /**
     * This method creates the Photo Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }

    /**
     * Start Course Activity when CONFIRM button is clicked
     * @param view
     */
    public void onClickToCourse(View view) {
        // Intent to start Course Activity
        Intent intent = new Intent(this, CourseActivity.class);
        Intent intent1 = getIntent();

        // Connect to BOF database
        AppDatabase db = AppDatabase.singleton(this.getApplicationContext());

        // Index new person_id
        // int i = new Random().nextInt(1000);
        int i = db.PersonDao().count();

        // Retrieve user's photo URL
        TextView urlView = findViewById(R.id.editTextPhoto);
        String url = urlView.getText().toString();

        // Pass user's photo URL and person_id to Course Activity
        intent.putExtra("photo_url",url);
        intent.putExtra("person_id", i);

        // Retrieve user's name
        String name = intent1.getStringExtra("person_name");

        // Create Person object of user
        Person p = new Person(i, name, url);
        p.personId = i;
        p.personName = name;
        p.url = url;

        // Store user's data (their profile) into BOF database
        db.PersonDao().insertPerson(p);

        startActivity(intent);  // Start Course Activity
    }
}