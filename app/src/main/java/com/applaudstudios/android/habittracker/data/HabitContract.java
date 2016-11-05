package com.applaudstudios.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by wjplaud83 on 10/13/16.
 */

public final class HabitContract {

    // this is to prevent accidental instantiation of the contract class,
    // give it an empty constructor

    private HabitContract() {
    }

    /**
     * Inner class that defines constant values for the habit database table.
     * Each entry in the table represents a single time.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of the database table for your habits */
        public final static String TABLE_NAME ="habits";

        /**
         * unique ID number for the habit done (this is for the database table only)
         * TYPE: Integer
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name (either yourself or something in tune with the habit
         * TYPE: Text
         */
        public final static String COLUMN_HABIT_NAME = "name";

        /**
         * Gender of the person
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         *
         * TYPE: INTEGER
         */
        public final static String COLUMN_HABIT_GENDER = "gender";
        /**
         * Type of exercise done
         * TYPE: Text
         */
        public final static String COLUMN_HABIT_EXERCISE = "exercise";

        /**
         * your weight
         * TYPE: Integer
         */
        public final static String COLUMN_HABIT_WEIGHT = "weight";

        /**
         * Possible values for the gender of the person.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        private byte[] name;
        private byte[] gender;

        public byte[] getName() {
            return name;
        }

        public byte[] getGender() {
            return gender;
        }
    }
}
