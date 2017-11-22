package tinkoff.fintech.exchange.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    public static String decimal(double result) {
        if (result > 1000000)
            return String.format(Locale.US, "%.1E", result);
        else if (result > 1000)
            return String.format(Locale.US, "%,d", (int) result);
        else if (result > 100)
            return String.format(Locale.US, "%d", (int) result);
        else if (result >= 1)
            return String.format(Locale.US, "%.1f", result);
        else
            return String.format(Locale.US, "%.3f", result);
    }

    public static String date(Date date) {
        return  new SimpleDateFormat(
                "dd MMM ''yy 'at' HH:mm", Locale.US).format(date);
    }

    public static String dateToRestrofit(Date date) {
        return  new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US).format(date);
    }
}
