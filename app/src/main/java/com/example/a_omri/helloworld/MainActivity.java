package com.example.a_omri.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//test Syll Ababacar

//test Amira Zaafouri
public class MainActivity extends Activity {

    TextView registerScreen;
    EditText txtUserName;
    Button btnLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        registerScreen = (TextView) findViewById(R.id.link_to_register);
        txtUserName = (EditText) findViewById(R.id.pseudoname);
        btnLogin = (Button) findViewById(R.id.btnStart);
        ConnectivityManager cmanager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cmanager.getActiveNetworkInfo();
        final LocationManager manager = (LocationManager) getSystemService( this.LOCATION_SERVICE );

        if(!(netinfo != null && netinfo.isConnected() &&  manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))) {
            //Toast.makeText(this, "plz turn ON your internet connection !!", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Connection erreur")
                    .setMessage("plz turn ON your internet and GPS connection !!")
                    .setCancelable(false)
                    .setPositiveButton("ok", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }
    }

        public void LoginTest(View view)
        {
                    if(view.getId()==R.id.link_to_register) {
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                    }else if(view.getId()==R.id.btnStart) {
                        String strUserName = txtUserName.getText().toString();
                        if (TextUtils.isEmpty(strUserName))
                            Toast.makeText(this, "plz enter your name !!", Toast.LENGTH_SHORT).show();
                        else {
                            Intent i = new Intent(getApplicationContext(), ListGroupActivity.class);
                            startActivity(i);

                        }
                    }


        }
}