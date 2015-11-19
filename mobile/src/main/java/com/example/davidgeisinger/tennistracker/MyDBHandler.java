package com.example.davidgeisinger.tennistracker;

/**
 * Created by davidgeisinger on 11/8/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public String CREATE_QUERY = "Create Table " + TableData.TableInfo.TABLE_NAME + "(" + TableData.TableInfo.COLUMN_Date + " TEXT," + TableData.TableInfo.COLUMN_Stats + " TEXT," + TableData.TableInfo.COLUMN_Stroke + " TEXT," + TableData.TableInfo.COLUMN_Time + " TEXT);";


    public MyDBHandler(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, DATABASE_VERSION);

        Log.d("Made DB", "hellz");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Made DB", "please");

        db.execSQL(CREATE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableData.TableInfo.TABLE_NAME);
        onCreate(db);
    }

    public void addEntry(MyDBHandler db, StatsPackage sp) {
        ContentValues values = new ContentValues();
        values.put(TableData.TableInfo.COLUMN_Date, sp.date);
        values.put(TableData.TableInfo.COLUMN_Stats, sp.stats);
        values.put(TableData.TableInfo.COLUMN_Time, sp.time);
        values.put(TableData.TableInfo.COLUMN_Stroke, sp.stroke);



        SQLiteDatabase sq = db.getWritableDatabase();
        sq.insert(TableData.TableInfo.TABLE_NAME, null, values);
        Log.d("ADDING", "yup");

        sq.close();
    }
    

    public StatsPackage findEntry(StatsPackage sp) {
        String query = "Select * FROM " + TableData.TableInfo.TABLE_NAME + " WHERE " + TableData.TableInfo.COLUMN_Date + " =  \"" + sp.date + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        StatsPackage new_sp = new StatsPackage("","","","");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            new_sp.date = cursor.getString(0);
            new_sp.stats = cursor.getString(1);
            new_sp.stroke = cursor.getString(2);
            new_sp.time = cursor.getString(3);

            cursor.close();
        } else {
            new_sp = null;
        }
        db.close();
        return new_sp;
    }

    public ArrayList<StatsPackage> findMany(String stroke) {
        String query = "Select * FROM " + TableData.TableInfo.TABLE_NAME + " WHERE " + TableData.TableInfo.COLUMN_Stroke + " =  \"" + "stroke" + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<StatsPackage> list_pack = new ArrayList<StatsPackage>();
        StatsPackage new_sp = new StatsPackage("","","","");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            new_sp.date = cursor.getString(0);
            new_sp.stats = cursor.getString(1);
            new_sp.stroke = cursor.getString(2);
            new_sp.time = cursor.getString(3);
            list_pack.add(new_sp);
        } else {
            new_sp = null;
            list_pack.add(new_sp);
            return list_pack;
        }

        while(cursor.moveToNext()) {
            Log.d("counter", cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
            StatsPackage temp = new StatsPackage("","","","");
            temp.date = cursor.getString(0);
            temp.stats = cursor.getString(1);
            temp.stroke = cursor.getString(2);
            temp.time = cursor.getString(3);
            list_pack.add(temp);
        }

        cursor.close();
        db.close();
        return list_pack;

    }

    public boolean deleteProduct(StatsPackage sp) {

        boolean result = false;

        String query = "Select * FROM " + TableData.TableInfo.TABLE_NAME + " WHERE " + TableData.TableInfo.COLUMN_Date + " =  \"" + sp.date + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        StatsPackage new_sp = new StatsPackage("","","","");

        if (cursor.moveToFirst()) {
            db.delete(TableData.TableInfo.TABLE_NAME, TableData.TableInfo.COLUMN_Date + " = ?",
                    new String[] { cursor.getString(0) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}