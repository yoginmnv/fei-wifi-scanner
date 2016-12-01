package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO;

/**
 * Created by maros on 1.12.2016.
 */

public class AddLocation extends AppCompatActivity
{
	private LocationDAO dao;
	private TextWatcher textWatcher;
	EditText et_blockName;
	EditText et_floor;

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

		List<Location> list = dao.readAllLocations();
		for( int i = 0; i < list.size(); ++i )
		{
			Location lc = list.get(i);
			System.out.println(lc.getId() + " " + lc.getBlockName() + " " + lc.getFloor());
		}

		et_blockName = (EditText) findViewById(R.id.et_location_block);
		et_floor = (EditText) findViewById(R.id.et_floor);

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
		String blockName = et_blockName.getText().toString();
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

		dao.createLocation(new Location(blockName, floor));
		Toast.makeText(this, "Location created successfully", Toast.LENGTH_SHORT).show();
	}
}
