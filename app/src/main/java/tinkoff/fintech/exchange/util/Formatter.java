package tinkoff.fintech.exchange.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    public static String decimal(double result) {
        if (result > 100)
            return String.format(Locale.US, "%d", (int) result);
        else if (result > 1)
            return String.format(Locale.US, "%.1f", result);
        else
            return String.format(Locale.US, "%.3f", result);
    }

    public static String date(Date date) {
        return  new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.US).format(date);
    }
}
