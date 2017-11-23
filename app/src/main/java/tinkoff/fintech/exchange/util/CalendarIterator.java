package tinkoff.fintech.exchange.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.main.analytics.AnalyticsFragment;

public class CalendarIterator {

    public static List<Date> getLast(AnalyticsFragment.Period period) {

        Calendar startCalender = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);
        switch (period) {
            case WEEK:
                startCalender.add(Calendar.DAY_OF_YEAR, -6);
                break;
            case TWO_WEEKS:
                startCalender.add(Calendar.DAY_OF_YEAR, -13);
                break;
            case MONTH:
                startCalender.add(Calendar.DAY_OF_YEAR, -30);
                break;
            default:
                break;
        }

        List<Date> dates = new ArrayList<>();

        for (Date date = startCalender.getTime(); startCalender.before(endCalendar); startCalender.add(Calendar.DATE, 1), date = startCalender.getTime()) {
            dates.add(date);
        }
        return dates;
    }

}
