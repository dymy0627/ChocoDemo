package com.yulin.myapplication;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String parserDateFormat(String source) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date date = null;
        try {
            date = input.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(date);
        Log.i("Utils", "formatted=" + formatted);
        return formatted;
    }
}
