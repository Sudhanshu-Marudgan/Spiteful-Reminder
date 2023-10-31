package com.example.spiteful_reminder;

import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class HelperWithKey implements Comparable<HelperWithKey> {
    private String key;
    private helper helper;

    public HelperWithKey(String key, helper helper) {
        this.key = key;
        this.helper = helper;
    }

    public String getKey() {
        return key;
    }

    public helper getHelper() {
        return helper;
    }

    @Override
    public int compareTo(HelperWithKey another) {
        // Compare the dates and times of two helper objects
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1, date2;
        try {
            date1 = format.parse(helper.getDate() + " " + helper.getTime());
            date2 = format.parse(another.helper.getDate() + " " + another.helper.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle parsing error
        }
        return date1.compareTo(date2);
    }
}

