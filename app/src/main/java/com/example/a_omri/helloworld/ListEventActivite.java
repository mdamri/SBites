package com.example.a_omri.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by salma on 15/12/2015.
 */
public class ListEventActivite extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to event.xml
        setContentView(R.layout.listevents);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String GroupID =  this.getIntent().getStringExtra("GroupID");
        String p = this.getIntent().getStringExtra("GroupName");
        //TextView t = (TextView) findViewById(R.id.GroupName);
        //t.setText(p);
        final ListView listview = (ListView) findViewById(R.id.listView);
        String lien = "http://bites.factorycampus.net/ListEvent.php?group_id="+GroupID+"";
        StringBuilder sb ;
        String result ;
        JSONObject json_data ;
        ArrayList<String> donnees = new ArrayList();
        sb = JsonToPhp.getData(lien,true);

        result = sb.toString();

        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {

                json_data = jArray.getJSONObject(i);
                donnees.add(json_data.getString("nom"));
            }
            ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, donnees);
            listview.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }
    }





    public void AjoutEvent(View v) {
        Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
        startActivity(i);
    }

}
