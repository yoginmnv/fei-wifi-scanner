package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.ListView;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanResultListviewAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;

/**
 * Created by Patrik on 30.11.2016.
 */

public class ScanWifiActivity extends AppCompatActivity
{
    private WifiScanner wifiScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_wifi);

        wifiScanner = new WifiScanner(this);

        ListView listView = (ListView)findViewById(R.id.wifiScanListViewId);
        WifiScanResultListviewAdapter adapter = new WifiScanResultListviewAdapter(this, wifiScanner.getWifiList());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        wifiScanner.startScan();
    }

    @Override
    protected void onPause()
    {
        wifiScanner.stopScan();
        super.onPause();
    }
}
