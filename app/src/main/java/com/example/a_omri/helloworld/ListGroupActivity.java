package com.example.a_omri.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by A-OMRI on 05/11/2015.
 */
public class ListGroupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        String p = this.getIntent().getStringExtra("pseudo");
        super.onCreate(savedInstanceState);
        // Set View to addgroup.xml
        setContentView(R.layout.listegroups);
        TextView t = (TextView) findViewById(R.id.pseudo);
        t.setText(p);
        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[] { "SBZ", "SOUSSE1", "SFAX2",
                "Black", "Webdev", "Ubuntu", "WindowsGroup", "Max OS X",
                "fallaga", "pirat", "CA", "IEEE", "Max OF ", "Linux Formation",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1, values);
        listview.setAdapter(adapter);
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String)l.getItemAtPosition(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }


    public void AjoutGroup(View v){
            Intent i = new Intent(getApplicationContext(), AddGroupActivity.class);
            startActivity(i);
    }
}