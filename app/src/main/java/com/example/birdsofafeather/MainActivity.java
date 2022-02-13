package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
 *          - Do Add Course Activity
 *          - Do Edit Course Activity
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
    }

    public void onClick(View view) {
        Intent intent=new Intent(this,NameActivity.class);
        startActivity(intent);


        /**
         * A. USER INFORMATION WORKFLOW
         * */



        /**
         * B. HOME ACTIVITY WORKFLOW
         * */

    }

    /**
     * TEMPORARY BUTTON TO START USER INFORMATION WORKFLOW
     * @param view
     */
    public void onUserInformationClicked(View view) {
        Intent intentUserInformationWorkflow = new Intent(this, BluetoothActivity.class);
        startActivity(intentUserInformationWorkflow);
    }

    /**
     * TEMPORARY BUTTON TO START HOME ACTIVITY WORKFLOW
     * @param view
     */
    public void onHomeActivityClicked(View view) {
        Intent intentHomeActivityWorkflow = new Intent(this, HomeActivity.class);
        startActivity(intentHomeActivityWorkflow);
    }
}