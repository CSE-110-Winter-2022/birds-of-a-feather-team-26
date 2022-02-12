package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;

import java.util.Random;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }

    public void onClickToCourse(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        Intent intent1 = getIntent();

        int i = new Random().nextInt(1000);

        TextView urlView = findViewById(R.id.editTextPhoto);
        String url = urlView.getText().toString();
        intent.putExtra("photo_url",url);
        intent.putExtra("person_id", i);

        AppDatabase db = AppDatabase.singleton(this.getApplicationContext());
        String name = intent1.getStringExtra("person_name");

        Person p = new Person(i, name, url);
        p.personId = i;
        p.personName = name;
        p.url = url;

        db.PersonDao().insertPerson(p);
        startActivity(intent);
    }
}