package com.hci.refuge;

import android.location.Location;
import java.util.ArrayList;

/**
 * Created by becca13 on 4/27/16.
 */
public class EventData {
    public String title, description;
    public int day, month, year;
    public double longitude, latitude;

    public EventData(String title, String description, int day, int month, int year, double lat, double lon) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
        this.latitude = lat;
        this.longitude = lon;
    }

    public ArrayList<EventData> getData(SearchOptions option){
        ArrayList<EventData> events = new ArrayList<EventData>();

        if(option.equals(SearchOptions.Food) || option.equals(SearchOptions.Meal)
                || option.equals(SearchOptions.Breakfast) || option.equals(SearchOptions.Lunch)
                || option.equals(SearchOptions.Dinner)){
            events.add(new EventData("NGO1 Food Drive","We will be holding a food drive from 16:00 to 18:00", 10, 5, 2016,38.980352, -76.938973));
            events.add(new EventData("NGO2 Food Drive","We will be holding a food drive from 14:00 to 17:00", 12, 5, 2016,38.992434, -76.946619));
            events.add(new EventData("Soup Kitchen1 Food Give Away","We will be holding a food drive from 10:00 to 14:00", 13, 5, 2016,38.981845, -76.937561));
            events.add(new EventData("Soup Kitchen2 Food Give Away","We will be holding a food drive from 12:00 to 18:00", 14, 5, 2016,38.978087, -76.938173));
            events.add(new EventData("NGO3 Food Drive","We will be holding a food drive from 8:00 to 12:00", 17, 5, 2016,38.991500, -76.934352));
        } else if(option.equals(SearchOptions.Clothing) || option.equals(SearchOptions.Apparel)
                || option.equals(SearchOptions.Shoes) || option.equals(SearchOptions.Duds)){
            events.add(new EventData("NGO1 Clothing Drive","We will be holding a clothing drive from 16:00 to 18:00", 9, 5, 2016,38.999145, -76.906961));
            events.add(new EventData("NGO2 Clothing Drive","We will be holding a clothing drive from 12:00 to 15:00", 12, 5, 2016,38.980686, -76.938992));
            events.add(new EventData("NGO3 Clothing Drive","We will be holding a clothing drive from 8:00 to 11:00", 13, 5, 2016,38.978293, -76.937487));
            events.add(new EventData("NGO4 Clothing Drive","We will be holding a clothing drive from 16:00 to 18:00", 18, 5, 2016,38.969068, -76.959720));
            events.add(new EventData("NGO5 Clothing Drive","We will be holding a clothing drive from 14:00 to 20:00", 20, 5, 2016,38.987758, -76.944848));
        } else if(option.equals(SearchOptions.Doctor) || option.equals(SearchOptions.Hospital)
                || option.equals(SearchOptions.Health) || option.equals(SearchOptions.Vaccination)
                || option.equals(SearchOptions.Medicine)){
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 9, 5, 2016,38.987185, -76.944699));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 11, 5, 2016,38.993765, -76.945189));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 14, 5, 2016,38.990334, -76.933080));
            events.add(new EventData("Health Care Forum","NGO2 will be holding a Health Care Forum from 12:00 to 14:00 to educate people about health care options in the area", 20, 5, 2016,38.969513, -76.952820));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends", 21, 5, 2016,39.014803, -76.927314));
        } else if(option.equals(SearchOptions.Education) || option.equals(SearchOptions.Language)
                || option.equals(SearchOptions.School) || option.equals(SearchOptions.Classes)
                || option.equals(SearchOptions.Learning)){
            events.add(new EventData("Language Class1","Class will be held from 8:00 to 10:00 every Monday", 9, 5, 2016,38.986748, -76.944584));
            events.add(new EventData("Language Class2","Class will be held from 10:00 to 12:00 every Wednesday", 11, 5, 2016,38.985967, -76.945106));
            events.add(new EventData("Language Class3","Class will be held from 8:00 to 10:00 every Saturday", 14, 5, 2016,38.989984, -76.936191));
            events.add(new EventData("School1","Welcome to ages 18 and under", 20, 5, 2016,38.988153, -76.941573));
            events.add(new EventData("School2","Welcome to ages 18 and under", 21, 5, 2016,38.986812, -76.926184));
        } else if(option.equals(SearchOptions.Housing) || option.equals(SearchOptions.Shelter)
                || option.equals(SearchOptions.Tent) || option.equals(SearchOptions.Camp)
                || option.equals(SearchOptions.Apartment)){
            events.add(new EventData("Shelter1","Welcome to all ages and familes", 10, 5, 2016,38.983939, -76.935812));
            events.add(new EventData("Shelter2","Welcome to all ages and familes", 14, 5, 2016,38.982102, -76.942937));
            events.add(new EventData("Shelter3","Welcome to all ages and familes", 16, 5, 2016,38.992571, -76.934326));
            events.add(new EventData("Youth Housing1","Welcome to ages 18 and under", 17, 5, 2016,38.991723, -76.943000));
            events.add(new EventData("Youth Housing2","Welcome to ages 18 and under", 23, 5, 2016,38.987057, -76.935340));
        } else if(option.equals(SearchOptions.Jobs) || option.equals(SearchOptions.Employment)
                || option.equals(SearchOptions.Money) || option.equals(SearchOptions.Payment)
                || option.equals(SearchOptions.Labor)){
            events.add(new EventData("Office1","Help wanted at Office1. Tasks include answering phones, sending emails, managing calendars.", 9, 5, 2016,38.986032, -76.939819));
            events.add(new EventData("Construction1","Help wanted at Construction1. Contact John at johnx@gmail.com for more details", 11, 5, 2016,38.987021, -76.935184));
            events.add(new EventData("Cashier1","Help wanted at Cashier1", 14, 5, 2016,38.999520, -76.910074));
            events.add(new EventData("Cashier2","Help wanted at Cashier2", 20, 5, 2016,38.982407, -76.937572));
            events.add(new EventData("Construction2","Help wanted at Construction2. Contact Bob at bobx@gmail.com for more details", 21, 5, 2016,38.989458, -76.935711));
        } else if(option.equals(SearchOptions.Transportation)){
            events.add(new EventData("Bus","Information on bus routes", 9, 5, 2016,38.989489, -76.940959));
            events.add(new EventData("Train","Information on train times", 11, 5, 2016,38.977994, -76.928732));
            events.add(new EventData("Car Rentals","Information on car rentals", 14, 5, 2016,39.007627, -76.929349));
            events.add(new EventData("Bikes","Information on bike shops", 20, 5, 2016,39.006439, -76.929612));
            events.add(new EventData("Cars","Information on car dealerships", 21, 5, 2016,39.010747, -76.928200));
        }

        return events;
    }


}
