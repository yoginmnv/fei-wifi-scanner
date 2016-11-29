package wifi.mobv.fei.stuba.sk.wifiscanner.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;

/**
 * Created by Feri on 25.11.2016.
 */

public class SQLController {

    private DBHelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        dbhelper = new DBHelper(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbhelper.close();
    }

    public void insertData(String ssid, String bssid, String max_signal, String poschodie, String blok) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.SSID,ssid);
        cv.put(DBHelper.BSSID,bssid);
        cv.put(DBHelper.MAX_SIGNAL,max_signal);
        cv.put(DBHelper.POSCHODIE, poschodie);
        cv.put(DBHelper.BLOK, blok);

        database.insert(DBHelper.TABLE_WIFI, null, cv);
    }

    public Cursor readData() {
        String[] allColumns = new String[] { DBHelper.MEMBER_ID,
                DBHelper.BLOK, DBHelper.BSSID};
        Cursor c = database.query(DBHelper.TABLE_WIFI, allColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readDataById(String member_ID) {
        String whereClause = DBHelper.MEMBER_ID + " = ? ";
        String[] whereArgs = new String[] {
                member_ID
        };

        Cursor c = database.query(DBHelper.TABLE_WIFI, null, whereClause,
                whereArgs, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //vyhladavanie podla BLOKU a POSCHODIA
    public Cursor findDataByBlok(String blok, String poschodie){
        String[] allColumns = new String[] {DBHelper.MEMBER_ID, DBHelper.BLOK, DBHelper.BSSID};
        String[] podmienka = new String[] { DBHelper.BLOK + "=" +  blok + "AND " + DBHelper.POSCHODIE + "=" + poschodie };
        Cursor c = database.query(DBHelper.TABLE_WIFI,allColumns,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public int updateData(long memberID, String ssid, String bssid, String max_signal, String poschodie, String blok) {
        ContentValues cvUpdate = new ContentValues();

        cvUpdate.put(DBHelper.SSID,ssid);
        cvUpdate.put(DBHelper.BSSID,bssid);
        cvUpdate.put(DBHelper.MAX_SIGNAL,max_signal);
        cvUpdate.put(DBHelper.POSCHODIE, poschodie);
        cvUpdate.put(DBHelper.BLOK, blok);

        int i = database.update(DBHelper.TABLE_WIFI, cvUpdate,
                DBHelper.MEMBER_ID + " = " + memberID, null);
        return i;
    }

    public void deleteData(long memberID) {
        database.delete(DBHelper.TABLE_WIFI, DBHelper.MEMBER_ID + "="
                + memberID, null);
    }

}