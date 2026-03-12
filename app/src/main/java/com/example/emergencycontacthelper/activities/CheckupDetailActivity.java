package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class CheckupDetailActivity extends AppCompatActivity {

    private String policePhone = "119"; // Default
    private DatabaseHelper dbHelper;
    private int loggedUserId;
    private TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Using activity_police_detail layout as requested for Police information
        setContentView(R.layout.activity_police_detail);

        dbHelper = new DatabaseHelper(this);
        loadUserSession();

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        
        loadSavedNumber();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCall).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + policePhone));
            startActivity(callIntent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateNumberActivity.class);
            intent.putExtra("service_type", "Police");
            intent.putExtra("current_number", policePhone);
            startActivity(intent);
        });
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void loadSavedNumber() {
        // Load saved police number or use default "119"
        policePhone = dbHelper.getServicePhone(loggedUserId, "Police", "119");
        if (tvPhoneNumber != null) {
            tvPhoneNumber.setText(policePhone);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedNumber(); // Refresh the number when returning from UpdateNumberActivity
    }
}
