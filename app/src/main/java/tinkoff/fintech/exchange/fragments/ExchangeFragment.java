package tinkoff.fintech.exchange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.Currency;
import tinkoff.fintech.exchange.ExchangeListAdapter;
import tinkoff.fintech.exchange.R;

//TODO: Custom ListView with favorite button
//TODO: Custom ListAdapter sort elements as long click chosen -> favourites -> most frequently used -> others

public class ExchangeFragment extends ListFragment {

    private final String[] coins = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

    public ExchangeFragment() {}


    public static ExchangeFragment newInstance() {
        ExchangeFragment fragment = new ExchangeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<Currency> adapter = new ExchangeListAdapter(getActivity(), getModel());
        setListAdapter(adapter);
    }

    private List<Currency> getModel() {
        List<Currency> list = new ArrayList<Currency>();
        for (String coin: coins) {
            list.add(get(coin));
        }
        // Первоначальный выбор одного из элементов
        list.get(1).setChecked(true);
        return list;
    }

    private Currency get(String s) {
        return new Currency(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fragmentCreated", getClass().getCanonicalName());
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

}
