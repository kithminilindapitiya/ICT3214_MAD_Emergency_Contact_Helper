package com.example.emergencycontacthelper.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencycontacthelper.ContactAdapter;
import com.example.emergencycontacthelper.EmergencyContact;
import com.example.emergencycontacthelper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // UI Components
    private TextView tvGreeting, tvUsername, tvAvatar, tvSeeAll;
    private RecyclerView rvContacts;
    private BottomNavigationView bottomNav;

    // Database & Data
    private DatabaseHelper dbHelper;
    private ContactAdapter contactAdapter;
    private List<EmergencyContact> contactList;

    // Logged-in user info
    private int loggedUserId;
    private String loggedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        loadUserSession();
        setupHeader();
        setupEmergencyButton();
        setupContactsList();
        setupQuickActionCards();
        setupBottomNavigation();

        // Handle back press using the modern API
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity(); // Exit the app
            }
        });
    }

    // ─────────────────────────────────────────────
    // 1. INITIALIZE VIEWS
    // ─────────────────────────────────────────────
    private void initViews() {
        tvGreeting   = findViewById(R.id.tvGreeting);
        tvUsername   = findViewById(R.id.tvUsername);
        tvAvatar     = findViewById(R.id.tvAvatar);
        tvSeeAll     = findViewById(R.id.tvSeeAll);
        rvContacts   = findViewById(R.id.rvContacts);
        bottomNav    = findViewById(R.id.bottomNav);
        dbHelper     = new DatabaseHelper(this);
    }

    // ─────────────────────────────────────────────
    // 2. LOAD USER FROM SharedPreferences
    // ─────────────────────────────────────────────
    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId   = sharedPref.getInt("user_id", -1);
        loggedUsername = sharedPref.getString("username", "User");

        if (loggedUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    // ─────────────────────────────────────────────
    // 3. SETUP HEADER
    // ─────────────────────────────────────────────
    private void setupHeader() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12)       greeting = "Good morning,";
        else if (hour < 17)  greeting = "Good afternoon,";
        else                 greeting = "Good evening,";

        tvGreeting.setText(greeting);
        tvUsername.setText(loggedUsername + " 👋");
        if (loggedUsername != null && !loggedUsername.isEmpty()) {
            tvAvatar.setText(String.valueOf(loggedUsername.charAt(0)).toUpperCase());
        }
    }

    // ─────────────────────────────────────────────
    // 4. EMERGENCY CALL BUTTON
    // ─────────────────────────────────────────────
    private void setupEmergencyButton() {
        findViewById(R.id.cardEmergency).setOnClickListener(v -> {
            EmergencyContact primaryContact = dbHelper.getPrimaryContact(loggedUserId);

            if (primaryContact == null) {
                Toast.makeText(this,
                        "No contacts saved yet. Please add a contact first.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("🆘 Emergency Call")
                    .setMessage("Call " + primaryContact.getName() + " at " + primaryContact.getPhone() + "?")
                    .setPositiveButton("Call Now", (dialog, which) -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + primaryContact.getPhone()));
                        startActivity(callIntent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // ─────────────────────────────────────────────
    // 5. CONTACTS RECYCLERVIEW
    // ─────────────────────────────────────────────
    private void setupContactsList() {
        contactList = dbHelper.getContactsByUser(loggedUserId);

        List<EmergencyContact> previewList = contactList.size() > 3
                ? contactList.subList(0, 3)
                : new ArrayList<>(contactList);

        contactAdapter = new ContactAdapter(this, previewList, contact -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
            startActivity(callIntent);
        });

        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactAdapter);
        rvContacts.setNestedScrollingEnabled(false);

        tvSeeAll.setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class))
        );
    }

    // ─────────────────────────────────────────────
    // 6. QUICK ACTION CARDS
    // ─────────────────────────────────────────────
    private void setupQuickActionCards() {
        findViewById(R.id.cardAmbulance).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:119")); // Ambulance number
            startActivity(callIntent);
        });

        findViewById(R.id.cardFirstAid).setOnClickListener(v ->
                startActivity(new Intent(this, AddContactActivity.class))
        );

        findViewById(R.id.cardHospital).setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class))
        );

        findViewById(R.id.cardCheckUp).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );
    }

    // ─────────────────────────────────────────────
    // 7. BOTTOM NAVIGATION
    // ─────────────────────────────────────────────
    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_contacts) {
                startActivity(new Intent(this, ViewContactsActivity.class));
                return true;
            } else if (id == R.id.nav_sos) {
                startActivity(new Intent(this, EmergencyTriggerActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupContactsList();
    }
}
