package tinkoff.fintech.exchange.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tinkoff.fintech.exchange.BuildConfig;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.enums.CurrencyName;
import tinkoff.fintech.exchange.main.analytics.AnalyticsFragment;
import tinkoff.fintech.exchange.main.history.HistoryFragment;
import tinkoff.fintech.exchange.main.operation.ExchangeFragment;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String TO_CURRENCY = "tinkoff.fintech.exchange.TO_CURRENCY";
    public static final String FROM_CURRENCY = "tinkoff.fintech.exchange.FROM_CURRENCY";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        chooseFragment(HistoryFragment.newInstance());
                        return true;
                    case R.id.navigation_exchange:
                        chooseFragment(ExchangeFragment.newInstance());
                        return true;
                    case R.id.navigation_analytics:
                        chooseFragment(AnalyticsFragment.newInstance());
                        return true;
                }
                return false;
            };

    private void chooseFragment(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, selectedFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFirstRun();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        chooseFragment(ExchangeFragment.newInstance());
        navigation.getMenu().getItem(1).setChecked(true);

    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        int currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (currentVersionCode == savedVersionCode) {
            return;
        } else if (savedVersionCode == DOESNT_EXIST) {
            Log.i("DatabaseInfo", "first run initialization");
            for (CurrencyName coin : CurrencyName.values()) {
                AppDatabase.getAppDatabase(getApplicationContext()).currencyDao().insertAll(new Currency(coin.name()));
            }
        }

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

}
