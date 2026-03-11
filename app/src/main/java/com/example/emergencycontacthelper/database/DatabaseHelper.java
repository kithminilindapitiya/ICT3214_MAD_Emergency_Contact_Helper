package com.example.emergencycontacthelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EmergencyContactHelper.db";
    private static final int DATABASE_VERSION = 2;

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

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Insert new user
    public boolean addUser(String name, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            db.execSQL(sql, new Object[]{
                    name.trim(),
                    email.trim(),
                    password.trim()
            });

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check user login
    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        Cursor cursor = db.rawQuery(query, new String[]{
                email.trim(),
                password.trim()
        });

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }
}