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
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        _buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        _buttonSignOut.setOnClickListener(this);

        _welcomeText = (TextView) findViewById(R.id.labelSignedInWelcome);

        ArrayList<String> lines = new ArrayList<>();
        String path = (String) getIntent().getExtras().get("File");
        File pro_file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(pro_file));
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "NOOOOOOO", Toast.LENGTH_LONG).show();
        }

        userInfo = new UserInfo(lines);
        userInfo.username = (String) getIntent().getExtras().get("User");

        _welcomeText.setText("Welcome " + userInfo.username + " from " + userInfo.origin.toString());

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
