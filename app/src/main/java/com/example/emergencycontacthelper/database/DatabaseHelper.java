package com.example.emergencycontacthelper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "emergency.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Users table
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "email TEXT UNIQUE," +
                        "password TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    // ✅ Add user
    public boolean addUser(String name, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL(
                    "INSERT INTO users(name,email,password) VALUES(?,?,?)",
                    new Object[]{name, email, password}
            );
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ✅ CHECK USER LOGIN (THIS WAS MISSING)
    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }
}