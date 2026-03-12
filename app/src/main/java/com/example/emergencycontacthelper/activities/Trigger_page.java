package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.R;
import com.google.android.material.button.MaterialButton;

public class Trigger_page extends AppCompatActivity {

    private MaterialButton btnBack, btnChangeContact;
    private TextView tvContactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trigger_page);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnBack = findViewById(R.id.btnBack);
        btnChangeContact = findViewById(R.id.btnChangeContact);
        tvContactPhone = findViewById(R.id.tvContactPhone);

        // Set Click Listener to go back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Navigate to Update page
        btnChangeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trigger_page.this, SOS_Contact_update.class);
                startActivity(intent);
            }
        });
        
        loadContact();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContact();
    }

    private void loadContact() {
        SharedPreferences sharedPreferences = getSharedPreferences("SOS_Prefs", MODE_PRIVATE);
        String phone = sharedPreferences.getString("sos_phone", "+94 XX XXX XXXX");
        tvContactPhone.setText(phone);
    }
}