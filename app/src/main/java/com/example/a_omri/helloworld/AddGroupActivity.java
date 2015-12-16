package com.example.a_omri.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by A-OMRI on 05/11/2015.
 */
public class AddGroupActivity extends Activity {
    //--------------------
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
    // a voir
    public void AddGroup(View view) {
        nomGroupe = nom.getText().toString();
        description = desc.getText().toString();
        String lien = "http://bites.factorycampus.net/AddGroup.php?groupe_name="+nomGroupe+"&groupe_description="+description+"&lat=74.3170&long=10.755368";
        StringBuilder sb ;
        String result = new String();
        JSONObject json_data ;
           sb = JsonToPhp.getData(lien, false);
        try{
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
            Log.i("tagconvertstr", "Erreur de json", e);
        }

    }
    
}