package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * DESCRIPTION
 * The Picture Activity module takes in the user's picture URL for future storage in app Database
 *
 * WORKFLOW
 * The Picture Activity workflow is as follows:
 *
 * (Ivy and Huaner please fill this out)
 * (Refer to Main Activity for a template on commenting)
 * (its like pseudocode)
 *
 **/

public class PictureActivity extends AppCompatActivity {

    /**
     * This method creates the Picture Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
    }
}