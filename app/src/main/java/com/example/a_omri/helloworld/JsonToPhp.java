package com.example.a_omri.helloworld;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ababacar Syll on 14/12/2015.
 */
public class JsonToPhp {

    public static StringBuilder getData(String lien,boolean type)
    {
         StringBuilder sb = new StringBuilder();
         InputStream is = null;
         ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(lien);
            HttpResponse response = httpclient.execute(httppost);
            if(type == true)
            {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.i("taghttppost", "erreur" , e);
         //  Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line ;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();

        } catch (Exception e) {
            Log.i("tagconvertstr", "erreur de reader" , e);
        }
        return sb;
    }
}
