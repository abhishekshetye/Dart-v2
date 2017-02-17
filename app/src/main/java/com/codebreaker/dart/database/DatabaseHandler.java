package com.codebreaker.dart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abhishek on 2/17/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dartMessages";

    // Contacts table name
    private static final String TABLE_MESSAGES = "messages";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WHO = "who";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TYPE = "type";
    private final ArrayList<Message> contact_list = new ArrayList<>();


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_WHO + " INTEGER,"
                + KEY_MESSAGE + " TEXT," + KEY_TYPE + " TEXT" + ")";
        try {
            db.execSQL(CREATE_CONTACTS_TABLE);
        }catch(SQLException e){
            Log.d("SLIMF", "Error " + e.getMessage());
        }
        Log.d("SLIMF", "Created db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        // Create tables again
        onCreate(db);
    }




    // Adding new message
    public void addMessage (Message contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WHO, contact.getWho()); // Contact Name
        values.put(KEY_MESSAGE, contact.getMessage()); // Contact Phone
        values.put(KEY_TYPE, contact.getType()); // Contact Email
        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Message> getMessages () {
        try {
            contact_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Message contact = new Message();
                    //contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setWho(Integer.parseInt(cursor.getString(1)));
                    contact.setMessage(cursor.getString(2));
                    contact.setType(cursor.getString(3));
                    // Adding contact to list
                    contact_list.add(contact);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return contact_list;
    }

    // Getting contacts Count
    public int getCounts() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count= cursor.getCount();
        cursor.close();
        return count;
        // return count
    }

}
