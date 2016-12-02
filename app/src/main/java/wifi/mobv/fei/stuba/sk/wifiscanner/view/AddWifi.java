package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import wifi.mobv.fei.stuba.sk.wifiscanner.MainActivity;
import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScan;

/**
 * Created by Feri on 25.11.2016.
 */

public class AddWifi extends AppCompatActivity implements OnClickListener {
    public static String SCAN_RESULT_INTENT_ID = "SCAN_RESULT_INTENT_ID";

    EditText ssid, bssid;//, signal, poschodie, blok ;
    Button add_bt;
    SQLController dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);

        ssid = (EditText) findViewById(R.id.SSID_new);
        bssid = (EditText) findViewById(R.id.BSSID_new);
//        signal = (EditText) findViewById(R.id.signal_new);
//        poschodie = (EditText) findViewById(R.id.poschodie_new);
//        blok = (EditText) findViewById(R.id.blok_new);

        add_bt = (Button) findViewById(R.id.add_bt_id);

        dbcon = new SQLController(this);
        dbcon.open();
        add_bt.setOnClickListener(this);

        // Ziskanie dat z intent-u
        Intent intent = getIntent();
        WifiScan scanResult = (WifiScan) intent.getSerializableExtra(SCAN_RESULT_INTENT_ID);

        // Skontroluje, ci prisli data z intent-u
        if (scanResult != null) {
            ssid.setText(scanResult.SSID);
            bssid.setText(scanResult.BSSID);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bt_id:
                String bssid_new = bssid.getText().toString();
                String ssid_new = ssid.getText().toString();
//                String signal_new = signal.getText().toString();
//                String poschodie_new = poschodie.getText().toString();
//                String blok_new = blok.getText().toString();

                dbcon.insertData(ssid_new, bssid_new, "", "", "");
//                dbcon.insertData(ssid_new, bssid_new, signal_new, poschodie_new, blok_new);

                Intent main = new Intent(AddWifi.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;

            default:
                break;
        }
    }
}