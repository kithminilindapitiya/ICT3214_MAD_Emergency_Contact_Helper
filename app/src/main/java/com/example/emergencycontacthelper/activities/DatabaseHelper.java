package com.example.emergencycontacthelper.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.example.emergencycontacthelper.EmergencyContact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EmergencyContactHelper.db";
    private static final int DATABASE_VERSION = 3; // Incremented version for new table

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";

        String CREATE_CONTACTS_TABLE = "CREATE TABLE emergency_contacts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT, " +
                "phone TEXT, " +
                "relation TEXT, " +
                "is_primary INTEGER DEFAULT 0, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";

        // New table for service contacts (Ambulance, Police, etc.)
        String CREATE_SERVICE_CONTACTS_TABLE = "CREATE TABLE service_contacts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "service_name TEXT, " +
                "phone TEXT, " +
                "UNIQUE(user_id, service_name), " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_SERVICE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            String CREATE_SERVICE_CONTACTS_TABLE = "CREATE TABLE service_contacts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "service_name TEXT, " +
                    "phone TEXT, " +
                    "UNIQUE(user_id, service_name), " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            db.execSQL(CREATE_SERVICE_CONTACTS_TABLE);
        } else {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS emergency_contacts");
            db.execSQL("DROP TABLE IF EXISTS service_contacts");
            onCreate(db);
        }
    }

    // ─────────────────────────────────────
    // Add New User
    // ─────────────────────────────────────
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    // ─────────────────────────────────────
    // Check User Login
    // ─────────────────────────────────────
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // ─────────────────────────────────────
    // Get User ID by Email
    // ─────────────────────────────────────
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    // ─────────────────────────────────────
    // Get Username by Email
    // ─────────────────────────────────────
    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            cursor.close();
            return username;
        }
        cursor.close();
        return "User";
    }

    // ─────────────────────────────────────
    // Get Primary Emergency Contact
    // ─────────────────────────────────────
    public EmergencyContact getPrimaryContact(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("emergency_contacts", null, "user_id = ? AND is_primary = 1",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            EmergencyContact contact = new EmergencyContact(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            );
            contact.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            contact.setRelation(cursor.getString(cursor.getColumnIndexOrThrow("relation")));
            cursor.close();
            return contact;
        }
        if (cursor != null) cursor.close();
        List<EmergencyContact> contacts = getContactsByUser(userId);
        return contacts.isEmpty() ? null : contacts.get(0);
    }

    // ─────────────────────────────────────
    // Get All Contacts by User
    // ─────────────────────────────────────
    public List<EmergencyContact> getContactsByUser(int userId) {
        List<EmergencyContact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("emergency_contacts", null, "user_id = ?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                EmergencyContact contact = new EmergencyContact(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                );
                contact.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                contact.setRelation(cursor.getString(cursor.getColumnIndexOrThrow("relation")));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    // ─────────────────────────────────────
    // Update Contact Phone Number
    // ─────────────────────────────────────
    public boolean updateContactPhone(int contactId, String newPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", newPhone);
        int result = db.update("emergency_contacts", values, "id = ?", new String[]{String.valueOf(contactId)});
        return result > 0;
    }

    // ─────────────────────────────────────
    // Service Contacts Methods
    // ─────────────────────────────────────
    
    public String getServicePhone(int userId, String serviceName, String defaultPhone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("service_contacts", new String[]{"phone"}, "user_id = ? AND service_name = ?",
                new String[]{String.valueOf(userId), serviceName}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            String phone = cursor.getString(0);
            cursor.close();
            return phone;
        }
        if (cursor != null) cursor.close();
        return defaultPhone;
    }

    public boolean updateServicePhone(int userId, String serviceName, String newPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("service_name", serviceName);
        values.put("phone", newPhone);
        
        long result = db.replace("service_contacts", null, values);
        return result != -1;
    }

    // ─────────────────────────────────────
    // Add Emergency Contact
    // ─────────────────────────────────────
    public boolean addEmergencyContact(int userId, String name, String phone, String relation, int isPrimary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("name", name);
        values.put("phone", phone);
        values.put("relation", relation);
        values.put("is_primary", isPrimary);
        long result = db.insert("emergency_contacts", null, values);
        return result != -1;
    }
}
