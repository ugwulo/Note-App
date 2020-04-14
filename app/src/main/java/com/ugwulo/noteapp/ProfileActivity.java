package com.ugwulo.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button = findViewById(R.id.follow_btn);
    }

    public void onClick(View view) {
        // button.setOnClickListener();
        Toast.makeText(ProfileActivity.this, "Goan buy tools",Toast.LENGTH_SHORT).show();
    }
}
