package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * DESCRIPTION
 * The Bluetooth Activity module fetches bluetooth permissions from the user's Android device
 *
 * WORKFLOW
 * The Bluetooth Activity workflow is as follows:
 *
 * (Derek and Vishvesh please fill this out)
 * (Refer to Main Activity for a template on commenting)
 * (its like pseudocode)
 *
 **/

public class BluetoothActivity extends AppCompatActivity {

    /**
     * This method creates the Bluetooth Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }

    /**
     * This method starts Name Activity when start_name_activity_btn is clicked
     *
     * @param view
     */
    public void onStartNameActivityClicked(View view) {
        Intent intentNameActivity = new Intent(this, NameActivity.class);
        startActivity(intentNameActivity);
    }
}