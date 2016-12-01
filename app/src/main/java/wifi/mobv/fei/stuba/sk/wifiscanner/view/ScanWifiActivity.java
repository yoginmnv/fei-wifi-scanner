package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ListView;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.WifiScanResultListviewAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik on 30.11.2016.
 */

public class ScanWifiActivity extends AppCompatActivity {
    private WifiScanner wifiScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_wifi);

        wifiScanner = new WifiScanner(getApplicationContext());

        ListView listView = (ListView)findViewById(R.id.wifiScanListViewId);
        WifiScanResultListviewAdapter adapter = new WifiScanResultListviewAdapter(this, wifiScanner.getWifiList(), getResources());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        wifiScanner.startScan();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        wifiScanner.stopScan();
    }
}
