package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class AmbulanceDetailActivity extends AppCompatActivity {

    private String ambulancePhone = "999"; // Default
    private DatabaseHelper dbHelper;
    private int loggedUserId;
    private TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_detail);

        dbHelper = new DatabaseHelper(this);
        loadUserSession();

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        
        loadSavedNumber();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCall).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + ambulancePhone));
            startActivity(callIntent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateNumberActivity.class);
            intent.putExtra("service_type", "Ambulance");
            intent.putExtra("current_number", ambulancePhone);
            startActivity(intent);
        });
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void loadSavedNumber() {
        ambulancePhone = dbHelper.getServicePhone(loggedUserId, "Ambulance", "999");
        tvPhoneNumber.setText(ambulancePhone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedNumber(); // Reload number if updated in UpdateNumberActivity
    }
}
