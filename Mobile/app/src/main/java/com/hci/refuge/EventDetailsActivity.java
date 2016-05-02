package com.hci.refuge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton directionButton;
    static EventData event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.labelDetailsTitle)).setText(event.title);
        ((TextView)findViewById(R.id.labelDetailsDate)).setText(""+event.month+"/"+event.day+"/"+event.year);
        ((TextView)findViewById(R.id.labelDetailsDescription)).setText(event.description);

        directionButton = (ImageButton) findViewById(R.id.buttonGetDirections);
        directionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + event.latitude + ","
                + event.longitude + " (" + event.title + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);
    }

}
