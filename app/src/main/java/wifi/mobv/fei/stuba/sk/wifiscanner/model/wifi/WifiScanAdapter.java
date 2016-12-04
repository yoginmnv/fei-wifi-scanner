package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.R;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.ManageWifi;

/**
 * Created by Patrik on 1.12.2016.
 */

public class WifiScanAdapter extends BaseAdapter implements View.OnClickListener{
    private List<Wifi> wifiList;
    private static LayoutInflater inflater;

    public WifiScanAdapter(ManageWifi activity)
    {
        // Initialize list
        wifiList = new ArrayList<>();

        // Layout inflater
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return wifiList.size();
    }

    public Wifi getItem(int position)
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
        public TextView tv_SSID;

        // BSSID
        public TextView tv_BSSID;

        // LocationId
        public TextView tv_locationId;

        public TextView tv_signalLevel;
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
            viewHolder.tv_SSID = (TextView)convertView.findViewById(R.id.WifiScanTextSSID);
            viewHolder.tv_BSSID = (TextView)convertView.findViewById(R.id.WifiScanTextBSSID);
            viewHolder.tv_locationId = (TextView)convertView.findViewById(R.id.WifiScanTextLocationId);
            viewHolder.tv_signalLevel = (TextView)convertView.findViewById(R.id.WifiScanTextSignalLevel);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Get the result from list
        Wifi wifiScan = wifiList.get(position);

        // Set content data
        viewHolder.tv_SSID.setText("SSID: " + wifiScan.getSSID());
        viewHolder.tv_BSSID.setText("BSSID: " + wifiScan.getBSSID());

        long locationId = wifiScan.getLocationID();
        // When locationID is set to it's default value -1, the text in Textview is substituted to string "Not available"
        viewHolder.tv_locationId.setText("Location ID: " + (locationId != -1 ? String.valueOf(locationId) : "Not available"));

        viewHolder.tv_signalLevel.setText("Signal level: " + wifiScan.getMaxLevel());

        return convertView;
    }

    public void updateList(List<Wifi> list)
    {
        wifiList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }
}