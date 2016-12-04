package wifi.mobv.fei.stuba.sk.wifiscanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.AddLocation;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.AddWifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.SavedWifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.UpdateWifi;

public class MainActivity extends AppCompatActivity
{

	SQLController dbcon;

	Button addmem_bt;
	ListView lv;
	TextView memID_tv, memBlok_tv, memMac_tv;
	TextView tv_locationResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbcon = SQLController.getInstance(this);

		//        WifiScanner ws = new WifiScanner(this);
		//        ws.startScan();
		//        dbcon.getWifiDAO().locateMe(ws.getWifiList());
		//        ws.stopScan();
		dbcon.open();

		addmem_bt = (Button)findViewById(R.id.addmem_bt_id);
		lv = (ListView)findViewById(R.id.memberList_id);

		// onClickListiner for addmember Button
		addmem_bt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent add_mem = new Intent(MainActivity.this, AddWifi.class);
				startActivity(add_mem);
			}
		});

		// Attach The Data From DataBase Into ListView Using Crusor Adapter
		Cursor cursor = dbcon.readData();
		String[] from = new String[]{WifiDAO.WifiEntry._ID, WifiDAO.WifiEntry.COLUMN_NAME_BSSID};
		int[] to = new int[]{R.id.member_id, R.id.member_BSSID};

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_wifis, cursor, from, to);

		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);

		// OnCLickListiner For List Items
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				memID_tv = (TextView)view.findViewById(R.id.member_id);

				String memberID_val = memID_tv.getText().toString();
				Cursor cursor = dbcon.readDataById(memberID_val);

				Intent modify_intent = new Intent(getApplicationContext(), UpdateWifi.class);
				modify_intent.putExtra("memberID", memberID_val);
				modify_intent.putExtra("memberLocation", cursor.getString(1));
				modify_intent.putExtra("memberBSSID", cursor.getString(2));
				modify_intent.putExtra("memberSSID", cursor.getString(3));
				modify_intent.putExtra("memberSignal", cursor.getString(4));


				startActivity(modify_intent);
			}
		});

		tv_locationResult = (TextView)findViewById(R.id.tv_main_locationResult);
	}

	public void changeActivity(View view)
	{
		Intent intent = null;

		switch( view.getId() )
		{
			case R.id.b_create_location:
				intent = new Intent(this, AddLocation.class);
				break;
			case R.id.b_wifi_manage:
				intent = new Intent(this, ManageWifi.class);
				break;

			case R.id.b_wifi_saved:
				intent = new Intent(this, SavedWifi.class);
				break;

		}

		if( intent != null )
		{
			startActivity(intent);
		}
	}

	public void locateMe(View view)
	{
		WifiScanner ws = new WifiScanner(this);
		tv_locationResult.setText("Retriving location");
	}

	public void createDummyData()
	{
		WifiDAO wifiDAO = SQLController.getInstance(this).getWifiDAO();
		List<Wifi> list = new ArrayList<>();
		list.add(list.size(), new Wifi(13, "mac13-0", "ssid13-nazov0", -50));
		list.add(list.size(), new Wifi(13, "mac13-1", "ssid13-nazov1", -50));
		list.add(list.size(), new Wifi(13, "mac13-2", "ssid13-nazov2", -50));
		list.add(list.size(), new Wifi(7, "mac7-0", "ssid7-nazov0", -50));
		list.add(list.size(), new Wifi(7, "mac7-1", "ssid7-nazov1", -50));
		list.add(list.size(), new Wifi(7, "mac7-2", "ssid7-nazov2", -50));
		list.add(list.size(), new Wifi(7, "mac7-3", "ssid7-nazov3", -50));
		list.add(list.size(), new Wifi(10, "mac10-0", "ssid7-nazov3", -50));
		list.add(list.size(), new Wifi(10, "mac10-1", "ssid7-nazov3", -50));
		//wifiDAO.create(list);
		wifiDAO.printAll();

		Location location = wifiDAO.locateMeDummy(list);
	}
}