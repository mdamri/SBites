package com.example.a_omri.helloworld;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by A-OMRI on 05/11/2015.
 */
public class AddGroupActivity extends Activity {
    //------------------
    EditText nom  ;
    EditText desc;
    double longitude;
    double latitude;
    String nomGroupe ;
    String description;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View to addgroup.xml
        setContentView(R.layout.addgroup);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        nom  = (EditText) findViewById(R.id.GroupName);
        desc = (EditText) findViewById(R.id.Description);
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing addgroup screen
                finish();
            }
        });
    }
    public void AddGroup(View view) {
        nomGroupe = nom.getText().toString();
        description = desc.getText().toString();
        String result = new String();
        InputStream is = null;
        JSONObject json_data = null;

        try {
            //commandes httpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bites.factorycampus.net/AddGroup.php?groupe_name="+nomGroupe+"&groupe_description="+description+"&lat=.743170&long=10.755368");
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

            json_data = new JSONObject(sb.toString());
            result = json_data.getString("msg");
            if (result.equals("OK"))
            {
                Toast.makeText(this, "groupe crée avec succée", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
                startActivity(i);
            }else if(result.equals("EXIST")) {
                Toast.makeText(this, "group exist déja", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.i("tagconvertstr", "" + e.toString());
        }

    }
    
}