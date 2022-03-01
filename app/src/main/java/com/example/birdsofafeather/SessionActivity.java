package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.birdsofafeather.model.IPerson;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonDao;

import java.util.List;

public class SessionActivity extends AppCompatActivity {


    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        // get all favorite people
<<<<<<< HEAD
        List<Person> persons = db.PersonDao().getAllPersons();
        List<Person> favPersons = null;
        for(Person person: persons){
            if (personDao.isFav(person.personId) == true){
                favPersons.add(person);
            }
=======
        List<Person> favPersons = db.PersonDao().getAllFav();
>>>>>>> 76d386b4e74688831f11578c16f030674caafc5c

        personsRecyclerView = findViewById(R.id.favorite_view);
        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(favPersons);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }




}