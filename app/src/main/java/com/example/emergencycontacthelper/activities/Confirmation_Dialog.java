package com.example.emergencycontacthelper.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergencycontacthelper.R;
import com.google.android.material.button.MaterialButton;

public class Confirmation_Dialog extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String contactName, contactPhone;
    private MaterialButton btnConfirm, btnCancel;
    private ImageButton btnClose;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation_dialog);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get data from intent
        contactName = getIntent().getStringExtra("contact_name");
        contactPhone = getIntent().getStringExtra("contact_phone");

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        btnClose = findViewById(R.id.btnClose);
        tvDescription = findViewById(R.id.tvDescription);

        if (contactName != null) {
            tvDescription.setText("You are about initiate an emergency call to " + contactName + ". Are you sure you wish to continue?");
        }
    }

    private void setupListeners() {
        // Confirm Button -> Make Call
        btnConfirm.setOnClickListener(v -> makePhoneCall());

        // Cancel Button -> Go Back
        btnCancel.setOnClickListener(v -> finish());

        // Close Button -> Go Back
        btnClose.setOnClickListener(v -> finish());
    }

    private void makePhoneCall() {
        if (contactPhone != null && !contactPhone.trim().isEmpty()) {
            if (ContextCompat.checkSelfPermission(Confirmation_Dialog.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Confirmation_Dialog.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                String dial = "tel:" + contactPhone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                finish(); // Close dialog after starting call
            }
        } else {
            Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}