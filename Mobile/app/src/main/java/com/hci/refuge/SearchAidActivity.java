package com.hci.refuge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class SearchAidActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener,
        android.widget.TextView.OnEditorActionListener,
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

    /**
     * The SearchAidActivity is presented to users when they sign in.
     * The first time they sign in, users are presented with a welcome popup explaining usage.
     * There is an AutoCompleteTextView to allow users to search aid types and give them suggestions.
     * Searched aid types return a list of events in the user's area or an error message.
     * Any list item can be clicked on to view more details
     */
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
        _searchBox.setOnEditorActionListener(this);

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
            resultsAdapter.setLatLonMax(lat, lon, userInfo.travelDistance);
            resultsAdapter.setResults(EventData.getData(search));
        }

        try {
            if(getIntent().getExtras().getString("FROM").equals("Create")) {
                new AlertDialog.Builder(this).setTitle("Welcome to Refuge!").setIcon(R.mipmap.ic_launcher)
                        .setMessage("While signed into Refuge, you can search for aid events posted in your area." +
                                " Use the search bar to choose from one of the available aid types," +
                                " and choose any result from the list to see more information about it.")
                        .setNegativeButton("Okay", null).show();
            }
        } catch (Exception ignored){}

    }

    /**
     * The user can hit the search key on their keyboard in order to initiate searching for an aid type
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_searchBox.getWindowToken(), 0);
            _searchButton.callOnClick();
            _searchBox.clearFocus();
            return true;
        }
        return false;
    }

    /**
     * If a search result is clicked, open up the EventDetailsActivity to see more info about it
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        EventDetailsActivity.event = (EventData) resultsAdapter.getItem(position);
        startActivity(intent);
    }

    /**
     * Overridden so that the user can't hit back to accidentally sign out
     */
    @Override
    public void onBackPressed() { }

    /**
     * Grabs the user's coarse location in order to find their approximate geological location.
     * Used to fill out distance values for the events listed as search results
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
            Location last = LocationServices.FusedLocationApi.getLastLocation(_googleClient);
            if (last != null) {
                lat = last.getLatitude();
                lon = last.getLongitude();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * These two methods are used to connect to Google Play in order to receive the user's location
     */
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

    /**
     * Only item in the menu is Sign Out. If they hit this, make sure they meant to
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sign_out) {
            final Context c = this;
            new AlertDialog.Builder(this).setMessage("Do you want to sign out?")
                    .setNegativeButton("Cancel", null).
                    setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userInfo = null;
                            search = null;
                            Intent intent = new Intent(c, MainActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Only button is Search. If they hit it, close the keyboard and return either:
     * 1) A list of events in the user's area of that type, or
     * 2) An error message explaining the lack of close aid or the use of an invalid search term
     */
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_searchBox.getWindowToken(), 0);
        try {
            SearchOptions type = SearchOptions.valueOf(_searchBox.getText().toString());
            search = type;
            resultsAdapter.setLatLonMax(lat, lon, userInfo.travelDistance);
            resultsAdapter.setResults(EventData.getData(search));
            if (resultsAdapter.getCount() == 0) {
                Toast.makeText(this, "No aid found in your vicinity", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Search using one of the suggested aid types.", Toast.LENGTH_LONG).show();
        }
    }
}
