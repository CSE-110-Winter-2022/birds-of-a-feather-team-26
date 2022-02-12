package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.db.AppDatabase;

import java.util.List;

public class NamePhotoDisplayActivity extends AppCompatActivity {

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_photo_display);


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<? extends IPerson> persons = db.personWithCourseDao().getAll();

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(persons);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }
}