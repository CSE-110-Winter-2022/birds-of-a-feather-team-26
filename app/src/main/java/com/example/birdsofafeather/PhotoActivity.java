package com.example.birdsofafeather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.databinding.ActivityMainBinding;
import com.example.birdsofafeather.databinding.ActivityPhotoBinding;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;

import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Random;

/**
 * The photo activity needs the user to input his/her url of the photo they want to upload. If the
 * user prefer not to upload, the app we design will automatically upload a default image for them.
 * After click the CONFIRM button, the user will be directed to the adding course page.
 */

public class PhotoActivity extends AppCompatActivity {

    ActivityPhotoBinding binding;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    /**
     * This method creates the Photo Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_photo);
        setContentView(binding.getRoot());

        binding.clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etURL.setText("");
                binding.urlphoto.setImageBitmap(null);
            }
        });

        binding.fetchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = binding.etURL.getText().toString();
                new FetchImage(url).start();
            }
        });
    }
    class FetchImage extends Thread {
        String URL;
        Bitmap bitmap;
        FetchImage(String URL) {
            this.URL = URL;
        }

        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(PhotoActivity.this);
                    progressDialog.setMessage("Getting your picture...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            InputStream inputStream = null;
            try {
                inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    binding.urlphoto.setImageBitmap(bitmap);
                }
            });
        }
    }


    /**
     * Start Course Activity when CONFIRM button is clicked
     * @param view
     */
    public void onClickToCourse(View view) {
        // Intent to start Course Activity
        Intent intent = new Intent(this, CourseActivity.class);
        Intent intent1 = getIntent();
        Button confirm = findViewById(R.id.confirmbtn);

        // Connect to BOF database
        AppDatabase db = AppDatabase.singleton(this.getApplicationContext());

        // Index new person_id
        // int i = new Random().nextInt(1000);
        int i = db.PersonDao().count();

        // Retrieve user's photo URL
        TextView urlView = findViewById(R.id.etURL);
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

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoActivity.this, CourseActivity.class);
                startActivity(intent);
            }
        });

        //startActivity(intent);  // Start Course Activity

    }
}