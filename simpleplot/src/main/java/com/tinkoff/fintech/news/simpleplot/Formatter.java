package com.tinkoff.fintech.news.simpleplot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    public static String doubleToString(double result) {
        if (result >= 1000000)
            return String.format(Locale.US, "%.1E", result);
        else if (result >= 1000)
            return String.format(Locale.US, "%,d", (int) result);
        else if (result >= 100)
            return String.format(Locale.US, "%.1f", result);
        else if (result >= 10)
            return String.format(Locale.US, "%.2f", result);
        else if (result >= 1)
            return String.format(Locale.US, "%.3f", result);
        else
            return String.format(Locale.US, "%.4f", result);
    }

    public static String dateToString(Date date) {
        return  new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US).format(date);
    }

    public static String dateToShortString(Date date) {
        return  new SimpleDateFormat(
                "dd MMM", Locale.US).format(date);
    }
    
}
