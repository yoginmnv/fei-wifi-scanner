package wifi.mobv.fei.stuba.sk.wifiscanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.AddWifi;
import wifi.mobv.fei.stuba.sk.wifiscanner.view.UpdateWifi;

public class MainActivity extends AppCompatActivity {

    Button addmem_bt;
    ListView lv;
    SQLController dbcon;
    TextView memID_tv, memBlok_tv, memMac_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbcon = new SQLController(this);
        dbcon.open();

        addmem_bt = (Button) findViewById(R.id.addmem_bt_id);
        lv = (ListView) findViewById(R.id.memberList_id);
        // onClickListiner for addmember Button
        addmem_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_mem = new Intent(MainActivity.this, AddWifi.class);
                startActivity(add_mem);
            }
        });

        // Attach The Data From DataBase Into ListView Using Crusor Adapter
        Cursor cursor = dbcon.readData();
        String[] from = new String[] { DBHelper.MEMBER_ID, DBHelper.BSSID};
        int[] to = new int[] { R.id.member_id, R.id.member_BSSID };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.view_member_entry, cursor, from, to);

        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);

        // OnCLickListiner For List Items
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                memID_tv = (TextView) view.findViewById(R.id.member_id);

                String memberID_val = memID_tv.getText().toString();
                Cursor cursor = dbcon.readDataById(memberID_val);

                Intent modify_intent = new Intent(getApplicationContext(), UpdateWifi.class);
                modify_intent.putExtra("memberID", memberID_val);
                modify_intent.putExtra("memberSSID",cursor.getString(1));
                modify_intent.putExtra("memberBSSID", cursor.getString(2));
                modify_intent.putExtra("memberSignal",cursor.getString(3));
                modify_intent.putExtra("memberPoschodie",cursor.getString(4));
                modify_intent.putExtra("memberBlok",cursor.getString(5));

                startActivity(modify_intent);
            }
        });
    }
}