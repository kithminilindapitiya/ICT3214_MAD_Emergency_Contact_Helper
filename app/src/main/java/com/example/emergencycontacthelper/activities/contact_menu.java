package com.example.emergencycontacthelper.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencycontacthelper.ContactAdapter;
import com.example.emergencycontacthelper.EmergencyContact;
import com.example.emergencycontacthelper.R;

import java.util.List;

public class contact_menu extends AppCompatActivity {

    private RecyclerView rvContactMenu;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private ContactAdapter adapter;
    private int loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_menu);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        loadUserSession();
        initViews();
        loadContacts();
    }

    private void loadUserSession() {
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedUserId = sharedPref.getInt("user_id", -1);
    }

    private void initViews() {
        rvContactMenu = findViewById(R.id.rvContactMenu);
        tvEmpty = findViewById(R.id.tvEmpty);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnAddContact).setOnClickListener(v -> {
            showAddContactDialog();
        });
        
        rvContactMenu.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadContacts() {
        List<EmergencyContact> contactList = dbHelper.getContactsByUser(loggedUserId);
        
        if (contactList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvContactMenu.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvContactMenu.setVisibility(View.VISIBLE);
            
            adapter = new ContactAdapter(this, contactList, contact -> {
                showContactOptionsDialog(contact);
            });
            rvContactMenu.setAdapter(adapter);
        }
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etContactName);
        EditText etPhone = dialogView.findViewById(R.id.etContactPhone);
        EditText etRelation = dialogView.findViewById(R.id.etContactRelation);
        View btnSave = dialogView.findViewById(R.id.btnSave);
        View btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String relation = etRelation.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || relation.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.addEmergencyContact(loggedUserId, name, phone, relation, 0);
                if (success) {
                    Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                    loadContacts();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showContactOptionsDialog(EmergencyContact contact) {
        String[] options = {"Call", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contact.getName());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) { // Call
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getPhone()));
                startActivity(intent);
            } else if (which == 1) { // Delete
                showDeleteConfirmation(contact);
            }
        });
        builder.show();
    }

    private void showDeleteConfirmation(EmergencyContact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (dbHelper.deleteContact(contact.getId())) {
                        Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
                        loadContacts();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }
}