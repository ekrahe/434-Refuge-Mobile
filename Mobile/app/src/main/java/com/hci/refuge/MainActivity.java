package com.hci.refuge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _passwordView, _usernameView;
    private Button _buttonSignIn, _buttonSignUp;

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
    }

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

                String line;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(pro_file));
                    line = br.readLine();
                    br.close();
                }
                catch (Exception e) {
                    Toast.makeText(this, "Unable to read user profile.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (line.equals(_passwordView.getText().toString())) {
                    intent = new Intent(this, SignedInActivity.class);
                    intent.putExtra("File", pro_file.getAbsolutePath());
                    intent.putExtra("User", _usernameView.getText().toString());

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
