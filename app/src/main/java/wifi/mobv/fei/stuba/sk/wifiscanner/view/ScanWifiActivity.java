package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ScanWifiActivity.this, AddWifi.class);
                intent.putExtra(AddWifi.SCAN_RESULT_INTENT_ID, wifiScanner.getWifiList().get(position));
                startActivity(intent);
            }
        });
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
