package com.hci.refuge;

import android.location.Location;
import java.util.ArrayList;

/**
 * Created by becca13 on 4/27/16.
 */
public class EventData {
    public String title, description;
    public int day, month, year;
    public Location destination;
    public double longitude, latitude;

    public EventData(String title, String description, int day, int month, int year, Location destination, double lon, double lat) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
        destination.setLatitude(lat);
        destination.setLongitude(lon);
    }

    public ArrayList<EventData> getData(SearchOptions option){
        ArrayList<EventData> events = new ArrayList<EventData>();

        if(option.equals(SearchOptions.Food)){
            events.add(new EventData("NGO1 Food Drive","We will be holding a food drive from 16:00 to 18:00", 10, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO2 Food Drive","We will be holding a food drive from 14:00 to 17:00", 12, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Soup Kitchen1 Food Give Away","We will be holding a food drive from 10:00 to 14:00", 13, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Soup Kitchen2 Food Give Away","We will be holding a food drive from 12:00 to 18:00", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO3 Food Drive","We will be holding a food drive from 8:00 to 12:00", 17, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Clothes)){
            events.add(new EventData("NGO1 Clothing Drive","We will be holding a clothing drive from 16:00 to 18:00", 9, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO2 Clothing Drive","We will be holding a clothing drive from 12:00 to 15:00", 12, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO3 Clothing Drive","We will be holding a clothing drive from 8:00 to 11:00", 13, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO4 Clothing Drive","We will be holding a clothing drive from 16:00 to 18:00", 18, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("NGO5 Clothing Drive","We will be holding a clothing drive from 14:00 to 20:00", 20, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Doctor) || option.equals(SearchOptions.Hospital) || option.equals(SearchOptions.Health)){
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 9, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 11, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Health Care Forum","NGO2 will be holding a Health Care Forum from 12:00 to 14:00 to educate people about health care options in the area", 20, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 21, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Education) || option.equals(SearchOptions.Language)){
            events.add(new EventData("Language Class1","Class will be held from 8:00 to 10:00 every Monday", 9, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Language Class2","Class will be held from 10:00 to 12:00 every Wednesday", 11, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Language Class3","Class will be held from 8:00 to 10:00 every Saturday", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("School1","Welcome to ages 18 and under", 20, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("School2","Welcome to ages 18 and under", 21, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Housing) || option.equals(SearchOptions.Shelter)){
            events.add(new EventData("Shelter1","Welcome to all ages and familes", 10, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Shelter2","Welcome to all ages and familes", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Shelter3","Welcome to all ages and familes", 16, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Youth Housing1","Welcome to ages 18 and under", 17, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Youth Housing2","Welcome to ages 18 and under", 23, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Jobs)){
            events.add(new EventData("Office1","Help wanted at Office1. Tasks include answering phones, sending emails, managing calendars.", 9, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Construction1","Help wanted at Construction1. Contact John at johnx@gmail.com for more details", 11, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Cashier1","Help wanted at Cashier1", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Cashier2","Help wanted at Cashier2", 20, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Construction2","Help wanted at Construction2. Contact Bob at bobx@gmail.com for more details", 21, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        } else if(option.equals(SearchOptions.Transportation)){
            events.add(new EventData("Bus","Information on bus routes", 9, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Train","Information on train times", 11, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Car Rentals","Information on car rentals", 14, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Bikes","Information on bike shops", 20, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
            events.add(new EventData("Cars","Information on car dealerships", 21, 5, 2016, new Location("College Park"), -119.3862300, 36.5363400));
        }

        return events;
    }


}
