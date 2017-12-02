package tinkoff.fintech.exchange.main.operation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;

public class ExchangeFragment extends Fragment {

    ExchangeViewModel model;
    ArrayAdapter<Currency> adapter;
    private ListView mListView;

    public ExchangeFragment() {}

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        mListView = view.findViewById(R.id.exchange_list);

        model = ViewModelProviders.of(this).get(ExchangeViewModel.class);
        model.getCurrencies().observe(this, currencies -> {
            adapter = new ExchangeListAdapter(getActivity(), currencies);
            mListView.setAdapter(adapter);
        });

        return view;
    }
}
