package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.content.Context;
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

public class WifiScanResultListviewAdapter extends BaseAdapter {
    private List<WifiScan> wifiList;
    private static LayoutInflater inflater;

    public WifiScanResultListviewAdapter(ScanWifiActivity activity, List<WifiScan> data)
    {
        // Set the data
        wifiList = data;

        // Inflate layout
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return wifiList.size();
    }

    public WifiScan getItem(int position)
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
        // SSID
        public TextView textViewSSID;

        // BSSID
        public TextView textViewBSSID;
    }

    // Function to create rows
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate view for row
            convertView = inflater.inflate(R.layout.wifi_scan_listview_row, null);

            // Create Viewholder object which contains row elements
            viewHolder = new ViewHolder();
            viewHolder.textViewSSID = (TextView)convertView.findViewById(R.id.WifiScanTextSSID);
            viewHolder.textViewBSSID = (TextView)convertView.findViewById(R.id.WifiScanTextBSSID);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Get the result from list
        WifiScan wifiScan = wifiList.get(position);

        viewHolder.textViewSSID.setText("SSID: " + wifiScan.SSID);
        viewHolder.textViewBSSID.setText("BSSID: " + wifiScan.BSSID);

        return convertView;
    }

    public void updateList(List<WifiScan> list)
    {
        wifiList = list;
        this.notifyDataSetChanged();
    }
}
