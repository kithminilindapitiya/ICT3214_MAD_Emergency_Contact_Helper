package com.example.emergencycontacthelper.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.R;

public class Edit_profile extends AppCompatActivity {

    private EditText etFullName, etPhone, etEmail;
    private DatabaseHelper dbHelper;
    private int loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserSession();
        loadCurrentData();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> {
            saveProfileChanges();
        });
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void loadCurrentData() {
        if (loggedUserId == -1) return;

        Cursor cursor = dbHelper.getUserData(loggedUserId);
        if (cursor != null && cursor.moveToFirst()) {
            etFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            cursor.close();
        }
    }

    private void saveProfileChanges() {
        String newName = etFullName.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Update Database
        boolean isUpdated = dbHelper.updateUserProfile(loggedUserId, newName, newEmail, newPhone);

        if (isUpdated) {
            // 2. Update SharedPreferences Session
            SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", newName);
            editor.putString("user_email", newEmail);
            editor.putString("user_phone", newPhone);
            editor.apply();

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to Profile page
        } else {
            Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
