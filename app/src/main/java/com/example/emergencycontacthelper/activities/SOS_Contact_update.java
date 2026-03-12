package com.example.emergencycontacthelper.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.EmergencyContact;
import com.example.emergencycontacthelper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SOS_Contact_update extends AppCompatActivity {

    private MaterialButton btnBack, btnSaveContact;
    private TextInputEditText etContactName, etContactPhone;
    private DatabaseHelper dbHelper;
    private int loggedUserId;
    private EmergencyContact primaryContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sos_contact_update);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        loadUserSession();
        initViews();
        loadExistingContact();
        setupListeners();
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSaveContact = findViewById(R.id.btnSaveContact);
        etContactName = findViewById(R.id.etContactName);
        etContactPhone = findViewById(R.id.etContactPhone);
    }

    private void loadExistingContact() {
        primaryContact = dbHelper.getPrimaryContact(loggedUserId);
        if (primaryContact != null) {
            etContactName.setText(primaryContact.getName());
            etContactPhone.setText(primaryContact.getPhone());
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSaveContact.setOnClickListener(v -> {
            String name = etContactName.getText().toString().trim();
            String phone = etContactPhone.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (primaryContact != null) {
                // Update existing primary contact
                boolean updated = dbHelper.updateContact(primaryContact.getId(), name, phone);
                if (updated) {
                    Toast.makeText(this, "Contact Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add as new primary contact if none exists
                boolean added = dbHelper.addEmergencyContact(loggedUserId, name, phone, "Emergency", 1);
                if (added) {
                    Toast.makeText(this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}