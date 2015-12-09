package com.example.a_omri.helloworld;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by A-OMRI on 05/11/2015.update Syll.ababacar
 */
public class ListGroupActivity extends Activity {
    ListView listView = null ;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.listegroups);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            listView = (ListView) findViewById(R.id.listView);
        }
        String result = new String();
        InputStream is = null;
        JSONObject json_data=null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        ArrayList<String> donnees = new ArrayList<String>();

        try {
            //commandes httpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bites.factorycampus.net/test.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }   catch(Exception e){
                Log.i("taghttppost", "" + e.toString());
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }


        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            StringBuilder sb  = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();

            result = sb.toString();
        }
        catch(Exception e)
        {
            Log.i("tagconvertstr", "" + e.toString());
        }
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {

                json_data = jArray.getJSONObject(i);
                donnees.add(json_data.getString("name"));
                //r.add(json_data.getString("categorie"));
            }
       ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,donnees);
            listView.setAdapter(adapter);
           // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, donnees));
        }
        catch(JSONException e){
            Log.e("taghttppost","Erreur récupération JSON",e);
        } catch (ParseException e) {
            Log.e("taghttppost", "Erreur récupération JSON", e);
        }



            TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        /*loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen

                finish();
            }
        });*/
    }
}
