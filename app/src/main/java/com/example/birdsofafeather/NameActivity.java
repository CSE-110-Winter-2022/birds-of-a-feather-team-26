package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
    }

    public void onClickToPhoto(View view) {
        Intent intent = new Intent(this,PhotoActivity.class);
        TextView nameView = findViewById(R.id.name_enter);
        String name = nameView.getText().toString();
        intent.putExtra("person_name", name);
        startActivity(intent);
    }

}