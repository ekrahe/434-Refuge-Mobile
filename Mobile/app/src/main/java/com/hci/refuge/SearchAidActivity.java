package com.hci.refuge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchAidActivity extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView _searchBox;
    ImageButton _searchButton;
    public static UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_aid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        ArrayAdapter<SearchOptions> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, SearchOptions.values());
        _searchBox = (AutoCompleteTextView) findViewById(R.id.fieldSearchBox);
        _searchBox.setAdapter(adapter);

        _searchButton = (ImageButton) findViewById(R.id.buttonSearchAid);
        _searchButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropdown_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sign_out) {
            final Context c = this;
            new AlertDialog.Builder(this).setMessage("Do you want to sign out?")
                    .setNegativeButton("Cancel", null).
                    setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userInfo = null;
                            Intent intent = new Intent(c, MainActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        try {
            SearchOptions type = SearchOptions.valueOf(_searchBox.getText().toString());
            Toast.makeText(this, "Looking for " + type + " within " + userInfo.travelDistance + "km", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Search using one of the suggested aid types.", Toast.LENGTH_LONG).show();
        }
    }
}
