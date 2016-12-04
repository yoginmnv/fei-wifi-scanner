package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import wifi.mobv.fei.stuba.sk.wifiscanner.MainActivity;
import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;

/**
 * Created by Feri on 25.11.2016.
 */

public class AddWifi extends AppCompatActivity implements OnClickListener {
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bt_id:
                String bssid_new = bssid.getText().toString();
                String ssid_new = ssid.getText().toString();
                String signal_new = signal.getText().toString();
                String id_location = location.getText().toString();

                dbcon.insertData(ssid_new, bssid_new, signal_new, id_location);

                Intent main = new Intent(AddWifi.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;

            default:
                break;
        }
    }
}