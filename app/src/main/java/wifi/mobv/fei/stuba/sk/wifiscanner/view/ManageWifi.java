package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;

/**
 * Created by maros on 4.12.2016.
 */

public class ManageWifi extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
	private WifiScanner ws;
	private CheckBox cb_add_auto;
	private Spinner s_blockFloor;
	private ToggleButton tb_scan;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_wifi);

		ws = new WifiScanner(this);

		cb_add_auto = (CheckBox)findViewById(R.id.cb_wifi_add_automatically);
		s_blockFloor = (Spinner)findViewById(R.id.s_wifi_block_floor);

		List<Location> list = SQLController.getInstance(this).getLocationDAO().readAll();
		LocationAdapter locationAdapter = new LocationAdapter(this, R.layout.manage_wifi, list);
		s_blockFloor.setAdapter(locationAdapter);
		s_blockFloor.setOnItemSelectedListener(this);

		tb_scan = (ToggleButton)findViewById(R.id.tb_wifi_scan);
		tb_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if(tb_scan.isChecked() ) {
					ws.startScan();
				}
				else {
					ws.stopScan();
				}
			}
		});

		// Listview for Wi-Fi scans
		ListView wifiScansListview = (ListView)findViewById(R.id.lv_wifi_available);
		WifiScanAdapter wifiScanAdapter = new WifiScanAdapter(this, ws.getWifiList());
		wifiScansListview.setAdapter(wifiScanAdapter);

	}

	protected void onPause()
	{
		// Stop scan, to avoid constant background Wi-Fi scanning
		if (tb_scan.isChecked()) {
			tb_scan.setChecked(false);
			ws.stopScan();
		}

		super.onPause();
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{
		Location location = (Location)adapterView.getItemAtPosition(i);
		Toast.makeText(this, String.valueOf(location.getId()), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{

	}
}
