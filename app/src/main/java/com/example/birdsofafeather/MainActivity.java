package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;

/**
 * DESCRIPTION
 * The Main Activity module acts as the "application launch pad"
 *
 * WORKFLOW
 * Main Activity workflow is as follows:
 *
 * A. If this is the user's first time using the app
 *      - Launch the User Information workflow
 *          - Start Bluetooth Activity (bluetooth permission is remembered)
 *          - Do Name Activity
 *          - Do Picture Activity
 *          - Do Course Activity
 *          - Finish & Return to Main Activity to start Home Activity
 *
 * B. Launch the Birds of a Feather Home Activity workflow
 *      - Start Home Activity
 *          - LIST THUMBNAILS OF STUDENTS WHO HAVE TAKEN THE SAME CLASSES AS USER
 *          - Clicking on thumbnails of students launches Profile Activity
 *              - Profile Activity
 *                  - displays UI of Student name, picture, courses taken
 *          - SAVES STUDENT INFORMATION INTO DATABASE
 *          - STOP BUTTON STOPS HOME ACTIVITY SEARCH FOR STUDENTS FUNCTION
 *
 **/

public class MainActivity extends AppCompatActivity {

    /**
     * This method creates the Main Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * A. USER INFORMATION WORKFLOW
         * */
        // Connect to BOF database
        AppDatabase db = AppDatabase.singleton(this.getApplicationContext());

        // If there is no Person data stored in the database, start User Information Workflow
        if (db.PersonDao().count() < 1) {
            Intent intentUserInformationWorkflow = new Intent(this, BluetoothActivity.class);
            startActivity(intentUserInformationWorkflow);
        }

        /**
         * B. HOME ACTIVITY WORKFLOW
         * */
        Intent intentHomeActivityWorkflow = new Intent(this, HomeActivity.class);
        startActivity(intentHomeActivityWorkflow);
    }

//    /**
//     * TEMPORARY BUTTON TO START USER INFORMATION WORKFLOW
//     * @param view
//     */
//    public void onUserInformationClicked(View view) {
//        Intent intentUserInformationWorkflow = new Intent(this, BluetoothActivity.class);
//        startActivity(intentUserInformationWorkflow);
//    }

    /**
     * TEMPORARY BUTTON TO START HOME ACTIVITY WORKFLOW
     * @param view
     */
    public void onHomeActivityClicked(View view) {
        Intent intentHomeActivityWorkflow = new Intent(this, HomeActivity.class);
        startActivity(intentHomeActivityWorkflow);
    }
}