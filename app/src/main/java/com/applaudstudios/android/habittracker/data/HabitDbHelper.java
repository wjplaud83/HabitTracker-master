package com.applaudstudios.android.habittracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.applaudstudios.android.habittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_GENDER;
import static com.applaudstudios.android.habittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_NAME;
import static com.applaudstudios.android.habittracker.data.HabitContract.HabitEntry.TABLE_NAME;

/**
 * Created by wjplaud83 on 10/13/16.
 */

/**
 * this is the database for the habit tracker app.
 */
public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    /**
     * name fo the database(db) file
     */
    private static final String DATABASE_NAME = "habits.db";


    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 7;

    /**
     * Constructs a new instance of {@link HabitDbHelper}.
     *
     * @param context of the app
     */
    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /***
     * This is called when the database in created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the habit table
        String SQL_CREATE_HABIT_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + HabitContract.HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HABIT_NAME + " TEXT NOT NULL, "
                + COLUMN_HABIT_GENDER + " INTEGER NOT NULL, "
                + HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE + " TEXT, "
                + HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABIT_TABLE);


    }


    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to be done here.
        db.execSQL("drop table if exists" + TABLE_NAME);
        onCreate(db);
    }


      // CREATE and READ Operations

     // Creating Tables
     // Adding new habit
     void addHabit(HabitContract.HabitEntry habit) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
     values.put(COLUMN_HABIT_NAME, habit.getName()); // Contact Name
     values.put(COLUMN_HABIT_GENDER, habit.getGender()); // Contact Gender

     // Inserting Row
     db.insert(TABLE_NAME, null, values);
     db.close(); // Closing database connection
     }


     // Getting All Habits
     public void getAllHabits() {
     List<HabitContract.HabitEntry> habitList = new ArrayList<HabitContract.HabitEntry>();
     // Select All Query
     String selectQuery = "SELECT  * FROM " + TABLE_NAME;

     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);
     }



}
