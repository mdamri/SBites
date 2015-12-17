package com.example.a_omri.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by salma on 15/12/2015.
 */
public class AddEventActivity extends Activity {
    EditText nom  ;
    EditText desc;
    String nomEvent ;
    String description;
    TimePicker tempsDebut;
    TimePicker tempsFin;
    DatePicker dateDebut;
    DatePicker dateFin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View to addEvent.xml
        setContentView(R.layout.addevent);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        nom  = (EditText) findViewById(R.id.eventName);
        desc = (EditText) findViewById(R.id.Description);
        TextView groupScreen = (TextView) findViewById(R.id.link_to_group);

        // Listening to Login Screen link
       groupScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing addevent screen
                finish();
            }
        });
    }

    public void AddEvent(View view) {
        nomEvent = nom.getText().toString();
        description = desc.getText().toString();

        int minD = tempsDebut.getMinute();
        int heureD=tempsDebut.getHour();
        int anneeD= dateDebut.getYear();
        int moisD=dateDebut.getMonth();
        int jourD=dateDebut.getDayOfMonth();

        String  dateDebutEvent=anneeD+"-"+moisD+"-"+jourD+" "+heureD+":"+minD+":"+"00";

        TextView gpe= (TextView) findViewById(R.id.GroupName);

        int minF = tempsFin.getMinute();
        int heureF=tempsFin.getHour();
        int anneeF= dateFin.getYear();
        int moisF=dateFin.getMonth();
        int jourF=dateFin.getDayOfMonth();
        String  dateFinEvent=anneeF+"-"+moisF+"-"+jourF+" "+heureF+":"+minF+":"+"00";
        String lien = "http://bites.factorycampus.net/AddEvent.php?nom="+nomEvent+"&description="+description+"&date_debut="+dateDebutEvent+"&date_fin="+dateFinEvent+
         "&group_id="+gpe;

        StringBuilder sb ;
        String result = new String();
        JSONObject json_data ;
        sb = JsonToPhp.getData(lien, false);
        try{
            json_data = new JSONObject(sb.toString());
            result = json_data.getString("msg");
            if (result.equals("OK"))
            {
                Toast.makeText(this, "événement crée avec succé", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ListEventActivite.class);
                startActivity(i);
            }else {
                Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.i("tagconvertstr", "Erreur de json", e);
        }

    }

}
