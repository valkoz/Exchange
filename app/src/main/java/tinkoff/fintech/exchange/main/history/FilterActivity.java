package tinkoff.fintech.exchange.main.history;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;


//TODO Refactor
public class FilterActivity extends AppCompatActivity {

    Calendar calendar;
    EditText edFrom;
    Date fromDate;
    EditText edTo;
    Date toDate;
    DatePickerDialog dpd;
    Button submitButton;
    RadioGroup rg;
    ListView lv;
    FilterListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        calendar = Calendar.getInstance();
        edFrom = findViewById(R.id.history_filer_from);
        edTo = findViewById(R.id.history_filer_to);
        submitButton = findViewById(R.id.history_filter_submit);
        rg = findViewById(R.id.history_radioGroup);

        lv = findViewById(R.id.history_filter_list);

        List coins = AppDatabase.getAppDatabase(getApplicationContext()).exchangeOperationDao().getExistingCurrencies();

        adapter = new FilterListAdapter(this, coins, new ArrayList<String>());

        HistoryQuery i = AppDatabase.getAppDatabase(getApplicationContext()).historyQueryDao().get();

        if (i != null) {
            fromDate = i.getFromDate();
            toDate = i.getToDate();
            edFrom.setText(Formatter.dateToString(fromDate));
            edTo.setText(Formatter.dateToString(toDate));
            Log.i("toAdapter", i.getCurrencies().toString());
            adapter = new FilterListAdapter(this, coins, i.getCurrencies());
        } else {
            updateDates(Calendar.YEAR);
        }
        lv.setAdapter(adapter);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.history_filter_all:
                        updateDates(Calendar.YEAR);
                        break;
                    case R.id.history_filter_week:
                        updateDates(Calendar.WEEK_OF_YEAR);
                        break;
                    case R.id.history_filter_month:
                        updateDates(Calendar.MONTH);
                        break;
                    default:
                        break;
                }
            }
        });


        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromLabel();
            }

        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToLabel();
            }

        };


        edFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.clearCheck();
                new DatePickerDialog(FilterActivity.this, fromDateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.clearCheck();
                new DatePickerDialog(FilterActivity.this, toDateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                if (fromDate.getTime() < toDate.getTime()) {
                    returnIntent.putExtra("from", fromDate.getTime());
                    returnIntent.putExtra("to", toDate.getTime());
                    returnIntent.putExtra("currencies", adapter.getChoosenCurrencies());
                    setResult(1, returnIntent);
                    AppDatabase.getAppDatabase(getApplicationContext()).historyQueryDao().deleteAll();
                    AppDatabase.getAppDatabase(getApplicationContext()).historyQueryDao().insert(new HistoryQuery(fromDate, toDate, adapter.getChoosenCurrencies()));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error:" +
                            Formatter.dateToString(fromDate)
                                    + Formatter.dateToString(toDate) + lv.getCheckedItemPosition(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateFromLabel() {
        fromDate = calendar.getTime();
        edFrom.setText(Formatter.dateToString(fromDate));
    }

    private void updateToLabel() {
        toDate = calendar.getTime();
        edTo.setText(Formatter.dateToString(toDate));
    }

    private void updateDates(int period) {
        fromDate = CalendarIterator.get(period);
        toDate = new Date();
        edFrom.setText(Formatter.dateToString(fromDate));
        edTo.setText(Formatter.dateToString(toDate));
    }
}
