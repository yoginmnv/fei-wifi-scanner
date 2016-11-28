package com.example.feri.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Feri on 25.11.2016.
 */

public class DBhelper extends SQLiteOpenHelper {
    // TABLE INFORMATTION
    public static final String TABLE_WIFI = "wifi";
    public static final String MEMBER_ID = "_id";

    public static final String SSID="ssid";
    public static final String BSSID = "mac_addr";
    public static final String MAX_SIGNAL="max_signal";
    public static final String POSCHODIE="poschodie";
    public static final String BLOK = "blok";

    // DATABASE INFORMATION
    static final String DB_NAME = "WIFI.DB";
    static final int DB_VERSION = 1;

    // TABLE CREATION STATEMENT
    private static final String CREATE_TABLE = "create table "
            + TABLE_WIFI + "(" + MEMBER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SSID + " TEXT NOT NULL, "
            + BSSID + " TEXT NOT NULL UNIQUE, "
            + MAX_SIGNAL + " TEXT NOT NULL, "
            + POSCHODIE + " TEXT NOT NULL, "
            + BLOK + " TEXT NOT NULL);";


    public DBhelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI);
        onCreate(db);
    }

}
