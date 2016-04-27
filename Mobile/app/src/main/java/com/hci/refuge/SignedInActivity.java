package com.hci.refuge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class SignedInActivity extends AppCompatActivity implements View.OnClickListener {

    Button _buttonSignOut;
    TextView _welcomeText;
    public static UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        _buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        _buttonSignOut.setOnClickListener(this);

        _welcomeText = (TextView) findViewById(R.id.labelSignedInWelcome);
        _welcomeText.setText("Welcome " + userInfo.username + " from " + userInfo.origin.toString());

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        userInfo = null;
        startActivity(intent);
    }
}
