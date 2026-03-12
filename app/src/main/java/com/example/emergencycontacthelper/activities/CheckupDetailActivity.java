package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.R;

public class CheckupDetailActivity extends AppCompatActivity {

    private String checkupPhone = "+1 800 222 333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCall).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + checkupPhone));
            startActivity(callIntent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            startActivity(new Intent(this, AddContactActivity.class));
        });
    }
}
