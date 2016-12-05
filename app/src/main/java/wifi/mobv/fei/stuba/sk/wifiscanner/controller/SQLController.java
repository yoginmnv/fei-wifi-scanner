package wifi.mobv.fei.stuba.sk.wifiscanner.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.HistoryDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;

/**
 * Created by Feri on 25.11.2016.
 */

public class SQLController {

    private static SQLController instance;
    private LocationDAO locationDAO;
    private WifiDAO wifiDAO;
    private HistoryDAO historyDAO;

    private DBHelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public static SQLController getInstance(Context c)
    {
        if( instance == null )
        {
            instance = new SQLController(c);
        }

        return instance;
    }

    public SQLController(Context c) {
        locationDAO = new LocationDAO(this, c);
        wifiDAO = new WifiDAO(this, c);
        historyDAO = new HistoryDAO(this, c);

        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        dbhelper = DBHelper.getInstance(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbhelper.close();
    }

    public long insertData(String ssid, String bssid, String max_signal, String id_location) {
        ContentValues cv = new ContentValues();
        cv.put(WifiDAO.WifiEntry.COLUMN_NAME_SSID,ssid);
        cv.put(WifiDAO.WifiEntry.COLUMN_NAME_BSSID,bssid);
        cv.put(WifiDAO.WifiEntry.COLUMN_NAME_MAX_LEVEL,max_signal);
        cv.put(WifiDAO.WifiEntry.COLUMN_NAME_ID_LOCATION, id_location);

        return database.insert(WifiDAO.WifiEntry.TABLE_NAME, null, cv);
    }

    public Cursor readData() {
        String[] allColumns = new String[] {
                WifiDAO.WifiEntry._ID,
                WifiDAO.WifiEntry.COLUMN_NAME_ID_LOCATION,
                WifiDAO.WifiEntry.COLUMN_NAME_BSSID
        };
        Cursor c = database.query(WifiDAO.WifiEntry.TABLE_NAME, allColumns, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readDataById(String member_ID) {
        String whereClause = WifiDAO.WifiEntry._ID + " = ? ";
        String[] whereArgs = new String[] {
                member_ID
        };

        Cursor c = database.query(WifiDAO.WifiEntry.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //vyhladavanie podla BLOKU a POSCHODIA
    public Cursor findDataByBlok(String blok, String poschodie){
        String[] allColumns = new String[] {WifiDAO.WifiEntry._ID, WifiDAO.WifiEntry.COLUMN_NAME_ID_LOCATION, WifiDAO.WifiEntry.COLUMN_NAME_BSSID};
        //String[] podmienka = new String[] { Wifi.WifiEntry.BLOK + "=" +  blok + "AND " + DBHelper.POSCHODIE + "=" + poschodie };
        Cursor c = database.query(WifiDAO.WifiEntry.TABLE_NAME,allColumns,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public int updateData(long memberID, String ssid, String bssid, String max_signal, String location) {
        ContentValues cvUpdate = new ContentValues();

        cvUpdate.put(WifiDAO.WifiEntry.COLUMN_NAME_SSID,ssid);
        cvUpdate.put(WifiDAO.WifiEntry.COLUMN_NAME_BSSID,bssid);
        cvUpdate.put(WifiDAO.WifiEntry.COLUMN_NAME_MAX_LEVEL,max_signal);
        cvUpdate.put(WifiDAO.WifiEntry.COLUMN_NAME_ID_LOCATION,location);

        int i = database.update(WifiDAO.WifiEntry.TABLE_NAME, cvUpdate, WifiDAO.WifiEntry._ID + " = " + memberID, null);
        return i;
    }

    public void deleteData(long memberID) {
        database.delete(WifiDAO.WifiEntry.TABLE_NAME, WifiDAO.WifiEntry._ID + "=" + memberID, null);
    }

    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    public WifiDAO getWifiDAO()
    {
        return wifiDAO;
    }

    public HistoryDAO getHistoryDAO()
    {
        return historyDAO;
    }
}