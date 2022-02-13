package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Person;
import com.example.birdsofafeather.model.db.PersonWithCourseDao;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
    }

//    public void onClickToPhoto(View view) {
//        Utilities.showAlert(NameActivity.this,"Enter your name");
//        Intent intent = new Intent(this,PhotoActivity.class);
//        TextView nameView = findViewById(R.id.name_enter);
//        String name = nameView.getText().toString();
//        if(name.equals("") || nameView.getText().length() == 0){
//            Utilities.showAlert(NameActivity.this,"Enter your name");
//            return;
//        }
//        intent.putExtra("person_name", name);
//        startActivity(intent);
//    }

    public void onClickToPhoto(View view) {
        Intent intent = new Intent(this,PhotoActivity.class);
        TextView nameView = findViewById(R.id.name_enter);
        String name = nameView.getText().toString();
        if(name.equals("")||nameView.getText().length()==0){
            Utilities.showAlert(NameActivity.this,"Enter your name");
            return;
        }
        // String name = nameView.getText().toString();
        intent.putExtra("person_name", name);
        startActivity(intent);
    }

}