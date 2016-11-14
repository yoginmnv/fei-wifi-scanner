package com.example.feri.database;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Feri on 11.11.2016.
 */

public class Display extends AppCompatActivity{
    int from_Where_I_Am_Coming = 0;
    private DatabaseHandler db;
    TextView polozka1;
    TextView polozka2;
    int id_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        polozka1=(TextView) findViewById(R.id.polozka1);
        polozka2=(TextView) findViewById(R.id.polozka2);



        db = new DatabaseHandler(this);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int value = extras.getInt("id");

            if(value>0) {
                Cursor rs = db.getData(value);
                id_update=value;
                rs.moveToFirst();
                String poloz1=rs.getString(rs.getColumnIndex(DatabaseHandler.KEY_NAME));
                String poloz2=rs.getString(rs.getColumnIndex(DatabaseHandler.KEY_PH_NO));

                if(!rs.isClosed()){
                    rs.close();
                }

                Button b=(Button) findViewById(R.id.button);
                b.setVisibility(View.INVISIBLE);

                polozka1.setText((CharSequence) poloz1);
                polozka1.setFocusable(false);
                polozka1.setClickable(false);

                polozka2.setText((CharSequence) poloz2);
                polozka2.setFocusable(false);
                polozka2.setClickable(false);

            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.display, menu);
            } else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.edit:
                Button b = (Button) findViewById(R.id.button);
                b.setVisibility(View.VISIBLE);

                polozka1.setEnabled(true);
                polozka1.setFocusableInTouchMode(true);
                polozka1.setClickable(true);

                polozka2.setEnabled(true);
                polozka2.setFocusableInTouchMode(true);
                polozka2.setClickable(true);

                return true;

            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Naozaj vymazať?")
                        .setPositiveButton("Áno", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteContact(id_update);
                                Toast.makeText(getApplicationContext(), "Vymazanie uspesne",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Vymazanie zrusene",Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Naozaj");

                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int value = extras.getInt("id");
            if(value>0) {
                if(db.updateContant(id_update,polozka1.getText().toString(),polozka2.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"not Updated",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(db.insertConact(polozka1.getText().toString(),polozka2.getText().toString())){
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }

}
