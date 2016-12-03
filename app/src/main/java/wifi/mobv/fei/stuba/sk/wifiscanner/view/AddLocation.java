package wifi.mobv.fei.stuba.sk.wifiscanner.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO;

/**
 * Created by maros on 1.12.2016.
 */

public class AddLocation extends AppCompatActivity
{
	private LocationDAO dao;
	private TextWatcher textWatcher;
    private  Location edit_location;

    SimpleCursorAdapter adapter;
	EditText et_blockName;
	EditText et_floor;

    Button editb;
    Button deleteb;

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

		dao = SQLController.getInstance(this).getLocationDAO();

		et_blockName = (EditText) findViewById(R.id.et_location_block);
		et_floor = (EditText) findViewById(R.id.et_floor);
		lv_location = (ListView) findViewById(R.id.lv_location);

        editb = (Button)findViewById(R.id.b_location_edit);
        deleteb = (Button)findViewById(R.id.b_location_delete);

        Cursor cursor = dao.readData();
		String[] from = new String[] {
				LocationDAO.LocationEntry._ID,
				LocationDAO.LocationEntry.COLUMN_NAME_BLOCK_NAME,
				LocationDAO.LocationEntry.COLUMN_NAME_FLOOR
		};

		int[] to = new int[] { R.id.location_id, R.id.location_nameOfBlock, R.id.location_floorNumber };

        adapter = new SimpleCursorAdapter(this, R.layout.view_locations, cursor, from, to);

        adapter.notifyDataSetChanged();
		lv_location.setAdapter(adapter);

        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView et_id = (TextView) view.findViewById(R.id.location_id);

                String memberID_val = et_id.getText().toString();

                edit_location = dao.read(Integer.parseInt(memberID_val));

                et_blockName.setText(edit_location.getBlockName());
                et_floor.setText(edit_location.getFloor());

            }
        });

        deleteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_location != null) {
                    dao.delete(edit_location.getId());

                    edit_location=null;
                    et_floor.setText("");
                    et_blockName.setText("");

                    String[] from = new String[] {
                            LocationDAO.LocationEntry._ID,
                            LocationDAO.LocationEntry.COLUMN_NAME_BLOCK_NAME,
                            LocationDAO.LocationEntry.COLUMN_NAME_FLOOR
                    };

                    int[] to = new int[] { R.id.location_id, R.id.location_nameOfBlock, R.id.location_floorNumber };

                    adapter = new SimpleCursorAdapter(AddLocation.this, R.layout.view_locations, dao.readData(), from, to);

                    adapter.notifyDataSetChanged();
                    lv_location.setAdapter(adapter);
                }
            }
        });

        editb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_location != null) {

                    edit_location.setBlockName(et_blockName.getText().toString());
                    edit_location.setFloor(et_floor.getText().toString());

                    dao.update(edit_location);

                    edit_location=null;
                    et_floor.setText("");
                    et_blockName.setText("");

                    String[] from = new String[] {
                            LocationDAO.LocationEntry._ID,
                            LocationDAO.LocationEntry.COLUMN_NAME_BLOCK_NAME,
                            LocationDAO.LocationEntry.COLUMN_NAME_FLOOR
                    };

                    int[] to = new int[] { R.id.location_id, R.id.location_nameOfBlock, R.id.location_floorNumber };

                    adapter = new SimpleCursorAdapter(AddLocation.this, R.layout.view_locations, dao.readData(), from, to);

                    adapter.notifyDataSetChanged();
                    lv_location.setAdapter(adapter);
                }
            }
        });



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
            String[] from = new String[] {
                    LocationDAO.LocationEntry._ID,
                    LocationDAO.LocationEntry.COLUMN_NAME_BLOCK_NAME,
                    LocationDAO.LocationEntry.COLUMN_NAME_FLOOR
            };

            int[] to = new int[] { R.id.location_id, R.id.location_nameOfBlock, R.id.location_floorNumber };

            adapter = new SimpleCursorAdapter(this, R.layout.view_locations, dao.readData(), from, to);

            adapter.notifyDataSetChanged();
            lv_location.setAdapter(adapter);

		}

	}
}
