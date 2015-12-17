package com.example.a_omri.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *chatRoomActivity Dridi
 */
public class ChatGroupActivity extends Activity {
    String groupe_id;
    String msg;
    String user;
    EditText id;
    EditText utilisateur;
    EditText messageEnvo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // String p = this.getIntent().getStringExtra("Groupe");
        // TextView t = (TextView) findViewById(R.id.GroupName);
        // t.setText(p);//put the group name
        final ListView listview = (ListView) findViewById(R.id.listView);

        // groupe_id = t.getText().toString();
        utilisateur = (EditText) findViewById(R.id.message);
        user = utilisateur.getText().toString();

        String lien = "http://bites.factorycampus.net/ListMsg.php?group_id=1&user=med";
        StringBuilder sb ;
        String result ;
        JSONObject json_data ;
        String message = new String();
        String userName = new String();
        sb = JsonToPhp.getData(lien,true);

        result = sb.toString();

        try {
            JSONArray jArray = new JSONArray(result);
            String[][] chat = new String[jArray.length()][2];
            for (int i = 0; i < jArray.length(); i++) {

                json_data = jArray.getJSONObject(i);
                chat[i][0] = (json_data.getString("username"));
                chat[i][1] = (json_data.getString("msg"));
            }
            // String[][] chat = new String[][]{{userName},{message}};
            List<HashMap<String, String>> liste = new
                    ArrayList<HashMap<String, String>>();

            HashMap<String, String> element;

            int j = 0;
            int k = 0;
            for (int i = 0; i < chat.length; i++) {
                //… on crée un élément pour la liste…
                element = new HashMap<String, String>();

                element.put("text1", chat[i][0]);

                element.put("text2", chat[i][1]);
                liste.add(element);
            }

            ListAdapter adapter = new SimpleAdapter(this,
                    liste, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2});
            listview.setAdapter(adapter);

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