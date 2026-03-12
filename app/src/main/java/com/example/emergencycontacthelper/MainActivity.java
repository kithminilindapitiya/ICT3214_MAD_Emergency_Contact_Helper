package com.example.emergencycontacthelper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencycontacthelper.activities.LoginActivity;
import com.example.emergencycontacthelper.activities.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DatabaseHelper(this);

        btnGetStarted = findViewById(R.id.btnGoToLogin);

        btnGetStarted.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

        });
    }
}