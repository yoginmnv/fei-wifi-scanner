package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.MainActivity;
import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO;

/**
 * Created by Feri on 25.11.2016.
 */

public class UpdateWifi extends AppCompatActivity implements OnClickListener,
		AdapterView.OnItemSelectedListener
{
	private WifiDAO wifiDAO;
	private TextView tv_ssid;
	private TextView tv_bssid;
	private Spinner s_blockFloor;
	private Button b_update;
	private Button b_delete;
	private long memberID;
	private long locationID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_wifi);

		wifiDAO = SQLController.getInstance(this).getWifiDAO();

		tv_ssid = (TextView)findViewById(R.id.tv_wifiUpdate_ssid);
		tv_bssid = (TextView)findViewById(R.id.tv_wifiUpdate_bssid);
		s_blockFloor = (Spinner)findViewById(R.id.s_wifiUpdate_blockFloor);
		b_update = (Button)findViewById(R.id.b_wifiUpdate_update);
		b_delete = (Button)findViewById(R.id.b_wifiUpdate_delete);

		List<Location> list = SQLController.getInstance(this).getLocationDAO().readAll();
		LocationAdapter adapter = new LocationAdapter(this, R.layout.update_wifi, list);
		s_blockFloor.setAdapter(adapter);
		s_blockFloor.setOnItemSelectedListener(this);

		Intent i = getIntent();
		memberID = Long.parseLong(i.getStringExtra("memberID"));
		tv_ssid.setText(i.getStringExtra("memberSSID"));
		tv_bssid.setText(i.getStringExtra("memberBSSID"));

		// treba overit ci to funguje a co sa vlastne posiela do memberLocation
		s_blockFloor.setSelection((int)adapter.getItemId(Integer.parseInt(i.getStringExtra("memberLocation")))-1);

		b_update.setOnClickListener(this);
		b_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch( v.getId() )
		{
			case R.id.b_wifiUpdate_update:
				wifiDAO.update(new Wifi(memberID, locationID, tv_bssid.getText().toString()));
				break;

			case R.id.b_wifiUpdate_delete:
				wifiDAO.delete(memberID);
				break;
		}

		Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home_intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{
		Location location = (Location)adapterView.getItemAtPosition(i);
		locationID = location.getId();
		Toast.makeText(this, String.valueOf(location.getId()), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{

	}
}