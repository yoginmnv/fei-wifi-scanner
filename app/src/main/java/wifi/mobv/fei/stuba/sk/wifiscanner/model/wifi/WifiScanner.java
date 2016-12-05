package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;

/**
 * Created by maros on 29.11.2016.
 */

public class WifiScanner {
    private static final String TAG = "WifiScanner";

    private final Handler handler;
    private Runnable runnable;

    private WifiManager wifiManager;
    private WifiScanReceiver receiverWifi;
    private List<Wifi> wifiList;
    private SQLController sqlController;

    private int signalLevel = 0;
    private long scanDelay = 10000; // min scan delay 10 sec

    private ManageWifi manageWifiActivity;

    private boolean localizing = false;

    public WifiScanner(ManageWifi activity)
    {
        // Initialize list
        wifiList = new ArrayList<>();

        // Set values
        manageWifiActivity = activity;

        // Manage all aspects of Wi-Fi connectivity
        wifiManager = (WifiManager) manageWifiActivity.getSystemService(Context.WIFI_SERVICE);

        // If is Wi-Fi disabled, enable it
        if (wifiManager.isWifiEnabled() == false ) {
            wifiManager.setWifiEnabled(true);

            Toast.makeText(manageWifiActivity, wifiList.isEmpty() ? "Wi-Fi scans received" : "Wi-Fi scans updated", Toast.LENGTH_SHORT).show();
        }

        receiverWifi = new WifiScanReceiver();

        sqlController = new SQLController(activity);

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                wifiManager.startScan();
                handler.postDelayed(runnable, scanDelay);
            }
        };
    }

    public void startScan()
    {
        manageWifiActivity.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        runnable.run();
        System.out.println("Wi-Fi scan started...");
    }

    public void stopScan()
    {
        handler.removeCallbacks(runnable);
        manageWifiActivity.unregisterReceiver(receiverWifi);
        System.out.println("Wi-Fi scan stopped...");
    }

    class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            System.out.println("Wi-Fi scans received...");

            // Show Toast message on data update
            if (!wifiList.isEmpty())
                Toast.makeText(manageWifiActivity, "Wi-Fi scans updated", Toast.LENGTH_SHORT).show();

            // Delete old content
            wifiList.clear();

            // Iterate through scans and create data
            for (ScanResult res : wifiManager.getScanResults()) {
                // Get the location ID of Wi-FI scan, if the network is saved in database
                long locationId = sqlController.getWifiDAO().findWifiLocationId(res.BSSID);
                Wifi wifiScan = new Wifi(locationId, res.BSSID, res.SSID, res.level);
                wifiList.add(wifiScan);

                System.out.println(wifiScan.getSSID()+ " - " + wifiScan.getBSSID());
            }

            // Update data in Listview
            ListView listView = (ListView) manageWifiActivity.findViewById(R.id.lv_wifi_available);
            ((WifiScanAdapter) listView.getAdapter()).updateList(wifiList);
        }
    }

    public int getSignalLevel()
    {
        return signalLevel;
    }

    /**
     Excellent >-50 dBm
     Good -50 to -60 dBm
     Fair -60 to -70 dBm
     Weak < -70 dBm
     */
    public void setSignalLevel(int signalLevel)
    {
        if( (signalLevel >= -100) && (signalLevel <= 0) )
        {
            this.signalLevel = signalLevel;
        }
        else
        {
            Log.w(TAG, "setSignalLevel() -> use signal level value in range <-100, 0>");
        }
    }

    public long getScanDelay()
    {
        return scanDelay;
    }

    public void setScanDelay(long scanDelay) {
        if( scanDelay > 3999 )
        {
            this.scanDelay = scanDelay;
        }
        else
        {
            Log.w(TAG, "setScanDelay() -> Use scan delay >= 3000 seconds");
        }
    }

    public List<Wifi> getWifiList()
    {
        return wifiList;
    }
}
