package wifi.mobv.fei.stuba.sk.wifiscanner.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;

/**
 * Created by maros on 3.12.2016.
 */

public class LocationAdapter extends BaseAdapter implements ListAdapter
{
	private List<Location> list = new ArrayList<Location>();
	private Context context;

	public LocationAdapter(List<Location> list, Context context)
	{
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int i)
	{
		return list.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return list.get(i).getId();
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup)
	{
		if( view == null )
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_location, null);
		}

//		//Handle TextView and display string from your list
//		TextView listItemText = (TextView) view.findViewById(R.id.tv_location_info);
//		if(list.size() != 0 )
//		{
//			listItemText.setText("Block " + list.get(i).getBlockName() + " - " + list.get(i).getFloor() + ". floor");
//		}
//
//		//Handle buttons and add onClickListeners
//		Button b_edit = (Button)view.findViewById(R.id.b_location_edit);
//		Button b_delete = (Button)view.findViewById(R.id.b_location_delete);
//
//		b_edit.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				//do something
//				list.remove(i); //or some other task
//				notifyDataSetChanged();
//			}
//		});
//
//		b_delete.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				//do something
//				notifyDataSetChanged();
//			}
//		});

		return view;
	}
}
