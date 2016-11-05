package com.applaudstudios.android.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.applaudstudios.android.habittracker.data.HabitContract;
import com.applaudstudios.android.habittracker.data.HabitDbHelper;


public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the habit name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter your exercise
     */
    private EditText mExerciseEditText;

    /**
     * EditText field for your weight
     */
    private EditText mWeightEditText;

    /**
     * Spinner field to enter a gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the person. The possible valid values are in the PetContract.java file:
     * {@link HabitContract.HabitEntry#GENDER_UNKNOWN}, {@link HabitContract.HabitEntry#GENDER_MALE}, or
     * {@link HabitContract.HabitEntry#GENDER_FEMALE}.
     */
    private int mGender = HabitContract.HabitEntry.GENDER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mExerciseEditText = (EditText) findViewById(R.id.edit_habit_exercise);
        mWeightEditText = (EditText) findViewById(R.id.edit_habit_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender for the habit.
     */
    private void setupSpinner() {
        // create the adapter for the spinner. The list options are from the String array
        // it will use to get the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // specify dropdown layout style - simple list with one item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = HabitContract.HabitEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = HabitContract.HabitEntry.GENDER_FEMALE;
                    } else {
                        mGender = HabitContract.HabitEntry.GENDER_UNKNOWN;
                    }
                }
            }

            //Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = HabitContract.HabitEntry.GENDER_UNKNOWN;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into the database
     */
    private void insertHabit() {
        // read from input fields
        // use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String exerciseString = mExerciseEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = Integer.parseInt(weightString);

        // create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // get the database in write code
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create a ContentValues object where column names are keys,
        // and habit attributes from the editor are teh values.
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_EXERCISE, exerciseString);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_GENDER, mGender);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_WEIGHT, weight);

        // insert a new row for the habit in the database. This returns the ID of the new row.
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on weather or not the insertion was successful.
        if (newRowId == -1) {
            // If the row ID is -1, then there is an error with insertion
            Toast.makeText(this, "Error with saving this habit", Toast.LENGTH_LONG).show();
        } else {
            // otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with the row id: " + newRowId, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu option from the res/menu/menu_editor.xml file.
        // this adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // respond to a click on the "save" menu option
            case R.id.action_save:
                // save habit to database
                insertHabit();
                // exit activity
                finish();
                return true;
            // respond to a click on the "delete" menu option
            case R.id.action_delete:
                //do nothing for now
                return true;
            // respond to a click on the "up" arrow button in the app bar
            case android.R.id.home:
                // navigate back to the parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
