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
    String groupeName;
    String user;
    Bundle tmp=new Bundle();
    ArrayList<Message> messages = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
           // messages =(ArrayList) this.getIntent().getParcelableArrayListExtra("messages");
         TextView t = (TextView) findViewById(R.id.GroupName);
         t.setText(groupeName);//put the group name
        final ListView listview = (ListView) findViewById(R.id.listView);
        groupe_id=this.getIntent().getStringExtra("GroupId");
        groupeName=this.getIntent().getStringExtra("groupNam");
        user=this.getIntent().getStringExtra("user");

        String lien = "http://bites.factorycampus.net/ListMsg.php?group_id="+groupe_id+"&user="+user+"";
        StringBuilder sb ;
        String result ;
        JSONObject json_data ;
        sb = JsonToPhp.getData(lien, true);

        result = sb.toString();

        try {
            JSONArray jArray = new JSONArray(result);
           // String[][] chat = new String[jArray.length()][2];
            for (int i = 0; i < jArray.length(); i++) {
                Message m = new Message();
                json_data = jArray.getJSONObject(i);
                m.setUser(json_data.getString("username"));
                m.setMsg(json_data.getString("msg"));
                messages.add(m);
            }
            // String[][] chat = new String[][]{{userName},{message}};
            List<HashMap<String, String>> liste = new
                    ArrayList<HashMap<String, String>>();

            HashMap<String, String> element;

            for (int i = 0; i < messages.size(); i++) {
                //… on crée un élément pour la liste…
                element = new HashMap<String, String>();

                element.put("text1", messages.get(i).getUser());

                element.put("text2", messages.get(i).getMsg());
                liste.add(element);
            }

            ListAdapter adapter = new SimpleAdapter(this,
                    liste, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2});
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }
        tmp = savedInstanceState;
    }
/*public void onBackPressed()
{

    Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
    i.putExtra("messages",messages);
    startActivity(i);
}*/

    public void EnvoyerMessage(View view) {

        EditText tonEdit = (EditText) findViewById(R.id.message);
        String message = tonEdit.getText().toString();
        String lien = "http://bites.factorycampus.net/AddMsg.php?user="+user+"&group_id="+groupe_id+"&msg=" +message+ "";
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
        onCreate(tmp);
    }

}