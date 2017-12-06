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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.pojo.CheckableCurrency;
import tinkoff.fintech.exchange.util.Formatter;


public class FilterActivity extends AppCompatActivity {

    private EditText edFrom;
    private EditText edTo;
    private Button submitButton;
    private RadioGroup rg;
    private RecyclerView recyclerView;
    private FilterRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FilterViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initViews();

        model = ViewModelProviders.of(this).get(FilterViewModel.class);
        model.getEndDate().observe(this, date -> edTo.setText(Formatter.dateToString(date)));
        model.getStartDate().observe(this, date -> edFrom.setText(Formatter.dateToString(date)));
        model.getCurrencies().observe(this, currencies -> adapter.addItems(currencies));

        rg.setOnCheckedChangeListener((RadioGroup radioGroup, int checkedId) -> {
                switch (checkedId) {
                    case R.id.history_filter_all:
                        model.updateDates(Calendar.YEAR); break;
                    case R.id.history_filter_week:
                        model.updateDates(Calendar.WEEK_OF_YEAR); break;
                    case R.id.history_filter_month:
                        model.updateDates(Calendar.MONTH); break;
                    default: break;
                }
        });

        edFrom.setOnClickListener(setTextListener(
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) ->
                        model.setStartDate(year, monthOfYear, dayOfMonth)));
        edTo.setOnClickListener(setTextListener(
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) ->
                        model.setEndDate(year, monthOfYear, dayOfMonth)));

        submitButton.setOnClickListener(view ->  {
                Intent returnIntent = new Intent();
                Date start = model.getStartDate().getValue();
                Date end = model.getEndDate().getValue();
                List<String> chosenCurrencies = model.getChosenCurrencies();

                if (start.getTime() < end.getTime()) {
                    returnIntent.putExtra("from", start.getTime());
                    returnIntent.putExtra("to", end.getTime());
                    returnIntent.putExtra("currencies", new ArrayList<>(chosenCurrencies));
                    setResult(1, returnIntent);
                    model.updateHistoryQuery(start, end, chosenCurrencies);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter correct dates",
                            Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void initViews() {

        edFrom = findViewById(R.id.history_filer_from);
        edTo = findViewById(R.id.history_filer_to);
        submitButton = findViewById(R.id.history_filter_submit);
        rg = findViewById(R.id.history_radioGroup);

        recyclerView = findViewById(R.id.filter_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FilterRecyclerAdapter(new ArrayList<CheckableCurrency>(),
                (CompoundButton compoundButton, boolean isChecked) ->
                        model.updateCurrencies(compoundButton.getTag(), isChecked));
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener setTextListener(DatePickerDialog.OnDateSetListener dateListener) {
        return view ->  {
                rg.clearCheck();
                new DatePickerDialog(FilterActivity.this, dateListener, model.getCurrentYear(),
                        model.getCurrentMonth(),
                        model.getCurrentDayOfMonth()).show();
            };
    }
}
