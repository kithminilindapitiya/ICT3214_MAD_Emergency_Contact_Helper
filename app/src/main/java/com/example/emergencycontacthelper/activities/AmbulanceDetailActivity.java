package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class AmbulanceDetailActivity extends AppCompatActivity {

    private String ambulancePhone = "999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_detail);

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
}
