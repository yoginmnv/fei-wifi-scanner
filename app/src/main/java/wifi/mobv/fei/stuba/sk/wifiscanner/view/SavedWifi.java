package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;

/**
 * Created by Feri on 04.12.2016.
 */

public class SavedWifi extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner s_blockFloor;
    private ListView lv_WifiSaved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_wifis);

        lv_WifiSaved = (ListView) findViewById(R.id.lv_wifi_saved);
        s_blockFloor = (Spinner)findViewById(R.id.s_wifi_block_floor);
        List<Location> list = SQLController.getInstance(this).getLocationDAO().readAll();
        LocationAdapter adapter = new LocationAdapter(this, R.layout.manage_wifi, list);
        s_blockFloor.setAdapter(adapter);
        s_blockFloor.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Location location = (Location)adapterView.getItemAtPosition(i);
        Cursor cursor = SQLController.getInstance(this).getWifiDAO().readWifiForLocation(location);

        String[] from = new String[] {
                WifiDAO.WifiEntry._ID,
                WifiDAO.WifiEntry.COLUMN_NAME_BSSID,
                WifiDAO.WifiEntry.COLUMN_NAME_SSID
        };
        int[] to = new int[] {
                R.id.tv_wifi_saved_id,
                R.id.tv_wifi_saved_BSSID,
                R.id.tv_wifi_saved_SSID
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_wifis_saved, cursor, from, to);

        adapter.notifyDataSetChanged();
        lv_WifiSaved.setAdapter(adapter);
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
