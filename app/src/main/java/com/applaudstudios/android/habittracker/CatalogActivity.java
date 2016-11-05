package com.applaudstudios.android.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.applaudstudios.android.habittracker.data.HabitContract;
import com.applaudstudios.android.habittracker.data.HabitDbHelper;

/**
 * Created by wjplaud83 on 10/13/16.
 */

public class CatalogActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // SetUp FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // to access the database we have to instantiate our SQLiteOpener subclass
        // and pass the context of the current activity
        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about
     * the state of the habit database.
     */
    private void displayDatabaseInfo() {
        // create and/or open a database to read it from
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //define a projection that specifies which columns from the database
        //you will actually use after the query.
        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE,
                HabitContract.HabitEntry.COLUMN_HABIT_GENDER,
                HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT };

        // Perform a query on the habits in the table
        Cursor cursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,    // the table to query
                projection,                             // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null);                                  // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            //create a header in the TextView that looks like this:
            //
            // The habit table contains <number of rows in Cursor> habits.
            // _id - name - gender - exercise - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order
            displayView.setText("The habits table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitContract.HabitEntry._ID + " _ " +
                    HabitContract.HabitEntry.COLUMN_HABIT_NAME + " _ " +
                    HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE + " _ " +
                    HabitContract.HabitEntry.COLUMN_HABIT_GENDER + " _ " +
                    HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT + "\n");

            // figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int exerciseColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE);
            int genderColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT);

            // iterate through all the returned rows of the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentExercise = cursor.getString(exerciseColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                // display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " _ " +
                        currentName + " _ " +
                        currentExercise + " _ " +
                        currentGender + " _ " +
                        currentWeight));
            }
        } finally {
            // always close the cursor when you are done reading from it.
            // this releases resources and make it invalid
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded habit data into the database for debugging purposes only
     */
    private void insertHabit() {
        //gets the database to writing mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Create a ContentValues object where column names are the key
        // and pet attributes are the values
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, "Gloria");
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_GENDER, HabitContract.HabitEntry.GENDER_FEMALE);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE, "Squats");
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT, 120);



        // insert a new row of habits into the database, returning the id of that new row.
        // the first argument for db.insert() is the habit table name.
        // the second argument provides the name of the column in which the framework
        // can insert NULL in the event that the ContentsValue is empty
        // (if this is set to "null", then the framework will not insert a row when there is no value)
        // The third argument id the ContentsValue object containing the info for teh habit.
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert Data" menu option
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
