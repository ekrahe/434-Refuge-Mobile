package com.hci.refuge;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/1/2016.
 */
public class SearchResultAdapter extends BaseAdapter {

    private ArrayList<EventData> _results = new ArrayList<>();
    private static LayoutInflater _inflater = null;
    private Context _context;
    private double lat = 0, lon = 0;

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
            data.distance = (TextView) result.findViewById(R.id.labelResultDistance);
            result.setTag(data);

        } else {
            data = (ViewHolder) result.getTag();
        }

        data.title.setText(event.title);
        data.date.setText(""+event.month+"/"+event.day+"/"+event.year);
        float[] dist = {0};
        Location.distanceBetween(lat, lon, event.latitude, event.longitude, dist);
        data.distance.setText(""+String.format(java.util.Locale.US,"%.2f", dist[0]/1000)+" km");

        return result;
    }

    static class ViewHolder {
        TextView title, date, distance;
    }

    public void setResults(ArrayList<EventData> results) {
        _results = new ArrayList<>(results);
        notifyDataSetChanged();
    }

    public void setLatLon(double la, double lo) {
        lat = la;
        lon = lo;
    }

}
