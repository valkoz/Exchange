package tinkoff.fintech.exchange.main.operation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;

public class ExchangeFragment extends ListFragment {

    ExchangeViewModel model;
    ArrayAdapter<Currency> adapter;

    public ExchangeFragment() {}

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(this).get(ExchangeViewModel.class);
        model.getCurrencies().observe(this, currencies -> {
            adapter = new ExchangeListAdapter(getActivity(), currencies);
            setListAdapter(adapter);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

}
