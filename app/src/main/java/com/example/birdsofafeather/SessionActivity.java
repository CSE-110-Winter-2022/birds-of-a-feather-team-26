package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;

import java.util.List;

public class SessionActivity extends AppCompatActivity {


    protected RecyclerView favRecyclerView;
    protected RecyclerView.LayoutManager favLayoutManager;
    protected FavViewAdapter favViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        // get all favorite people
        List<Person> favPersons = db.PersonDao().getAllFav();

        favRecyclerView = findViewById(R.id.favorite_view);
        favLayoutManager = new LinearLayoutManager(this);
        favRecyclerView.setLayoutManager(favLayoutManager);

        favViewAdapter = new FavViewAdapter(favPersons);
        favRecyclerView.setAdapter(favViewAdapter);
    }

}