package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class FirstAidDetailActivity extends AppCompatActivity {

    private String firstAidPhone = "+1 555 911 000"; // Default
    private DatabaseHelper dbHelper;
    private int loggedUserId;
    private TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_detail);

        dbHelper = new DatabaseHelper(this);
        loadUserSession();

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        
        loadSavedNumber();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCall).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + firstAidPhone));
            startActivity(callIntent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateNumberActivity.class);
            intent.putExtra("service_type", "First Aid");
            intent.putExtra("current_number", firstAidPhone);
            startActivity(intent);
        });
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void loadSavedNumber() {
        firstAidPhone = dbHelper.getServicePhone(loggedUserId, "First Aid", "+1 555 911 000");
        tvPhoneNumber.setText(firstAidPhone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedNumber(); // Refresh the number when returning from UpdateNumberActivity
    }
}
