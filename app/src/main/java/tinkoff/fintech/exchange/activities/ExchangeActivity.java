package tinkoff.fintech.exchange.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.network.ApiResponse;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.Formatter;

public class ExchangeActivity extends AppCompatActivity {

    private void sendRequest(String from, String to) {
        Call<ApiResponse> responseCall = RetrofitClient.getInstance().getService().latest(from, to);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                TextView tv = findViewById(R.id.exchange_result_decimal);
                EditText ed = findViewById(R.id.exchange_input_decimal);
                double rate = response.body().getRates().getRate();
                double mul = Double.parseDouble(ed.getText().toString());
                double result = rate * mul;
                String res = Formatter.decimal(result);

                tv.setText(res);

                ExchangeOperation operation = new ExchangeOperation(Calendar.getInstance().getTime(),
                        from, to, mul, result);
                AsyncTask.execute(() -> AppDatabase.getAppDatabase(getApplicationContext())
                        .exchangeOperationDao()
                        .insertAll(operation));
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("error", "");
                Toast toast = Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("activityCreated", getClass().getCanonicalName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        Intent intent = getIntent();

        final String from = intent.getStringExtra(MainActivity.FROM_CURRENCY);
        TextView textView = findViewById(R.id.first_currency);
        textView.setText(from);

        final String to = intent.getStringExtra(MainActivity.TO_CURRENCY);
        TextView textView1 = findViewById(R.id.second_currency);
        textView1.setText(to);

        Button button = findViewById(R.id.exchange_button);
        button.setOnClickListener(view -> sendRequest(from, to));

    }
}
