package com.example.a_omri.helloworld;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
public class MainActivity extends Activity implements LocationListener {
    double longitude;
    double latitude;
    TextView registerScreen;
    EditText txtUserName;
    Button btnLogin;
    SharedPreferences sharedpreferences;
    String strUserName;
    static final String MyPREFERENCES = "Houmtymyprefs";
    static final String Name = "";
    Location location;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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

        registerScreen = (TextView) findViewById(R.id.link_to_register);
        txtUserName = (EditText) findViewById(R.id.pseudoname);
        btnLogin = (Button) findViewById(R.id.btnStart);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String storedUserName = sharedpreferences.getString(Name, "");
        if (!storedUserName.equals("")) {
            strUserName = storedUserName;
            Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
            i.putExtra("pseudo", strUserName);
            startActivity(i);
        } else {


            ConnectivityManager cmanager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cmanager.getActiveNetworkInfo();

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
            if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
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
                                    i.putExtra("longitude",longitude);
                                    i.putExtra("latitude",latitude);
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
