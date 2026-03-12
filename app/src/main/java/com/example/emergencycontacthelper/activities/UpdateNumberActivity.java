package com.example.emergencycontacthelper.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
        String defaultPhone = getIntent().getStringExtra("current_number");
        
        // Load the saved number from the new database table
        String currentSavedNumber = dbHelper.getServicePhone(loggedUserId, serviceType, defaultPhone);
        tvCurrentNumber.setText(currentSavedNumber);

        if (serviceType != null) {
            tvUpdateTitle.setText("Update " + serviceType + " Number");
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnSaveNumber).setOnClickListener(v -> {
            String newNumber = etNewNumber.getText().toString().trim();
            if (newNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a new number", Toast.LENGTH_SHORT).show();
            } else {
                updateServiceNumber(newNumber);
            }
        });
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void updateServiceNumber(String newNumber) {
        boolean success = dbHelper.updateServicePhone(loggedUserId, serviceType, newNumber);
        if (success) {
            Toast.makeText(this, serviceType + " number updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update number", Toast.LENGTH_SHORT).show();
        }
    }
}
