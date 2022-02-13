package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * DESCRIPTION
 * The Photo Activity module takes in the user's picture URL for future storage in app Database
 *
 * WORKFLOW
 * The Photo Activity workflow is as follows:
 *
 * (Ivy and Huaner please fill this out)
 * (Refer to Main Activity for a template on commenting)
 * (its like pseudocode)
 *
 **/

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
}