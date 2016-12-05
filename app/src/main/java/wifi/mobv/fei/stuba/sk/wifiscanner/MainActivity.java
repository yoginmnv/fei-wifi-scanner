package wifi.mobv.fei.stuba.sk.wifiscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi.WifiScanner;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.AddLocation;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.SavedWifi;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void changeActivity(View view)
	{
		Intent intent = null;

		switch( view.getId() )
		{
			case R.id.b_location_manage:
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
		new WifiScanner(MainActivity.this);
	}

	public void createDummyData()
	{
		SQLController instance = SQLController.getInstance(this);

		LocationDAO locationDAO = instance.getLocationDAO();
		long l1 = locationDAO.create(new Location("C", "1"));
		long l2 = locationDAO.create(new Location("C", "2"));
		long l3 = locationDAO.create(new Location("C", "3"));
		locationDAO.printAll();

		WifiDAO wifiDAO = instance.getWifiDAO();
		List<Wifi> list = new ArrayList<>();
		list.add(list.size(), new Wifi(l1, "mac13-0", "ssid13-nazov0", -50));
		list.add(list.size(), new Wifi(l1, "mac13-1", "ssid13-nazov1", -50));
		list.add(list.size(), new Wifi(l1, "mac13-2", "ssid13-nazov2", -50));
		list.add(list.size(), new Wifi(l2, "mac7-0", "ssid7-nazov0", -50));
		list.add(list.size(), new Wifi(l2, "mac7-1", "ssid7-nazov1", -50));
		list.add(list.size(), new Wifi(l2, "mac7-2", "ssid7-nazov2", -50));
		list.add(list.size(), new Wifi(l2, "mac7-3", "ssid7-nazov3", -50));
		list.add(list.size(), new Wifi(l3, "mac10-0", "ssid7-nazov3", -50));
		list.add(list.size(), new Wifi(l3, "mac10-1", "ssid7-nazov3", -50));
		wifiDAO.create(list);
		wifiDAO.printAll();

		Location location = wifiDAO.locateMeDummy(list);
		System.out.println("Here you are " + location.toString());
	}
}