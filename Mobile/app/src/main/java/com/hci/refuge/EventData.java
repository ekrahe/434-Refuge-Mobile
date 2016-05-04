package com.hci.refuge;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;


/**
 * Created by becca13 on 4/27/16.
 */
public class EventData {
    public String title, description, who, docs;
    public double longitude, latitude;
    public GregorianCalendar cal1;
    public GregorianCalendar cal2;


    public EventData(String title, String description, String who, String docs, double lat, double lon, GregorianCalendar start, GregorianCalendar end) {
        this.title = title;
        this.description = description;
        this.latitude = lat;
        this.longitude = lon;
        this.cal1 = start;
        this.cal2 = end;
        this.who = who;
        this.docs = docs;
    }

    public EventData(EventData event) {
        this.title = event.title;
        this.description = event.description;
        this.latitude = event.latitude;
        this.longitude = event.longitude;
    }

    public static ArrayList<EventData> getData(SearchOptions option){
        ArrayList<EventData> events = new ArrayList<EventData>();

        if(option.equals(SearchOptions.Food) || option.equals(SearchOptions.Meal)
                || option.equals(SearchOptions.Breakfast) || option.equals(SearchOptions.Lunch)
                || option.equals(SearchOptions.Dinner)){
            events.add(new EventData("NGO1 Food Drive","NGO1 will be holding a food drive. Please come prepared with the paper work listed below to be able to receive food.","Families & Singles", "Photo ID", 38.980352, -76.938973, new GregorianCalendar(2016,5,10,16,0), new GregorianCalendar(2016,5,10,18,0)));
            events.add(new EventData("NGO2 Food Drive","NGO2 will be holding a food drive. Please come prepared with the paper work listed below to be able to receive food.","Families & Singles", "Photo ID", 38.992434, -76.946619, new GregorianCalendar(2016,5,12,14,0), new GregorianCalendar(2016,5,12,17,0)));
            events.add(new EventData("Soup Kitchen1 Food Give Away","Soup Kitchen1 will be holding a food give away. Please come prepared with the paper work listed below to be able to receive food.", "Families & Singles","Photo ID",38.981845, -76.937561, new GregorianCalendar(2016,5,13,10,0), new GregorianCalendar(2016,5,13,14,0)));
            events.add(new EventData("Soup Kitchen2 Food Give Away","Soup Kitche2 will be holding a food give away. Please come prepared with the paper work listed below to be able to receive food.", "Families & Singles", "Photo ID", 38.978087, -76.938173, new GregorianCalendar(2016,5,14,12,0), new GregorianCalendar(2016,5,14,18,0)));
            events.add(new EventData("NGO3 Food Drive","We will be holding a food drive from 8:00 to 12:00","Families & Singles", "Photo ID", 38.991500, -76.934352, new GregorianCalendar(2016,5,17,8,0), new GregorianCalendar(2016,5,17,12,0)));
        } else if(option.equals(SearchOptions.Clothing) || option.equals(SearchOptions.Apparel)
                || option.equals(SearchOptions.Shoes) || option.equals(SearchOptions.Duds)){
            events.add(new EventData("NGO1 Clothing Drive","We will be holding a clothing drive. Please come prepared with the paper work listed below to be able to receive aid.","Families & Singles", "Photo ID", 38.999145, -76.906961,new GregorianCalendar(2016,5,9,16,0), new GregorianCalendar(2016,5,9,18,0)));
            events.add(new EventData("NGO2 Clothing Drive","We will be holding a clothing drive. Please come prepared with the paper work listed below to be able to receive aid.","Families & Singles", "Photo ID", 38.980686, -76.938992,new GregorianCalendar(2016,5,12,12,0), new GregorianCalendar(2016,5,12,18,0)));
            events.add(new EventData("NGO3 Clothing Drive","We will be holding a clothing drive. Please come prepared with the paper work listed below to be able to receive aid.","Families & Singles", "Photo ID", 38.978293, -76.937487,new GregorianCalendar(2016,5,13,8,0), new GregorianCalendar(2016,5,13,11,0)));
            events.add(new EventData("NGO4 Clothing Drive","We will be holding a clothing drive. Please come prepared with the paper work listed below to be able to receive aid.","Families & Singles", "Photo ID", 38.969068, -76.959720,new GregorianCalendar(2016,5,18,16,0), new GregorianCalendar(2016,5,18,18,30)));
            events.add(new EventData("NGO5 Clothing Drive","We will be holding a clothing drive. Please come prepared with the paper work listed below to be able to receive aid.","Families & Singles", "Photo ID", 38.987758, -76.944848,new GregorianCalendar(2016,5,20,14,0), new GregorianCalendar(2016,5,20,20,0)));
        } else if(option.equals(SearchOptions.Doctor) || option.equals(SearchOptions.Hospital)
                || option.equals(SearchOptions.Health) || option.equals(SearchOptions.Vaccination)
                || option.equals(SearchOptions.Medicine)){
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends. Please come prepared with the paper work listed below to be able to receive health services.", "All ages", "Photo ID", 38.987185, -76.944699, new GregorianCalendar(2016,5,9), new GregorianCalendar(2016,5,9)));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends. Please come prepared with the paper work listed below to be able to receive health services.","All ages", "Photo ID", 38.993765, -76.945189,new GregorianCalendar(2016,5,11), new GregorianCalendar(2016,5,11)));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends. Please come prepared with the paper work listed below to be able to receive health services.", "All ages", "Photo ID", 38.990334, -76.933080,new GregorianCalendar(2016,5,14), new GregorianCalendar(2016,5,14)));
            events.add(new EventData("Health Care Forum","NGO2 will be holding a Health Care Forum to educate people about health care options in the area","Families & Singles", "None", 38.969513, -76.952820,new GregorianCalendar(2016,5,20,12,0), new GregorianCalendar(2016,5,20,14,0)));
            events.add(new EventData("Clinic Services","Clinic hours are from 8:00 to 21:00 every week day and from 10:00 to 17:00 on the weekends. Please come prepared with the paper work listed below to be able to receive health services.","All ages", "Photo ID", 39.014803, -76.927314,new GregorianCalendar(2016,5,21), new GregorianCalendar(2016,5,21)));
        } else if(option.equals(SearchOptions.Education) || option.equals(SearchOptions.Language)
                || option.equals(SearchOptions.School) || option.equals(SearchOptions.Classes)
                || option.equals(SearchOptions.Learning)){
            events.add(new EventData("Language Class1","Class will be held from 8:00 to 10:00 every Monday for 6 weeks","Ages 18 and up", "Photo ID", 38.986748, -76.944584,new GregorianCalendar(2016,5,9,8,0), new GregorianCalendar(2016,6,20,8,0)));
            events.add(new EventData("Language Class2","Class will be held from 10:00 to 12:00 every Wednesday for 6 weeks","Ages 18 and under", "Photo ID", 38.985967, -76.945106,new GregorianCalendar(2016,5,11,10,0), new GregorianCalendar(2016,6,22,10,0)));
            events.add(new EventData("Language Class3","Class will be held from 8:00 to 10:00 every Saturday for 6 weeks", "All ages", "Photo ID", 38.989984, -76.936191,new GregorianCalendar(2016,5,14,8,0), new GregorianCalendar(2016,6,25,8,0)));
            events.add(new EventData("School1","School1 is one of the top refugee school in the country. Visit our website or contact school1@gmail.com","Ages 18 and under", "Birth Certificate", 38.988153, -76.941573, new GregorianCalendar(2016,5,20), new GregorianCalendar(2017,5,20)));
            events.add(new EventData("School2","School2 is one of the top refugee school in the country. Visit our website or contact school1@gmail.com", "Ages 18 and under", "Birth Certificate", 38.986812, -76.926184, new GregorianCalendar(2016,5,21), new GregorianCalendar(2017,5,21)));
        } else if(option.equals(SearchOptions.Housing) || option.equals(SearchOptions.Shelter)
                || option.equals(SearchOptions.Tent) || option.equals(SearchOptions.Camp)
                || option.equals(SearchOptions.Apartment)){
            events.add(new EventData("Shelter1","Welcome to all ages and familes","Families & Singles", "Photo ID", 38.983939, -76.935812, new GregorianCalendar(2016,5,10), new GregorianCalendar(2017,5,10)));
            events.add(new EventData("Shelter2","Welcome to all ages and familes","Families & Singles", "Photo ID", 38.982102, -76.942937, new GregorianCalendar(2016,5,13), new GregorianCalendar(2017,5,13)));
            events.add(new EventData("Shelter3","Welcome to all ages and familes","Families & Singles", "Photo ID", 38.992571, -76.934326, new GregorianCalendar(2016,5,16), new GregorianCalendar(2017,5,16)));
            events.add(new EventData("Youth Housing1","Welcome to ages 18 and under","Ages 18 and under", "Photo ID", 38.991723, -76.943000, new GregorianCalendar(2016,5,17), new GregorianCalendar(2017,5,17)));
            events.add(new EventData("Youth Housing2","Welcome to ages 18 and under", "Ages 18 and under", "Photo ID", 38.987057, -76.935340, new GregorianCalendar(2016,5,23), new GregorianCalendar(2017,5,23)));
        } else if(option.equals(SearchOptions.Jobs) || option.equals(SearchOptions.Employment)
                || option.equals(SearchOptions.Money) || option.equals(SearchOptions.Payment)
                || option.equals(SearchOptions.Labor)){
            events.add(new EventData("Office1","Help wanted at Office1. Tasks include answering phones, sending emails, managing calendars.","Ages 18 and older", "Photo ID", 38.986032, -76.939819, new GregorianCalendar(2016,5,9,8,0), new GregorianCalendar(2016,5,9,17,0)));
            events.add(new EventData("Construction1","Help wanted at Construction1. Contact John at johnx@gmail.com for more details","Males ages 16 and older", "Photo ID", 38.987021, -76.935184,new GregorianCalendar(2016,5,11,7,0), new GregorianCalendar(2016,5,11,18,0)));
            events.add(new EventData("Cashier1","Help wanted at Cashier1","Ages 16 and older", "Photo ID", 38.999520, -76.910074, new GregorianCalendar(2016,5,13,14,0), new GregorianCalendar(2016,5,13,20,0)));
            events.add(new EventData("Cashier2","Help wanted at Cashier2","Ages 16 and older", "Photo ID", 38.982407, -76.937572, new GregorianCalendar(2016,5,19,6,0), new GregorianCalendar(2016,5,19,14,0)));
            events.add(new EventData("Construction2","Help wanted at Construction2. Contact Bob at bobx@gmail.com for more details","Males ages 18 and older", "Photo ID", 38.989458, -76.935711, new GregorianCalendar(2016,5,24,7,0), new GregorianCalendar(2016,5,24,18,0)));
        } else if(option.equals(SearchOptions.Transportation)){
            events.add(new EventData("Bus","Information on bus routes","Families & Singles", "Photo ID", 38.989489, -76.940959, new GregorianCalendar(2016,5,10), new GregorianCalendar(2016,5,10)));
            events.add(new EventData("Train","Information on train times", "Families & Singles", "Photo ID", 38.977994, -76.928732, new GregorianCalendar(2016,5,14), new GregorianCalendar(2016,5,14)));
            events.add(new EventData("Car Rentals","Information on car rentals", "Families & Singles", "Photo ID", 39.007627, -76.929349, new GregorianCalendar(2016,5,17), new GregorianCalendar(2016,5,17)));
            events.add(new EventData("Bikes","Information on bike shops","Families & Singles", "Photo ID", 39.006439, -76.929612, new GregorianCalendar(2016,5,25), new GregorianCalendar(2016,5,25)));
            events.add(new EventData("Cars","Information on car dealerships", "Families & Singles", "Photo ID", 39.010747, -76.928200, new GregorianCalendar(2016,5,28), new GregorianCalendar(2016,5,28)));
        }

        return events;
    }


}
