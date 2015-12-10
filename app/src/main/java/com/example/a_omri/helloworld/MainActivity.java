package com.example.a_omri.helloworld;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//test Amira Zaafouri
public class MainActivity extends Activity {
    double longitude;
    double latitude;
    TextView registerScreen;
    EditText txtUserName;
    Button btnLogin;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        registerScreen = (TextView) findViewById(R.id.link_to_register);
        txtUserName = (EditText) findViewById(R.id.pseudoname);
        btnLogin = (Button) findViewById(R.id.btnStart);
        ConnectivityManager cmanager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cmanager.getActiveNetworkInfo();
        final LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        if (!( netinfo.isConnected())) {
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
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

        public void LoginTest(View view)
        {
                    if(view.getId()==R.id.link_to_register) {
                        Intent i = new Intent(getApplicationContext(), AddGroupActivity.class);
                        startActivity(i);
                    }else if(view.getId()==R.id.btnStart) {
                        String strUserName = txtUserName.getText().toString();
                        if (TextUtils.isEmpty(strUserName))
                            Toast.makeText(this, "plz enter your name !!"+longitude+"--"+latitude, Toast.LENGTH_SHORT).show();
                        else {

                            Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
                            i.putExtra("pseudo",strUserName);
                            startActivity(i);
                        }
                    }


        }
}