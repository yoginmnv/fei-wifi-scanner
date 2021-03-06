package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;

/**
 * Created by Feri on 25.11.2016.
 */

public class AddWifi extends AppCompatActivity implements OnClickListener {
    public static String WIFI_SCAN_INTENT_ID = "wifi_scan_intent";
    public static String LOCATION_ID_INTENT_ID = "location_id_intent";

    EditText ssid, bssid, signal, location;
    Button add_bt;
    SQLController dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wifi);

        ssid = (EditText) findViewById(R.id.SSID_new);
        bssid = (EditText) findViewById(R.id.BSSID_new);
        signal = (EditText) findViewById(R.id.signal_new);
        location = (EditText) findViewById(R.id.location_new);


        add_bt = (Button) findViewById(R.id.add_bt_id);

        dbcon = new SQLController(this);
        dbcon.open();
        add_bt.setOnClickListener(this);

        // Get data from intent
        Intent intent = getIntent();

        /// Get Wifi scan object
        Wifi wifiScan = (Wifi) intent.getSerializableExtra(WIFI_SCAN_INTENT_ID);
        if (wifiScan != null) {
            ssid.setText(wifiScan.getSSID());
            ssid.setEnabled(false);

            bssid.setText(wifiScan.getBSSID());
            bssid.setEnabled(false);

            signal.setText(String.valueOf(wifiScan.getMaxLevel()));
            signal.setEnabled(false);
        }

        /// Get location ID
        long locationId = intent.getLongExtra(LOCATION_ID_INTENT_ID, -1);
        if (locationId != -1) {
            location.setText(String.valueOf(locationId));
            location.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bt_id:
                String bssid_new = bssid.getText().toString();
                String ssid_new = ssid.getText().toString();
                String signal_new = signal.getText().toString();
                String id_location = location.getText().toString();

                // Get the ID of newly inserted row. Value is -1 when error occured
                long insertRowId = dbcon.insertData(ssid_new, bssid_new, signal_new, id_location);

                Toast.makeText(this, insertRowId != -1 ? "Wi-Fi network was succesfully inserted" : "Error: Wi-Fi network was not inserted", Toast.LENGTH_LONG).show();

                finish();
                break;

            default:
                break;
        }
    }
}