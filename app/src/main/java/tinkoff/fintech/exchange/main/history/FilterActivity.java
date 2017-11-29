package tinkoff.fintech.exchange.main.history;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.main.operation.ExchangeListAdapter;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;


//TODO Add list, refactor to ViewModel.
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

        FilterListAdapter adapter = new FilterListAdapter(this, coins);
        lv.setAdapter(adapter);

        updateDates(Calendar.YEAR);

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
