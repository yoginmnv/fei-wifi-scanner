package com.example.feri.sqlitedemo;

/**
 * Created by Feri on 25.11.2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Modify_member extends AppCompatActivity implements OnClickListener {

    EditText ssid, bssid, signal, poschodie, blok ;
    Button edit_bt, delete_bt;

    long member_id;

    SQLController dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify_member);

        dbcon = new SQLController(this);
        dbcon.open();

        ssid = (EditText) findViewById(R.id.SSID_edit);
        bssid = (EditText) findViewById(R.id.BSSID_edit);
        signal = (EditText) findViewById(R.id.signal_edit);
        poschodie = (EditText) findViewById(R.id.poschodie_edit);
        blok = (EditText) findViewById(R.id.blok_edit);

        edit_bt = (Button) findViewById(R.id.update_bt_id);
        delete_bt = (Button) findViewById(R.id.delete_bt_id);

        Intent i = getIntent();
        String memberID = i.getStringExtra("memberID");

        String memberSSID = i.getStringExtra("memberSSID");
        String memberBSSID = i.getStringExtra("memberBSSID");
        String memberSignal = i.getStringExtra("memberSignal");
        String memberPoschodie = i.getStringExtra("memberPoschodie");
        String memberBlok = i.getStringExtra("memberBlok");


        member_id = Long.parseLong(memberID);


        ssid.setText(memberSSID);
        bssid.setText(memberBSSID);
        signal.setText(memberSignal);
        poschodie.setText(memberPoschodie);
        blok.setText(memberBlok);

        blok.setText(memberBlok);


        edit_bt.setOnClickListener(this);
        delete_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_bt_id:
                String bssid_upd = bssid.getText().toString();
                String ssid_upd = ssid.getText().toString();

                String signal_upd = signal.getText().toString();
                String poschodie_upd = poschodie.getText().toString();
                String blok_upd = blok.getText().toString();

                dbcon.updateData(member_id,ssid_upd,bssid_upd,signal_upd,poschodie_upd,blok_upd);
                this.returnHome();
                break;

            case R.id.delete_bt_id:
                dbcon.deleteData(member_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}
