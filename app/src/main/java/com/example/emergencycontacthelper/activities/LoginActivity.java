package com.example.emergencycontacthelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencycontacthelper.R;
import com.example.emergencycontacthelper.database.DatabaseHelper;
import com.example.emergencycontacthelper.utils.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        boolean isValid = db.checkUser(email, hashedPassword);

        if (isValid) {

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            Intent intent =
                    new Intent(LoginActivity.this, HomeActivity.class);

            intent.putExtra("email", email);

            startActivity(intent);

            finish();

        } else {

            Toast.makeText(this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT).show();
        }
    }
}