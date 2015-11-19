package com.example.a_omri.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by A-OMRI on 05/11/2015.
 */
public class AddGroupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to addgroup.xml
        setContentView(R.layout.addgroup);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing addgroup screen
                finish();
            }
        });
    }
}