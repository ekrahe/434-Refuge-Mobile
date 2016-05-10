package com.hci.refuge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton directionButton;
    static EventData event;

    /**
     * Provides details about a specific event that a user clicks on in SearchAidActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.labelDetailsTitle)).setText(event.title);
        ((TextView)findViewById(R.id.labelDetailsStart)).setText(new SimpleDateFormat("MM/dd/yy hh:mm a").format(event.cal1.getTime()));
        ((TextView)findViewById(R.id.labelDetailsEnd)).setText(new SimpleDateFormat("MM/dd/yy hh:mm a").format(event.cal2.getTime()));
        ((TextView)findViewById(R.id.labelDetailsWho)).setText(""+event.who);
        ((TextView)findViewById(R.id.labelDetailsNeed)).setText(""+event.docs);
        ((TextView)findViewById(R.id.labelDetailsDescription)).setText(event.description);

        directionButton = (ImageButton) findViewById(R.id.buttonGetDirections);
        directionButton.setOnClickListener(this);
    }

    /**
     * The Directions button opens up the Google Maps app on the (lat, lon) location of the event
     */
    @Override
    public void onClick(View v) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + event.latitude + ","
                + event.longitude + " (" + event.title + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);
    }

}
