package com.example.mdp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencycontacthelper.ContactAdapter;
import com.example.emergencycontacthelper.EmergencyContact;
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

    // Logged-in user info (from SharedPreferences)
    private int loggedUserId;
    private String loggedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity);

        initViews();
        loadUserSession();
        setupHeader();
        setupEmergencyButton();
        setupContactsList();
        setupQuickActionCards();
        setupBottomNavigation();
    }

    // ─────────────────────────────────────────────
    // 1. INITIALIZE VIEWS
    // ─────────────────────────────────────────────
    private void initViews() {
        //tvGreeting   = findViewById(R.id.tvGreeting);
        //tvUsername   = findViewById(R.id.tvUsername);
        //tvAvatar     = findViewById(R.id.tvAvatar);
        //tvSeeAll     = findViewById(R.id.tvSeeAll);
        //rvContacts   = findViewById(R.id.rvContacts);
        //bottomNav    = findViewById(R.id.bottomNav);
        //dbHelper     = new DatabaseHelper(this);
    }

    // ─────────────────────────────────────────────
    // 2. LOAD USER FROM SharedPreferences
    // ─────────────────────────────────────────────
    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId   = sharedPref.getInt("user_id", -1);
        loggedUsername = sharedPref.getString("username", "User");

        // If no session, redirect to Login
        if (loggedUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    // ─────────────────────────────────────────────
    // 3. SETUP HEADER (Greeting + Avatar)
    // ─────────────────────────────────────────────
    private void setupHeader() {
        // Dynamic greeting based on time of day
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12)       greeting = "Good morning,";
        else if (hour < 17)  greeting = "Good afternoon,";
        else                 greeting = "Good evening,";

        tvGreeting.setText(greeting);
        tvUsername.setText(loggedUsername + " 👋");

        // Avatar = first letter of username
        tvAvatar.setText(String.valueOf(loggedUsername.charAt(0)).toUpperCase());
    }

    // ─────────────────────────────────────────────
    // 4. EMERGENCY CALL BUTTON
    // ─────────────────────────────────────────────
    private <EmergencyContact> void setupEmergencyButton() {
       findViewById(R.id.cardEmergency).setOnClickListener(v -> {
            // Fetch the primary contact from DB
            com.example.emergencycontacthelper.EmergencyContact primaryContact = dbHelper.getPrimaryContact(loggedUserId);

            if (primaryContact == null) {
                Toast.makeText(this,
                        "No contacts saved yet. Please add an emergency contact first.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Show confirmation dialog before calling
            new AlertDialog.Builder(this)
                    .setTitle("🆘 Emergency Call")
                    .setMessage("Call " + primaryContact.getClass() + " at " + primaryContact.getClass() + "?")
                    .setPositiveButton("Call Now", (dialog, which) -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + primaryContact.getClass()));
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
        // Fetch contacts from SQLite filtered by logged-in user
        contactList = dbHelper.getContactsByUser(loggedUserId);

        // Show only first 3 as preview on Home
        List<EmergencyContact> previewList = contactList.size() > 3
                ? contactList.subList(0, 3)
                : new ArrayList<>(contactList);

        contactAdapter = new ContactAdapter(this, previewList, contact -> {
            // Quick call on contact row tap
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
            startActivity(callIntent);
        });

        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactAdapter);
        rvContacts.setNestedScrollingEnabled(false);

        // "See All" button
        tvSeeAll.setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class))
        );
    }

    // ─────────────────────────────────────────────
    // 6. QUICK ACTION CARDS
    // ─────────────────────────────────────────────
    private void setupQuickActionCards() {
        // Add Contact card
        findViewById(R.id.nav_contacts).setOnClickListener(v ->
                startActivity(new Intent(this, AddContactActivity.class))
        );

        // View All card
        findViewById(R.id.cardViewAll).setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class))
        );

        // Edit Contact card
        findViewById(R.id.nav_contacts).setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class)) // Edit from the list
        );

        // Settings card
        findViewById(R.id.nav_sos).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );
    }

    // ─────────────────────────────────────────────
    // 7. BOTTOM NAVIGATION
    // ─────────────────────────────────────────────
    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_home); // Home is active by default

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true; // Already here

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

    // ─────────────────────────────────────────────
    // 8. REFRESH CONTACTS when returning to Home
    // ─────────────────────────────────────────────
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh contact list in case user added/deleted one
        setupContactsList();
    }

    // ─────────────────────────────────────────────
    // 9. PREVENT GOING BACK TO LOGIN
    // ─────────────────────────────────────────────
    //@Override
    //public void onBackPressed() {
        // Do nothing — user must use Logout to leave
        // You can show a dialog here if you want
    }
//}