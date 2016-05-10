package com.hci.refuge;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eddie on 4/14/2016.
 * Stores information about the current user (either logged in, or being created)
 */
public class UserInfo {

    String profilePicPath = null, name = "", username = "", password = "";
    Double travelDistance = new Double(0);
    Country origin = Country.Blank;
    HashMap<String, String> docs;
    int birthday = 0, birthmonth = 0, birthyear = 0;
    Bitmap propic = null;

    public UserInfo() {
        docs = new HashMap<>();
    }

    public UserInfo(UserInfo ui) {
        profilePicPath = ui.profilePicPath; name = ui.name; username = ui.username; password = ui.password;
        travelDistance = new Double(ui.travelDistance); origin = ui.origin; docs = new HashMap<>(ui.docs);
        birthday = ui.birthday; birthmonth = ui.birthmonth; birthyear = ui.birthyear;
    }

    /**
     * Constructor for creating a UserInfo object from a previously stored file (username.txt)
     */
    public UserInfo(ArrayList<String> vals) {
        password = vals.get(0);
        name = vals.get(1);
        profilePicPath = vals.get(2);

        String birthdayPattern = "-";
        Pattern p = Pattern.compile(birthdayPattern);
        String[] dates = p.split(vals.get(3));
        birthmonth = Integer.parseInt(dates[0]);
        birthday = Integer.parseInt(dates[1]);
        birthyear = Integer.parseInt(dates[2]);

        travelDistance = Double.parseDouble(vals.get(4));

        String country = vals.get(5);
        if (country.equals("Afghanistan")) origin = Country.Afghanistan;
        else if (country.equals("Albania")) origin = Country.Albania;
        else if (country.equals("Eritrea")) origin = Country.Eritrea;
        else if (country.equals("Iran")) origin = Country.Iran;
        else if (country.equals("Iraq")) origin = Country.Iraq;
        else if (country.equals("Kosovo")) origin = Country.Kosovo;
        else if (country.equals("Nigeria")) origin = Country.Nigeria;
        else if (country.equals("Other")) origin = Country.Other;
        else if (country.equals("Pakistan")) origin = Country.Pakistan;
        else if (country.equals("Syria")) origin = Country.Syria;
        else if (country.equals("Ukraine")) origin = Country.Ukraine;
        else origin = Country.USA;

        docs = new HashMap<>();
        for (int i = 6; i < vals.size(); i++) {
            String line = vals.get(i);
            if (line.startsWith("id: ")) {
                docs.put("ID", line.substring(4));
            } else if (line.startsWith("registration: ")) {
                docs.put("ID", line.substring(14));
            } else if (line.startsWith("passport: ")) {
                docs.put("ID", line.substring(10));
            }
        }
    }

    /**
     * Checks to see what required fields are missing when somebody tries to create an account
     * Returns null if they're fine,
     * or a String to be presented to the user listing what they still need
     */
    public String readyToCreate() {
        String ret = "Missing ";
        ArrayList<String> needs = new ArrayList<>();
        if (profilePicPath == null) needs.add("profile picture");
        if (name.length() == 0) needs.add("name");
        if (username.length() == 0) needs.add("username");
        if (password.length() == 0) needs.add("password");
        if (travelDistance == 0) needs.add("travel distance");
        if (origin == Country.Blank) needs.add("country of origin");
        if (birthday == 0) needs.add("birth date");

        if (needs.isEmpty()) {
            return null;
        }
        else {
            ret += needs.get(0);
            if (needs.size() > 2) ret += ",";
            for (int i = 1; i < needs.size() - 1; i++) {
                ret += " " + needs.get(i) + ",";
            }
            if (needs.size() > 1) ret += " and " + needs.get(needs.size() - 1);
            ret += ".";
        }

        return ret.trim();
    }

    /**
     * When a new user is created in CreateAccountActivity,
     * this method is called to generate the String written into username.txt in order to save their info
     */
    public String fileStyle() {
        String s = "" + password
                + "\n" + name
                + "\n" + profilePicPath
                + "\n" + birthmonth + "-" + birthday + "-" + birthyear
                + "\n" + travelDistance.toString()
                + "\n" + origin.toString();

        if (docs.containsKey("ID")) s += "\nid: " + docs.get("ID");
        if (docs.containsKey("Registration")) s += "\nregistration: " + docs.get("Registration");
        if (docs.containsKey("Passport")) s += "\npassport: " + docs.get("Passport");

        return s;
    }

}
