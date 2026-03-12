package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.EmergencyContact;
import com.example.emergencycontacthelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class EmergencyTriggerActivity extends AppCompatActivity {

    private MaterialButton btnBack, btnChangeContact;
    private MaterialCardView btnSosCall;
    private TextView tvContactPhone;
    private DatabaseHelper dbHelper;
    private int loggedUserId;
    private EmergencyContact primaryContact;

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

        dbHelper = new DatabaseHelper(this);
        loadUserSession();
        initViews();
        loadPrimaryContact();
        setupListeners();
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSosCall = findViewById(R.id.btnSosCall);
        btnChangeContact = findViewById(R.id.btnChangeContact);
        tvContactPhone = findViewById(R.id.tvContactPhone);
    }

    private void loadPrimaryContact() {
        primaryContact = dbHelper.getPrimaryContact(loggedUserId);
        if (primaryContact != null) {
            tvContactPhone.setText(primaryContact.getPhone());
        } else {
            tvContactPhone.setText("No Contact Selected");
        }
    }

    private void setupListeners() {
        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        // SOS Button -> Go to Confirmation Dialog
        if (btnSosCall != null) {
            btnSosCall.setOnClickListener(v -> {
                primaryContact = dbHelper.getPrimaryContact(loggedUserId); // Refresh data
                if (primaryContact != null) {
                    Intent intent = new Intent(EmergencyTriggerActivity.this, Confirmation_Dialog.class);
                    intent.putExtra("contact_name", primaryContact.getName());
                    intent.putExtra("contact_phone", primaryContact.getPhone());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please select an emergency contact first", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Change Contact Button -> Go to UpdateNumberActivity
        if (btnChangeContact != null) {
            btnChangeContact.setOnClickListener(v -> {
                Intent intent = new Intent(EmergencyTriggerActivity.this, UpdateNumberActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrimaryContact();
    }
}
