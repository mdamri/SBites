package com.example.a_omri.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final ListView listview = (ListView) findViewById(R.id.listView);
        String result = new String();
        InputStream is = null;
        JSONObject json_data = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        ArrayList<String> donnees = new ArrayList<String>();

        try {
            //commandes httpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bites.factorycampus.net/ListGroup.php?user="+p+"&lat=34.777401&long=10.726717");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.i("taghttppost", "" + e.toString());
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();

            result = sb.toString();
        } catch (Exception e) {
            Log.i("tagconvertstr", "" + e.toString());
        }
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