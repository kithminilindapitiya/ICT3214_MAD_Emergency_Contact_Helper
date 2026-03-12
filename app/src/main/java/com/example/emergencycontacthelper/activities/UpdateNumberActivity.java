package com.example.emergencycontacthelper.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emergencycontacthelper.EmergencyContact;
import com.example.emergencycontacthelper.R;

public class UpdateNumberActivity extends AppCompatActivity {

    private TextView tvUpdateTitle, tvCurrentNumber;
    private EditText etNewNumber;
    private String serviceType;
    private DatabaseHelper dbHelper;
    private int loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_number);

        dbHelper = new DatabaseHelper(this);
        loadUserSession();

        tvUpdateTitle = findViewById(R.id.tvUpdateTitle);
        tvCurrentNumber = findViewById(R.id.tvCurrentNumber);
        etNewNumber = findViewById(R.id.etNewNumber);

        serviceType = getIntent().getStringExtra("service_type");
        
        loadCurrentContact();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnSaveNumber).setOnClickListener(v -> {
            String newNumber = etNewNumber.getText().toString().trim();
            if (newNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a new number", Toast.LENGTH_SHORT).show();
            } else {
                saveNewNumber(newNumber);
            }
        });
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void loadCurrentContact() {
        EmergencyContact primaryContact = dbHelper.getPrimaryContact(loggedUserId);
        if (primaryContact != null) {
            tvCurrentNumber.setText(primaryContact.getPhone());
        } else {
            tvCurrentNumber.setText("No primary contact");
        }
    }

    private void saveNewNumber(String newNumber) {
        // Here we update the primary contact's number in the database
        EmergencyContact primaryContact = dbHelper.getPrimaryContact(loggedUserId);
        
        if (primaryContact != null) {
            // If exists, update
            boolean success = dbHelper.updateContactPhone(primaryContact.getId(), newNumber);
            if (success) {
                Toast.makeText(this, "Number updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If no primary contact exists, create a default one
            boolean success = dbHelper.addEmergencyContact(loggedUserId, "Primary Contact", newNumber, "Emergency", 1);
            if (success) {
                Toast.makeText(this, "Primary contact added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
            }
        }
    }
}