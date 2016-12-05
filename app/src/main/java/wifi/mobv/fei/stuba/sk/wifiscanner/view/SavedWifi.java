package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;

/**
 * Created by Feri on 04.12.2016.
 */

public class SavedWifi extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner s_blockFloor;
    private ListView lv_WifiSaved;
    private Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_wifis);

        lv_WifiSaved = (ListView) findViewById(R.id.lv_wifi_saved);
        s_blockFloor = (Spinner)findViewById(R.id.s_wifiSaved_blockFloor);
        List<Location> list = SQLController.getInstance(this).getLocationDAO().readAll();
        LocationAdapter adapter = new LocationAdapter(this, R.layout.manage_wifi, list);
        s_blockFloor.setAdapter(adapter);
        s_blockFloor.setOnItemSelectedListener(this);

        lv_WifiSaved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView memID_tv = (TextView) view.findViewById(R.id.tv_wifi_saved_id);

                String memberID_val = memID_tv.getText().toString();

                Cursor cursor = SQLController.getInstance(SavedWifi.this).open().readDataById(memberID_val);

                Intent modify_intent = new Intent(getApplicationContext(), UpdateWifi.class);
                modify_intent.putExtra("memberID", memberID_val);
                modify_intent.putExtra("memberLocation",cursor.getString(1));
                modify_intent.putExtra("memberBSSID", cursor.getString(2));
                modify_intent.putExtra("memberSSID",cursor.getString(3));
                modify_intent.putExtra("memberSignal",cursor.getString(4));


                startActivity(modify_intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        location = (Location)adapterView.getItemAtPosition(i);
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

    public void deleteAllWifiFromList(View view){

        if(SQLController.getInstance(this).getWifiDAO().readWifiForLocation(location).getCount()==0)
            return;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SQLController.getInstance(SavedWifi.this).getWifiDAO().deleteForLocation(location.getId());

                Cursor cursor = SQLController.getInstance(SavedWifi.this).getWifiDAO().readWifiForLocation(location);

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

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(SavedWifi.this, R.layout.view_wifis_saved, cursor, from, to);

                adapter.notifyDataSetChanged();
                lv_WifiSaved.setAdapter(adapter);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
