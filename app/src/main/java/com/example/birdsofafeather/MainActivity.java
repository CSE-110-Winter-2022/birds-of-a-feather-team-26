package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.databinding.ActivityMainBinding;
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
    
    private static final String TAG = "BirdsOfAFeather";

    private static final String[] REQUIRED_PERMISSIONS;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    };
        }
    }

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    
    
    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    /** Returns true if the app was granted all the permissions. Otherwise, returns false. */
    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        int i = 0;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Log.i(TAG, "Failed to request the permission " + permissions[i]);
                Toast.makeText(this, "Missing Permissions! ", Toast.LENGTH_LONG).show();
                return;
            }
            i++;
        }
    }
    
    

    /**
     * This method creates the Main Activity
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
        else {
            Intent intentHomeActivityWorkflow = new Intent(this, HomeActivity.class);
            startActivity(intentHomeActivityWorkflow);
        }
    }

    /**
     * TEMPORARY BUTTON TO START HOME ACTIVITY WORKFLOW
     */
    public void onHomeActivityClicked(View view) {
        Intent intentHomeActivityWorkflow = new Intent(this, HomeActivity.class);
        startActivity(intentHomeActivityWorkflow);
    }
}
