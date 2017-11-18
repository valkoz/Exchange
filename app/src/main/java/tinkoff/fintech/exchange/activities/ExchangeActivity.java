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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.BuildConfig;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.network.Api;
import tinkoff.fintech.exchange.network.ApiResponse;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RatesDeserializer;
import tinkoff.fintech.exchange.util.Formatter;

public class ExchangeActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build();

    private Gson gson = new GsonBuilder().registerTypeAdapter(RateObject.class, new RatesDeserializer()).create();
    private Retrofit retrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl("http://api.fixer.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private Api service = retrofit.create(Api.class);

    public Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    private void sendRequest(String from, String to) {
        Call<ApiResponse> responseCall = service.latest(from, to);
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
