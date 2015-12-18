package com.example.a_omri.helloworld;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

//test Amira Zaafouri
public class MainActivity extends Activity {
    double longitude;
    double latitude;
    TextView registerScreen;
    EditText txtUserName;
    Button btnLogin;
    SharedPreferences sharedpreferences;
    String strUserName;
    static final String MyPREFERENCES="Houmtymyprefs";
    static final String Name = "";
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        registerScreen = (TextView) findViewById(R.id.link_to_register);
        txtUserName = (EditText) findViewById(R.id.pseudoname);
        btnLogin = (Button) findViewById(R.id.btnStart);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String storedUserName = sharedpreferences.getString(Name,"");
        if(!storedUserName.equals("")){
            strUserName = storedUserName;
            Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
            i.putExtra("pseudo",strUserName);
            startActivity(i);
        }
        else {


            ConnectivityManager cmanager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cmanager.getActiveNetworkInfo();
            final LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);



            if (!(netinfo.isConnected())) {
                //Toast.makeText(this, "plz turn ON your internet connection !!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Connection erreur")
                        .setMessage("plz turn ON your internet !!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(i);
                            }
                        }).create().show();
            }
            if (!(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                //Toast.makeText(this, "plz turn ON your internet connection !!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Connection erreur")
                        .setMessage("plz turn ON your GPS connection !!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(i);
                            }
                        }).create().show();
            }
        }


    }

        public void LoginTest(View view)
        {
                    if(view.getId()==R.id.link_to_register) {
                        Intent i = new Intent(getApplicationContext(), AddGroupActivity.class);
                        startActivity(i);
                    }else if(view.getId()==R.id.btnStart) {
                        strUserName = txtUserName.getText().toString();
                        if (TextUtils.isEmpty(strUserName))
                            Toast.makeText(this, "plz enter your name !!"+longitude+"--"+latitude, Toast.LENGTH_SHORT).show();
                        else {
                            String lien = "http://bites.factorycampus.net/CheckUser.php?user="+strUserName+"";
                            StringBuilder sb ;

                            String result ;
                            JSONObject json_data ;
                            sb = JsonToPhp.getData(lien, false);

                            try{
                                json_data = new JSONObject(sb.toString());
                                result = json_data.getString("msg");
                                if (result.equals("OK"))
                                {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Name, strUserName);
                                    editor.commit();

                                    Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
                                    i.putExtra("pseudo",strUserName);
                                    startActivity(i);
                                }
                                else if(result.equals("EXIST")) {
                                    Toast.makeText(this, "Pseudo existant", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.i("tagconvertstr", "Erreur de json", e);
                            }

                        }
                    }


        }
}
