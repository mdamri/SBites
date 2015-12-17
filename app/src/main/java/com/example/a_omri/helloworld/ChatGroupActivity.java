package com.example.a_omri.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *chatRoomActivity Dridi
 */
public class ChatGroupActivity extends Activity {
    String groupe_id;
    String msg;
    String user;
    EditText id;
    EditText utilisateur;
    EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String p = this.getIntent().getStringExtra("Groupe");
        TextView t = (TextView) findViewById(R.id.GroupName);
        t.setText(p);//put the group name
        final ListView listview = (ListView) findViewById(R.id.listView);

        groupe_id = t.getText().toString();
        utilisateur = (EditText) findViewById(R.id.message);
        user = utilisateur.getText().toString();

        String lien = "http://bites.factorycampus.net/ListMsg.php?group_id="+groupe_id+"&user="+user+"";
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
            }
            ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice ,donnees);
            listview.setAdapter(adapter);//mettre le contenue dans l'adapter puit l'affecter a la liste
        } catch (JSONException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }
    }



    public void EnvoyerMessage(View view) {

        EditText tonEdit = (EditText) findViewById(R.id.message);
        String message = tonEdit.getText().toString();
        msg=message;
        TextView t = (TextView) findViewById(R.id.GroupName);
        groupe_id = t.getText().toString();
        utilisateur = (EditText) findViewById(R.id.message);
        user = utilisateur.getText().toString();

        String lien = "http://bites.factorycampus.net/AddMsg.php?user="+user+"&group_id="+groupe_id+"&msg="+msg+"";
        StringBuilder sb ;
        String result = new String();
        JSONObject json_data ;
        sb = JsonToPhp.getData(lien, false);
        try{
            json_data = new JSONObject(sb.toString());
            result = json_data.getString("msg");

        } catch (Exception e) {
            Log.i("tagconvertstr", "Erreur de json", e);
        }

    }
}