package com.hci.refuge;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Eddie on 5/1/2016.
 * Adapter used in SearchAidActivity to propagate a ListView with EventData search results.
 */
public class SearchResultAdapter extends BaseAdapter {

    private ArrayList<EventData> _results = new ArrayList<>();
    private static LayoutInflater _inflater = null;
    private Context _context;
    private double lat = 0, lon = 0, maxDist = 0;

    public SearchResultAdapter(Context context) {
        _context = context;
        _inflater = LayoutInflater.from(_context);
    }

    @Override
    public int getCount() {
        return _results.size();
    }

    @Override
    public Object getItem(int position) {
        return _results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Fills a search result layout with information from a particular EventData
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        ViewHolder data;

        EventData event = _results.get(position);

        if (null == convertView) {
            data = new ViewHolder();
            result = _inflater.inflate(R.layout.item_search_result, parent, false);
            data.title = (TextView) result.findViewById(R.id.labelResultTitle);
            data.date = (TextView) result.findViewById(R.id.labelResultDate);
            data.date2 = (TextView) result.findViewById(R.id.labelResultDate2);
            data.distance = (TextView) result.findViewById(R.id.labelResultDistance);
            result.setTag(data);

        } else {
            data = (ViewHolder) result.getTag();
        }

        data.title.setText(event.title);
        data.date.setText(new SimpleDateFormat("MM/dd/yy").format(event.cal1.getTime())+"  AT  "
                +new SimpleDateFormat("hh:mm a").format(event.cal1.getTime()));
        data.date2.setText(new SimpleDateFormat("MM/dd/yy").format(event.cal2.getTime())+"  AT  "
                +new SimpleDateFormat("hh:mm a").format(event.cal2.getTime()));
        float[] dist = {0};
        Location.distanceBetween(lat, lon, event.latitude, event.longitude, dist);
        data.distance.setText(""+String.format(java.util.Locale.US,"%.2f", dist[0]/1000)+" km away");

        return result;
    }

    static class ViewHolder {
        TextView title, date, date2, distance;
    }

    /**
     * Fills the List of EventData with those results that are within 'max' km of user's location
     */
    public void setResults(ArrayList<EventData> results) {
        _results = new ArrayList<>();
        for (EventData event : results) {
            float[] dist = {0};
            Location.distanceBetween(lat, lon, event.latitude, event.longitude, dist);
            if (dist[0]/1000 <= maxDist) _results.add(event);
        }
        notifyDataSetChanged();
    }

    /**
     * Setter for the user's current location and their Maximum Travel Distance value
     */
    public void setLatLonMax(double la, double lo, double max) {
        lat = la;
        lon = lo;
        maxDist = max;
    }

}
