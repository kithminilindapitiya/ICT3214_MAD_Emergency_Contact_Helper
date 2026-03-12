package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class HospitalDetailActivity extends AppCompatActivity {

    private String hospitalPhone = "123-456-789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCallHospital).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + hospitalPhone));
            startActivity(callIntent);
        });

        findViewById(R.id.btnUpdateHospital).setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateNumberActivity.class);
            intent.putExtra("service_type", "Hospital");
            intent.putExtra("current_number", hospitalPhone);
            startActivity(intent);
        });
    }
}
