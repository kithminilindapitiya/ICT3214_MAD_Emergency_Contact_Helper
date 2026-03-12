package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencycontacthelper.R;
import com.example.emergencycontacthelper.utils.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user already logged in
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        if (sharedPref.getInt("user_id", -1) != -1) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Go to Register page
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        // Login button click
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash the password before checking
        String hashedPassword = PasswordUtils.hashPassword(password);

        boolean isValid = db.checkUser(email, hashedPassword);

        if (isValid) {

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            // Save user session
            SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("user_id", db.getUserIdByEmail(email));
            editor.putString("username", db.getUsernameByEmail(email));

            editor.apply();

            // Go to Home page
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        } else {

            Toast.makeText(this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT).show();
        }
    }
}