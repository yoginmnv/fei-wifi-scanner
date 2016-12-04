package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

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
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;

/**
 * Created by Patrik on 1.12.2016.
 */

public class WifiScanAdapter extends BaseAdapter implements View.OnClickListener{
    private List<ScanResult> wifiList;
    private static LayoutInflater inflater;

    public WifiScanAdapter(ManageWifi activity, List<ScanResult> data)
    {
        // Set the data
        wifiList = data;

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
            convertView = inflater.inflate(R.layout.view_wifi_scans, null);

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
        ScanResult scanResult = wifiList.get(position);
        viewHolder.textViewSSID.setText("SSID: " + scanResult.SSID);
        viewHolder.textViewBSSID.setText("BSSID: " + scanResult.BSSID);

        return convertView;
    }

    public void updateList(List<ScanResult> list)
    {
        wifiList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }
}