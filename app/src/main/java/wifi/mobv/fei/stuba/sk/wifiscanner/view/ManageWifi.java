package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.History;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;

/**
 * Created by maros on 4.12.2016.
 */

public class ManageWifi extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
	private SQLController controller;
	private WifiScanner ws;
	private CheckBox cb_add_auto;
	private Spinner s_blockFloor;
	private ToggleButton tb_scan;
	private TextView tv_lastScan;

	private List<Location> locationList;
	private Long actualLocationID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_wifi);

		controller = SQLController.getInstance(this);
		// Wi-Fi scanner
		ws = new WifiScanner(this);

		// Chceckbox Scan automatically
		cb_add_auto = (CheckBox)findViewById(R.id.cb_wifi_add_automatically);

		// Spinner for choosing a location
		s_blockFloor = (Spinner)findViewById(R.id.s_wifi_block_floor);

		locationList = controller.getLocationDAO().readAll();
		LocationAdapter locationAdapter = new LocationAdapter(this, R.layout.manage_wifi, locationList);
		s_blockFloor.setAdapter(locationAdapter);
		s_blockFloor.setOnItemSelectedListener(this);


		// Start / Stop scan button
		tb_scan = (ToggleButton)findViewById(R.id.tb_wifi_scan);
		tb_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if(tb_scan.isChecked() ) {
					ws.startScan();
					controller.getHistoryDAO().create(new History(actualLocationID));
				}
				else {
					ws.stopScan();
				}
			}
		});


		// Listview for Wi-Fi scans
		ListView wifiScansListview = (ListView)findViewById(R.id.lv_wifi_available);
		WifiScanAdapter wifiScanAdapter = new WifiScanAdapter(this);
		wifiScansListview.setAdapter(wifiScanAdapter);

		wifiScansListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Intent intent = new Intent(ManageWifi.this, AddWifi.class);

				// Selected Wi-fi network scan
				intent.putExtra(AddWifi.WIFI_SCAN_INTENT_ID, ws.getWifiList().get(position));

				// Selected location ID
				Location selectedLocation = (Location)s_blockFloor.getSelectedItem();
				if (selectedLocation != null)
					intent.putExtra(AddWifi.LOCATION_ID_INTENT_ID, selectedLocation.getId());

				startActivity(intent);
			}
		});

		tv_lastScan = (TextView)findViewById(R.id.tv_savedWifis_lastScan);
		actualLocationID = ((Location)s_blockFloor.getSelectedItem()).getId();
		setLastScanDate(controller.getHistoryDAO().readLatest(actualLocationID));
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
		actualLocationID = location.getId();
		setLastScanDate(controller.getHistoryDAO().readLatest(actualLocationID));
//		Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{

	}

	public void setLastScanDate(History h)
	{
		if( h == null )
		{
			tv_lastScan.setText(getResources().getString(R.string.location_last_scan) + " newer");
		}
		else
		{
			tv_lastScan.setText(getResources().getString(R.string.location_last_scan) + " " + h.getDate());
		}
	}

	public Long getActualLocationID()
	{
		return actualLocationID;
	}
}
