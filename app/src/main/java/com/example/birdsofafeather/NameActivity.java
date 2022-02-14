package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonWithCourseDao;

/**
 * The name activity lets user enter their name, and navigates to the next photo activity by clicking
 * the CONFIRM button.
 * If the user does not enter their name, then when they click the button, there will be an alert
 * message reminding them to input the name.
 */

public class NameActivity extends AppCompatActivity {

    /**
     * This method creates the Name Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
    }

    /**
     * Start Photo Activity when CONFIRM button is clicked
     * @param view
     */
    public void onClickToPhoto(View view) {
        // Intent to start Photo Activity
        Intent intent = new Intent(this,PhotoActivity.class);

        // Retrieve user's name
        TextView nameView = findViewById(R.id.name_enter);
        String name = nameView.getText().toString();

        // If user does not enter name, raise alert
        if(name.equals("")||nameView.getText().length()==0){
            Utilities.showAlert(NameActivity.this,"Enter your name");
            return;
        }

        // Pass user's name onto Photo Activity
        intent.putExtra("person_name", name);
        startActivity(intent);  // Start Photo Activity
    }
}