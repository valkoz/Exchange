package tinkoff.fintech.exchange.main.history;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.Formatter;


public class FilterActivity extends AppCompatActivity {

    Calendar calendar;
    EditText edFrom;
    EditText edTo;
    Button submitButton;
    RadioGroup rg;
    private RecyclerView mRecyclerView;
    private FilterRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FilterViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        calendar = Calendar.getInstance();
        edFrom = findViewById(R.id.history_filer_from);
        edTo = findViewById(R.id.history_filer_to);
        submitButton = findViewById(R.id.history_filter_submit);
        rg = findViewById(R.id.history_radioGroup);

        List<String> coins = AppDatabase.getAppDatabase(getApplicationContext()).exchangeOperationDao().getExistingCurrencies();
        HistoryQuery i = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();

        mRecyclerView = findViewById(R.id.filter_list_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Currency> currencies = new ArrayList<Currency>();
        if (i != null) {
            for (String coinName: coins) {
                if (i.getCurrencies().contains(coinName)) {
                    currencies.add(new Currency(coinName, true));
                } else {
                    currencies.add(new Currency(coinName, false));
                }
            }
        } else {
            for (String coinName: coins) {
                currencies.add(new Currency(coinName, true));
            }
        }

        mAdapter = new FilterRecyclerAdapter(currencies);
        mRecyclerView.setAdapter(mAdapter);

        model = ViewModelProviders.of(this).get(FilterViewModel.class);
        model.getEndDate().observe(this, date -> edTo.setText(Formatter.dateToString(date)));
        model.getStartDate().observe(this, date -> edFrom.setText(Formatter.dateToString(date)));

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.history_filter_all:
                        model.updateDates(Calendar.YEAR);
                        break;
                    case R.id.history_filter_week:
                        model.updateDates(Calendar.WEEK_OF_YEAR);
                        break;
                    case R.id.history_filter_month:
                        model.updateDates(Calendar.MONTH);
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
                model.setStartDate(calendar);
            }

        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                model.setEndDate(calendar);
            }

        };


        edFrom.setOnClickListener(setTextListener(fromDateListener));
        edTo.setOnClickListener(setTextListener(toDateListener));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                Date start = model.getStartDate().getValue();
                Date end = model.getEndDate().getValue();

                if (start.getTime() < end.getTime()) {
                    returnIntent.putExtra("from", start.getTime());
                    returnIntent.putExtra("to", end.getTime());
                    returnIntent.putExtra("currencies", new ArrayList<>(mAdapter.getChosenCurrencies()));
                    setResult(1, returnIntent);
                    AppDatabase.getAppDatabase(getApplicationContext()).historyQueryDao().deleteAll();
                    AppDatabase.getAppDatabase(getApplicationContext()).historyQueryDao().insert(new HistoryQuery(start, end, mAdapter.getChosenCurrencies()));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter correct dates",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private View.OnClickListener setTextListener(DatePickerDialog.OnDateSetListener dateListener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.clearCheck();
                new DatePickerDialog(FilterActivity.this, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }
}
