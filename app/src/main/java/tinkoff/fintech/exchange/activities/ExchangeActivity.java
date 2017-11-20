package tinkoff.fintech.exchange.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.network.ApiResponse;
import tinkoff.fintech.exchange.network.ErrorType;
import tinkoff.fintech.exchange.network.RateCallback;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.Formatter;

public class ExchangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("activityCreated", getClass().getCanonicalName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        TextView tv = findViewById(R.id.exchange_result_decimal);
        EditText ed = findViewById(R.id.exchange_input_decimal);
        Button button = findViewById(R.id.exchange_button);

        Intent intent = getIntent();

        final String from = intent.getStringExtra(MainActivity.FROM_CURRENCY);
        TextView textView = findViewById(R.id.first_currency);
        textView.setText(from);

        final String to = intent.getStringExtra(MainActivity.TO_CURRENCY);
        TextView textView1 = findViewById(R.id.second_currency);
        textView1.setText(to);

        final List<RateObject> response = new ArrayList<>();

        RateCallback rateCallback = new RateCallback() {
            @Override
            public void onSuccess(RateObject rate) {
                response.add(rate);
            }

            @Override
            public void onError(ErrorType type) {
                if (type == ErrorType.REQUEST_ERROR)
                    Toast.makeText(getBaseContext(), "Error. Check your connection", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "fixer.io api error", Toast.LENGTH_SHORT).show();
            }
        };

        RetrofitClient.sendRequest(rateCallback, from, to);

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("TextWatcher", "onTextChanged");
                double mul = Double.parseDouble(ed.getText().toString());
                double result = response.get(0).getRate() * mul;
                String res = Formatter.decimal(result);

                tv.setText(res);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        button.setOnClickListener(view -> {
            double mul = Double.parseDouble(ed.getText().toString());
            double result = response.get(0).getRate() * mul;
            ExchangeOperation operation = new ExchangeOperation(Calendar.getInstance().getTime(),
                    from, to, mul, result);
            AsyncTask.execute(() -> AppDatabase.getAppDatabase(getApplicationContext())
                    .exchangeOperationDao()
                    .insertAll(operation));
        });

    }
}
