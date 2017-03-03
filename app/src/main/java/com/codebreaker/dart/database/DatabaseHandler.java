package com.codebreaker.dart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    //interests table
    // Contacts table name
    private static final String TABLE_INTERESTS = "userInterests";

    // Contacts Table Columns names
    private static final String KEY_IID = "id";
    private static final String KEY_INTEREST = "interest";
    private static final String KEY_COUNT = "count";
    private static final String KEY_ISINTEREST = "isInterest";
    private static final String KEY_ISINDB = "isInDB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_WHO + " INTEGER,"
                + KEY_MESSAGE + " TEXT," + KEY_TYPE + " TEXT" + ")";

        String CREATE_INTERESTS_TABLE = "CREATE TABLE " + TABLE_INTERESTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_INTEREST + " TEXT," + KEY_COUNT + " INTEGER," +
                KEY_ISINTEREST + " INTEGER," + KEY_ISINDB + " INTEGER" + ")";

        try {
            db.execSQL(CREATE_CONTACTS_TABLE);
            db.execSQL(CREATE_INTERESTS_TABLE);
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


    // for interests table

    // Adding new message
    public void addInterest (String interest) {

        //check if the same interest exists
        String selectQuery = "SELECT  * FROM " + TABLE_INTERESTS + " WHERE " + KEY_INTEREST + " =  \"" + interest + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorz = db.rawQuery(selectQuery, null);
        int length = cursorz.getCount();
        if(length != 0){
            //exists
            String strSQL = "UPDATE " + TABLE_INTERESTS  + " SET " + KEY_COUNT  + " = " + KEY_COUNT + " + 1  WHERE " + KEY_INTEREST + " =  \""+ interest + "\"";
            db.execSQL(strSQL);
            Log.d("SLIMF", "interest updated");
        }else {

            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_COUNT, 1);
            values.put(KEY_INTEREST, interest);
            values.put(KEY_ISINDB, 0);
            values.put(KEY_ISINTEREST, 0);
            // Inserting Row
            db.insert(TABLE_INTERESTS, null, values);
            Log.d("SLIMF", "interest added");
        }

        db.close();
    }


    public void addInterest (String interest, int number) {

        //check if the same interest exists
        String selectQuery = "SELECT  * FROM " + TABLE_INTERESTS + " WHERE " + KEY_INTEREST + " =  \"" + interest + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorz = db.rawQuery(selectQuery, null);
        int length = cursorz.getCount();
        if(length != 0){
            //exists
            String strSQL = "UPDATE " + TABLE_INTERESTS  + " SET " + KEY_COUNT  + " = " + KEY_COUNT + " + " + number + "  WHERE " + KEY_INTEREST + " =  \""+ interest + "\"";
            db.execSQL(strSQL);
            Log.d("SLIMF", "interest updated");
        }else {

            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_COUNT, number);
            values.put(KEY_INTEREST, interest);
            values.put(KEY_ISINDB, 0);
            values.put(KEY_ISINTEREST, 0);
            // Inserting Row
            db.insert(TABLE_INTERESTS, null, values);
            Log.d("SLIMF", "interest added");
        }

        db.close();
    }

    public void checkAndUpdateInterest(){
        String q = "UPDATE " + TABLE_INTERESTS + " SET " + KEY_ISINTEREST + " = 1 WHERE " + KEY_COUNT + " > 10 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        int length = cursor.getCount();
        Log.d("SLIMF", length + " rows affected ");
        cursor.close();
        db.close();
    }

    public void updateDBValue(List<String> interests){
        SQLiteDatabase db = this.getWritableDatabase();
        for(String interest: interests){
            String q = "UPDATE " + TABLE_INTERESTS + " SET " + KEY_ISINDB + " = 1 WHERE " + KEY_INTEREST + " = \"" + interest + "\"";
            db.execSQL(q);
        }
        db.close();
        Log.d("SLIMF", "values updated");
    }

    public List<String> getInterests(){
        List<String> interests = new ArrayList<>();
        try {
            contact_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_INTERESTS + " WHERE " + KEY_ISINTEREST + " = 1 ";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    interests.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return interests;
    }

    public List<String> getInterestsToUpload(){
        List<String> interests = new ArrayList<>();
        try {
            contact_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_INTERESTS + " WHERE " + KEY_ISINTEREST + " = 1 AND " + KEY_ISINDB + " = 0";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    interests.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return interests;
    }



    public List<Message> getAllData () {
        try {
            List<Message> contact_list = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_INTERESTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Message contact = new Message();
                    //contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setMessage(cursor.getString(1) + " " + cursor.getString(2) + " DB-> " + cursor.getString(4) + " INT -> "  + cursor.getString(3));
                    //contact.setMessage(cursor.getString(2));
                    //contact.setType(cursor.getString(3));
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

}
