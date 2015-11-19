package com.example.davidgeisinger.tennistracker;

/**
 * Created by davidgeisinger on 11/11/15.
 */
import android.provider.BaseColumns;

/**
 * Created by davidgeisinger on 11/11/15.
 */
public class TableData {

    public TableData() {

    }

    public static abstract class TableInfo implements BaseColumns {

        public static final String COLUMN_Date = "date";
        public static final String COLUMN_Stats = "stats";
        public static final String COLUMN_Time = "time";
        public static final String COLUMN_Stroke = "stroke";
        public static final String DATABASE_NAME = "tennis_DB";
        public static final String TABLE_NAME = "tennis_Table";
    }
}