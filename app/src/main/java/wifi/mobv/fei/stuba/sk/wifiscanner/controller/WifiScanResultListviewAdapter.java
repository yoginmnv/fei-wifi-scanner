package wifi.mobv.fei.stuba.sk.wifiscanner.controller;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ScanWifiActivity;

/**
 * Created by Patrik on 1.12.2016.
 */

public class WifiScanResultListviewAdapter extends BaseAdapter implements View.OnClickListener{
    private ScanWifiActivity activity;
    private List<ScanResult> wifiList;
    private static LayoutInflater inflater = null;
    public Resources resoruces;

    public WifiScanResultListviewAdapter(ScanWifiActivity actvt, List<ScanResult> data, Resources res)
    {
        // Set values
        activity = actvt;
        wifiList = data;
        resoruces = res;

        // Layout inflater
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return wifiList.size();
    }

    public ScanResult getItem(int position)
    {
        return wifiList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    // Viewholder class for data
    public static class ViewHolder
    {
        public TextView textView;
    }

    // Function to create rows
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate view for row
            view = inflater.inflate(R.layout.wifi_scan_listview_row, null);

            // Create Viewholder object which contains row elements
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView)view.findViewById(R.id.WifiScanText_id);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        ScanResult scanResult = wifiList.get(position);
        viewHolder.textView.setText(scanResult.toString());

        return view;
    }
    @Override
    public void onClick(View view) {

    }
}
