package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class QuickActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_actions);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Setup Hospital Card to navigate to detailed page
        findViewById(R.id.cardHospital).setOnClickListener(v -> 
            startActivity(new Intent(this, HospitalDetailActivity.class)));

        // Other cards can use the detailed pages you previously had, or similar logic
        findViewById(R.id.cardAmbulance).setOnClickListener(v -> 
            startActivity(new Intent(this, AmbulanceDetailActivity.class)));

        findViewById(R.id.cardFirstAid).setOnClickListener(v -> 
            startActivity(new Intent(this, FirstAidDetailActivity.class)));

        findViewById(R.id.cardCheckUp).setOnClickListener(v -> 
            startActivity(new Intent(this, CheckupDetailActivity.class)));
    }
}
