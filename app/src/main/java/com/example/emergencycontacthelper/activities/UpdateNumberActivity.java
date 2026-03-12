package com.example.emergencycontacthelper.activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_number);

        tvUpdateTitle = findViewById(R.id.tvUpdateTitle);
        tvCurrentNumber = findViewById(R.id.tvCurrentNumber);
        etNewNumber = findViewById(R.id.etNewNumber);

        serviceType = getIntent().getStringExtra("service_type");
        String currentNumber = getIntent().getStringExtra("current_number");

        if (serviceType != null) {
            tvUpdateTitle.setText("Update " + serviceType + " Number");
        }
        if (currentNumber != null) {
            tvCurrentNumber.setText(currentNumber);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnSaveNumber).setOnClickListener(v -> {
            String newNumber = etNewNumber.getText().toString().trim();
            if (newNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a new number", Toast.LENGTH_SHORT).show();
            } else {
                // Here you would typically save to Database or SharedPreferences
                Toast.makeText(this, serviceType + " number updated to: " + newNumber, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
