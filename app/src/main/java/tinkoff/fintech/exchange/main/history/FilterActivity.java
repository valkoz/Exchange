package tinkoff.fintech.exchange.main.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import tinkoff.fintech.exchange.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        CalendarView calendarView = findViewById(R.id.analytics_calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Toast.makeText(getApplicationContext(), ""+dayOfMonth, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
