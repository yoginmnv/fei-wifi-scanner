package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maros on 29.11.2016.
 */

public class WifiScanner {
    private static final String TAG = "WifiScanner";
    private final Handler handler;
    private Runnable runnable;
    private WifiManager wifiManager;
    private WifiScanReceiver receiverWifi;
    private int signalLevel = 0;
    private long scanDelay = 4000; // min scan delay 3 sec
    private List<ScanResult> wifiList;

    public WifiScanner(Context context)
    {
        handler = new Handler();
        // manage all aspects of WIFI connectivity
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // enable automatic wifi
        if( wifiManager.isWifiEnabled() == false )
        {
            wifiManager.setWifiEnabled(true);
        }

        receiverWifi = new WifiScanReceiver();
        context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

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
        Log.i(TAG, "startScan() -> run()");
        runnable.run();
    }

    public void stopScan()
    {
        Log.i(TAG, "stopScan() -> removeCallbacks()");
        handler.removeCallbacks(runnable);
    }

    class WifiScanReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
            System.out.println("");
            ArrayList<String> connections = new ArrayList<String>();
            wifiList = wifiManager.getScanResults(); // ACCESS_WIFI_STATE

            for( int i = 0; i < wifiList.size(); ++i )
            {
                ScanResult res = wifiList.get(i);
                if( res.level < signalLevel )
                {
                    System.out.println(res.SSID + " - " + res.BSSID + " - " + res.level);
                }
                //dataSource.createWifi(new Wifi("C", "3", res.SSID, res.BSSID, res.level));
            }
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
