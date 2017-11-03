package tinkoff.fintech.exchange;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tinkoff.fintech.exchange.fragments.AnalyticsFragment;
import tinkoff.fintech.exchange.fragments.ExchangeFragment;
import tinkoff.fintech.exchange.fragments.HistoryFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TO_CURRENCY = "tinkoff.fintech.exchange.TO_CURRENCY";
    public static final String FROM_CURRENCY = "tinkoff.fintech.exchange.FROM_CURRENCY";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        }
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        chooseFragment(ExchangeFragment.newInstance());
        navigation.getMenu().getItem(1).setChecked(true);

    }

}
