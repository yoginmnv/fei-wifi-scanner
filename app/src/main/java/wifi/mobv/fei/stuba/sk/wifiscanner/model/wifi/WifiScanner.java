package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.MainActivity;
import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;

import static android.R.attr.id;
import static wifi.mobv.fei.stuba.sk.wifiscanner.R.id.s_wifi_block_floor;

/**
 * Created by maros on 29.11.2016.
 */

public class WifiScanner
{
    private static final String TAG = "WifiScanner";

    private Handler handler;
    private Runnable runnable;

    private WifiManager wifiManager;
    private WifiScanReceiver receiverWifi;
    private List<Wifi> wifiList;

    private int signalLevel = 0;
    private long scanDelay = 10000; // min scan delay 10 sec

    private ManageWifi manageWifiActivity;
    private long idLocation;
    private MainActivity activity;
	private boolean locateMe = false;
	private TextView tvPosition;
    public WifiScanner(MainActivity context)
    {
        activity = context;
		locateMe = true;

        // manage all aspects of WIFI connectivity
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // enable automatic wifi
        if( wifiManager.isWifiEnabled() == false )
        {
            wifiManager.setWifiEnabled(true);
        }

        receiverWifi = new WifiScanReceiver();
        context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		tvPosition = (TextView)activity.findViewById(R.id.tv_main_locationResult);
		tvPosition.setText("Retriving location");

        wifiManager.startScan();
    }

    public WifiScanner(ManageWifi activity)
    {
        // Initialize list
        wifiList = new ArrayList<>();

        // Set values
        manageWifiActivity = activity;

        // Default ID for location
        // Used to initialize default value for Location ID in scanned Wifi object
        idLocation = -1;

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

			if( locateMe )
			{
				Location l = SQLController.getInstance(activity).getWifiDAO().locateMe(wifiManager.getScanResults());

				if( l == null )
				{
					tvPosition.setText("Position unknown");
				}
				else
				{
					tvPosition.setText("You are on " + l.getBlockName() + l.getFloor());
				}

				locateMe = false;
				activity.unregisterReceiver(receiverWifi);
			}
			else
			{
				// Show Toast message on data update
				if (!wifiList.isEmpty())
				{
					Toast.makeText(manageWifiActivity, "Wi-Fi scans updated", Toast.LENGTH_SHORT).show();
				}

				// Delete old content
				wifiList.clear();

				idLocation = manageWifiActivity.getActualLocationID();
				// Iterate through scans and create data
				for (ScanResult res : wifiManager.getScanResults()) {
					Wifi wifiScan = new Wifi(idLocation, res.BSSID, res.SSID, res.level);
					wifiList.add(wifiScan);

					System.out.println(wifiScan.getSSID()+ " - " + wifiScan.getBSSID());
				}

				// Update data in Listview
				ListView listView = (ListView)manageWifiActivity.findViewById(R.id.lv_wifi_available);
				((WifiScanAdapter)listView.getAdapter()).updateList(wifiList);

				CheckBox addAuto = (CheckBox)manageWifiActivity.findViewById(R.id.cb_wifi_add_automatically);
				if( addAuto.isChecked() )
				{
					WifiDAO wifiDAO = SQLController.getInstance(manageWifiActivity).getWifiDAO();
					wifiDAO.create(wifiList);
					Toast.makeText(manageWifiActivity, "Networks added autoamticly", Toast.LENGTH_SHORT);
				}
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

    public List<Wifi> getWifiList()
    {
        return wifiList;
    }

    public void setIdLocation(long idLocation) { this.idLocation = idLocation; }
}
