package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.LocationAdapter;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO;

/**
 * Created by maros on 1.12.2016.
 */

public class AddLocation extends AppCompatActivity
{
	private LocationDAO dao;
	private TextWatcher textWatcher;
	LocationAdapter adapter;
	EditText et_blockName;
	EditText et_floor;
	ListView lv_location;

	public void AddLocation(LocationDAO dao)
	{
		this.dao = dao;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_location);

		dao = new LocationDAO(this);

		et_blockName = (EditText) findViewById(R.id.et_location_block);
		et_floor = (EditText) findViewById(R.id.et_floor);
		lv_location = (ListView) findViewById(R.id.lv_location);

		//instantiate custom adapter
//		adapter = new LocationAdapter(dao.readAll(), this);
//		lv_location.setAdapter(adapter);

		textWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void afterTextChanged(Editable editable)
			{

			}
		};

		// just disable the create button
		et_blockName.addTextChangedListener(textWatcher);
		et_floor.addTextChangedListener(textWatcher);
	}

	public void createLocation(View view)
	{
		String blockName = et_blockName.getText().toString().toUpperCase();
		if( blockName.matches("") )
		{
			Toast.makeText(this, "You did not enter a block name", Toast.LENGTH_SHORT).show();
			return;
		}

		String floor = et_floor.getText().toString();
		if (floor.matches("")) {
			Toast.makeText(this, "You did not enter a floor number", Toast.LENGTH_SHORT).show();
			return;
		}

		if( dao.create(new Location(blockName, floor)) == -1 )
		{
			Toast.makeText(this, "Entry alredy exists", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Location created successfully", Toast.LENGTH_SHORT).show();
			adapter = new LocationAdapter(dao.readAll(), this);
			lv_location.setAdapter(adapter);
		}

	}
}
