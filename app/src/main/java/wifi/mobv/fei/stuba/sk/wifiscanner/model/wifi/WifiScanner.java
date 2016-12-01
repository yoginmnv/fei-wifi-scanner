package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;

/**
 * Created by maros on 29.11.2016.
 */

public class WifiScanner {
//    private static final String TAG = "WifiScanner";
//    private final Handler handler;
//    private Runnable runnable;
//    private int signalLevel = 0;
//    private long scanDelay = 4000; // min scan delay 3 sec

    private WifiManager wifiManager;
    private WifiScanReceiver receiverWifi;
    private List<ScanResult> wifiList = new ArrayList<ScanResult>();
    private Activity scanWifiActivity;

    public WifiScanner(Activity activity) {
        scanWifiActivity = activity;
//        handler = new Handler();
        // manage all aspects of WIFI connectivity
        wifiManager = (WifiManager) scanWifiActivity.getSystemService(Context.WIFI_SERVICE);

        // enable automatic wifi
        if (wifiManager.isWifiEnabled() == false) {
            // If is wifi disabled, enable it
            wifiManager.setWifiEnabled(true);

            Toast.makeText(scanWifiActivity, "WiFi was enabled", Toast.LENGTH_LONG).show();
        }

        receiverWifi = new WifiScanReceiver();
        scanWifiActivity.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

//        runnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                wifiManager.startScan();
//                handler.postDelayed(runnable, scanDelay);
//            }
//        };

    }

//    public void location(Activity a)
//    {
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

    public void startScan() {
        System.out.println("Wi-Fi scan started...");
        wifiManager.startScan();
    }

    public void stopScan() {
        System.out.println("Wi-Fi scan stopped...");
        scanWifiActivity.unregisterReceiver(receiverWifi);
    }

    class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            System.out.println("Received scans");
            ArrayList<String> connections = new ArrayList<String>();
            wifiList = wifiManager.getScanResults(); // ACCESS_WIFI_STATE

            ListView listView = (ListView) scanWifiActivity.findViewById(R.id.wifiScanListViewId);
            ((WifiScanResultListviewAdapter) listView.getAdapter()).updateList(wifiList);

            for (int i = 0; i < wifiList.size(); ++i) {
                ScanResult res = wifiList.get(i);
                System.out.println(res.SSID + " - " + res.BSSID + " - " + res.level);
            }
        }
    }

//    public int getSignalLevel()
//    {
//        return signalLevel;
//    }
//
//    /**
//     Excellent >-50 dBm
//     Good -50 to -60 dBm
//     Fair -60 to -70 dBm
//     Weak < -70 dBm
//     */
//    public void setSignalLevel(int signalLevel)
//    {
//        if( (signalLevel >= -100) && (signalLevel <= 0) )
//        {
//            this.signalLevel = signalLevel;
//        }
//        else
//        {
//            Log.w(TAG, "setSignalLevel() -> use signal level value in range <-100, 0>");
//        }
//    }
//
//    public long getScanDelay()
//    {
//        return scanDelay;
//    }
//
//    public void setScanDelay(long scanDelay) {
//        if( scanDelay > 3999 )
//        {
//            this.scanDelay = scanDelay;
//        }
//        else
//        {
//            Log.w(TAG, "setScanDelay() -> Use scan delay >= 3000 seconds");
//        }
//    }

    public List<ScanResult> getWifiList() {
        return wifiList;
    }
}
