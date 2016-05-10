package com.hci.refuge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _passwordView, _usernameView;
    private Button _buttonSignIn, _buttonSignUp;

    /**
     * MainActivity is a login Activity, where the user can sign in to an existing account
     * or create a new one
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _usernameView = (EditText) findViewById(R.id.loginUsername);
        _passwordView = (EditText) findViewById(R.id.loginPassword);

        _buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        _buttonSignIn.setOnClickListener(this);

        _buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        _buttonSignUp.setOnClickListener(this);

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    SearchAidActivity.COARSE_LOCATION_CODE);
        }

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    666);
        }

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    42);
        }
    }

    /**
     * The Sign In button checks that username.txt exists,
     * and that the entered password matches the one stored in username.txt.
     * If it does, the user is logged in and sent to SearchAidActivity.
     * If it doesn't the user is told given an error message.
     *
     * The Sign Up button sends the user to CreateAccountActivity
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSignIn:
                File pro_file = new File(getFilesDir(), _usernameView.getText().toString() + ".txt");
                if (!pro_file.exists()) {
                    Toast.makeText(this, "No such user.", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<String> lines = new ArrayList<>();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(pro_file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    br.close();
                }
                catch (Exception e) {
                    Toast.makeText(this, "Unable to read user file.", Toast.LENGTH_LONG).show();
                }
                UserInfo userInfo = new UserInfo(lines);

                if (userInfo.password.equals(_passwordView.getText().toString())) {
                    intent = new Intent(this, SearchAidActivity.class);
                    userInfo.username = _usernameView.getText().toString();
                    SearchAidActivity.userInfo = new UserInfo(userInfo);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Incorrect password.", Toast.LENGTH_LONG).show();
                    return;
                }

                break;
            case R.id.buttonSignUp:
                intent = new Intent(this, CreateAccountActivity.class);
                startActivity(intent);
                break;
        }
    }

}
