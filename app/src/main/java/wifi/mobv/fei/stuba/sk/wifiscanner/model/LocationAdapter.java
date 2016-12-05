package wifi.mobv.fei.stuba.sk.wifiscanner.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;

/**
 * Created by maros on 4.12.2016.
 */

public class LocationAdapter extends ArrayAdapter<Location>
{
	public LocationAdapter(Context context, int textViewResourceId, List<Location> list)
	{
		super(context, textViewResourceId, list);
	}

	@Override //don't override if you don't want the default spinner to be a two line view
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return initView(position, convertView);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return initView(position, convertView);
	}

	private View initView(int position, View convertView)
	{
		if( convertView == null )
		{
			convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
		}

		TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);

		tvText1.setText(getItem(position).getBlockName() + getItem(position).getFloor());

		return convertView;
	}
}