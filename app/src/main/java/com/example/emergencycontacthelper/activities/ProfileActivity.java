package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvDisplayFullName, tvDisplayEmail, tvDisplayPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadUserData();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Edit Profile බොත්තම එබූ විට Edit_profile Activity එකට යාම
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, Edit_profile.class);
            startActivity(intent);
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvDisplayFullName = findViewById(R.id.tvDisplayFullName);
        tvDisplayEmail = findViewById(R.id.tvDisplayEmail);
        tvDisplayPhone = findViewById(R.id.tvDisplayPhone);
    }

    private void loadUserData() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sharedPref.getString("username", "User");
        String email = sharedPref.getString("user_email", "No Email");
        String phone = sharedPref.getString("user_phone", "+94 77 123 4567");

        tvUserName.setText(name);
        tvDisplayFullName.setText(name);
        tvDisplayEmail.setText(email);
        tvDisplayPhone.setText(phone); 
    }

    private void logoutUser() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Edit කර පැමිණි විට විස්තර refresh කිරීම
    }
}
