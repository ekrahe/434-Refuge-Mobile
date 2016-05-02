package com.hci.refuge;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class SearchAidActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient _googleClient;

    AutoCompleteTextView _searchBox;
    ImageButton _searchButton;
    public static UserInfo userInfo;
    ListView resultsList;
    SearchResultAdapter resultsAdapter;
    private static SearchOptions search = null;

    protected static final int COARSE_LOCATION_CODE = 1243;
    private static double lat = -1, lon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_aid);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (_googleClient == null) {
            _googleClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        ArrayAdapter<SearchOptions> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, SearchOptions.values());
        _searchBox = (AutoCompleteTextView) findViewById(R.id.fieldSearchBox);
        _searchBox.setAdapter(adapter);
        _searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    _searchBox.setText(_searchBox.getText().toString().trim());
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        _searchBox.clearFocus();

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(_searchBox.getWindowToken(), 0);

                        _searchButton.callOnClick();
                    }
                    return true;
                }
                return false;
            }
        });

        _searchButton = (ImageButton) findViewById(R.id.buttonSearchAid);
        _searchButton.setOnClickListener(this);

        resultsList = (ListView) findViewById(R.id.wrapperSearchResults);
        resultsAdapter = new SearchResultAdapter(this);
        resultsList.setAdapter(resultsAdapter);
        resultsList.setOnItemClickListener(this);
        if (lat == -1) {
            lat = 38.985992;
            lon = -76.942541;
        }
        if(search != null) {
            resultsAdapter.setLatLon(lat, lon);
            resultsAdapter.setResults(EventData.getData(search));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        EventDetailsActivity.event = new EventData((EventData) resultsAdapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // don't sign out of the app!
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
            Location last = LocationServices.FusedLocationApi.getLastLocation(_googleClient);
            if (last != null) {
                lat = last.getLatitude();
                lon = last.getLongitude();
            }
        } else {
            Toast.makeText(this, "You're no fun", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        _googleClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        _googleClient.disconnect();
        super.onStop();
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
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_searchBox.getWindowToken(), 0);
        try {
            SearchOptions type = SearchOptions.valueOf(_searchBox.getText().toString());
            search = type;
            resultsAdapter.setLatLon(lat, lon);
            resultsAdapter.setResults(EventData.getData(search));
        } catch (Exception e) {
            Toast.makeText(this, "Search using one of the suggested aid types.", Toast.LENGTH_LONG).show();
        }
    }
}
