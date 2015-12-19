package com.example.a_omri.helloworld;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class ListGroupActivity extends Activity implements LocationListener {

    ArrayList messageArrayList ;
    private double longitude;
    private double latitude;
    Location location;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to addgroup.xml
        setContentView(R.layout.listegroups);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

       /* if(this.getIntent().getStringExtra("messages")== null) {
            ArrayList<Message> messageArrayList = new ArrayList<Message>();
        }
        else {
            messageArrayList = this.getIntent().getStringArrayListExtra("messages");
        }*/
        final String p = this.getIntent().getStringExtra("pseudo");

        TextView t = (TextView) findViewById(R.id.pseudo);
        t.setText(p);
        final ListView listview = (ListView) findViewById(R.id.listView);
        String lien = "http://bites.factorycampus.net/ListGroup.php?user="+p+"&lat="+latitude +"&long="+longitude;
        StringBuilder sb ;
        String result ;
        JSONObject json_data ;
        final ArrayList<String> donnees = new ArrayList();
        final ArrayList<String> donneesId = new ArrayList();
        sb = JsonToPhp.getData(lien, true);

            result = sb.toString();

        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {

                json_data = jArray.getJSONObject(i);
                donnees.add(json_data.getString("name"));
                donneesId.add(json_data.getString("id"));

            }
            ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, donnees);
            listview.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String item = (String) listview.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), ChatGroupActivity.class);
                i.putExtra("GroupId",donneesId.get(position));
                i.putExtra("user",p);
                i.putExtra("groupName",donnees.get(position));
             //   i.putExtra("messages",messageArrayList);
                startActivity(i);
            }
        });

    }





    public void AjoutGroup(View v) {
        Intent i = new Intent(getApplicationContext(), AddGroupActivity.class);
        startActivity(i);
    }


    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getAltitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}