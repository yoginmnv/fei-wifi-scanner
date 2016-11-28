package com.example.feri.sqlitedemo;

/**
 * Created by Feri on 25.11.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLController {

    private DBhelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        dbhelper = new DBhelper(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbhelper.close();
    }

    public void insertData(String ssid, String bssid, String max_signal, String poschodie, String blok) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.SSID,ssid);
        cv.put(DBhelper.BSSID,bssid);
        cv.put(DBhelper.MAX_SIGNAL,max_signal);
        cv.put(DBhelper.POSCHODIE, poschodie);
        cv.put(DBhelper.BLOK, blok);

        database.insert(DBhelper.TABLE_WIFI, null, cv);
    }

    public Cursor readData() {
        String[] allColumns = new String[] { DBhelper.MEMBER_ID,
                DBhelper.BLOK, DBhelper.BSSID};
        Cursor c = database.query(DBhelper.TABLE_WIFI, allColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readDataById(String member_ID) {
        String whereClause = DBhelper.MEMBER_ID + " = ? ";
        String[] whereArgs = new String[] {
            member_ID
        };

        Cursor c = database.query(DBhelper.TABLE_WIFI, null, whereClause,
                whereArgs, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


//vyhladavanie podla BLOKU a POSCHODIA
    public Cursor findDataByBlok(String blok, String poschodie){
        String[] allColumns = new String[] {DBhelper.MEMBER_ID, DBhelper.BLOK, DBhelper.BSSID};
        String[] podmienka = new String[] { DBhelper.BLOK + "=" +  blok + "AND " + DBhelper.POSCHODIE + "=" + poschodie };
        Cursor c = database.query(DBhelper.TABLE_WIFI,allColumns,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public int updateData(long memberID, String ssid, String bssid, String max_signal, String poschodie, String blok) {
        ContentValues cvUpdate = new ContentValues();

        cvUpdate.put(DBhelper.SSID,ssid);
        cvUpdate.put(DBhelper.BSSID,bssid);
        cvUpdate.put(DBhelper.MAX_SIGNAL,max_signal);
        cvUpdate.put(DBhelper.POSCHODIE, poschodie);
        cvUpdate.put(DBhelper.BLOK, blok);

        int i = database.update(DBhelper.TABLE_WIFI, cvUpdate,
                DBhelper.MEMBER_ID + " = " + memberID, null);
        return i;
    }

    public void deleteData(long memberID) {
        database.delete(DBhelper.TABLE_WIFI, DBhelper.MEMBER_ID + "="
                + memberID, null);
    }

}
