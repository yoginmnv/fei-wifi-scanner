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
    private List<ScanResult> wifiList;

    private int signalLevel = 0;
    private long scanDelay = 10000; // min scan delay 10 sec

    private ManageWifi manageWifiActivity;

    public WifiScanner(ManageWifi activity)
    {
        manageWifiActivity = activity;
        wifiList = new ArrayList<>();

        // Manage all aspects of Wi-Fi connectivity
        wifiManager = (WifiManager) manageWifiActivity.getSystemService(Context.WIFI_SERVICE);

        // If is Wi-Fi disabled, enable it
        if (wifiManager.isWifiEnabled() == false ) {
            wifiManager.setWifiEnabled(true);

            Toast.makeText(manageWifiActivity, wifiList.isEmpty() ? "Wi-Fi scans received" : "Wi-Fi scans updated", Toast.LENGTH_SHORT).show();
        }

        receiverWifi = new WifiScanReceiver();

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

//    public void location(Activity a)
//    {
////    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
////    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//        LocationManager locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//            }
//        });
//    }

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

            // Get scans
            List<ScanResult> scanResults = wifiManager.getScanResults();

            // Delete old content
            wifiList.clear();

            // Add scans to list
            for (int i = 0; i < scanResults.size(); ++i) {
                ScanResult res = scanResults.get(i);
//                WifiScan wifiScan = new WifiScan(res.SSID, res.BSSID);
//
//                System.out.println(wifiScan.SSID + " - " + wifiScan.BSSID );
                wifiList.add(res);
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

    public List<ScanResult> getWifiList()
    {
        return wifiList;
    }
}
