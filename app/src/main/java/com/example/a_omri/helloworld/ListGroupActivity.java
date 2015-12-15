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
 * Created by A-OMRI on 05/11/2015.
 */
public class ListGroupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to addgroup.xml
        setContentView(R.layout.listegroups);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String p = this.getIntent().getStringExtra("pseudo");
        TextView t = (TextView) findViewById(R.id.pseudo);
        t.setText(p);
        final ListView listview = (ListView) findViewById(R.id.listView);
        String lien = "http://bites.factorycampus.net/ListGroup.php?user="+p+"&lat=34.777401&long=10.726717";
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
                donnees.add(json_data.getString("name"));
                //r.add(json_data.getString("categorie"));
            }
            ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, donnees);
            listview.setAdapter(adapter);
            // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, donnees));
        } catch (JSONException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }
    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) l.getItemAtPosition(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }


    public void AjoutGroup(View v) {
        Intent i = new Intent(getApplicationContext(), AddGroupActivity.class);
        startActivity(i);
    }


}