package tinkoff.fintech.exchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ExchangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("activityCreated", getClass().getCanonicalName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.first_currency);
        textView.setText(message);

    }
}
