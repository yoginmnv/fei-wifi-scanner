package wifi.mobv.fei.stuba.sk.wifiscanner.model.wifi;

import java.io.Serializable;

/**
 * Created by patrik.michlo on 01/12/16.
 */

public class WifiScan implements Serializable {
    public String SSID;
    public String BSSID;

    public WifiScan(String ssid, String bssid)
    {
        SSID = ssid;
        BSSID = bssid;
    }
}
