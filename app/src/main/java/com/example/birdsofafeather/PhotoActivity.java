package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }

    public void onClickToCourse(View view) {
        Intent intent=new Intent(this, CourseActivity.class);
        TextView text=findViewById(R.id.editTextPhoto);
        String photo=text.getText().toString();
        intent.putExtra("photo_url",photo);
        startActivity(intent);
    }
}