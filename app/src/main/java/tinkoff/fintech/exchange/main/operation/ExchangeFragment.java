package tinkoff.fintech.exchange.main.operation;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.pojo.ExchangePair;

import static tinkoff.fintech.exchange.main.MainActivity.FROM_CURRENCY;
import static tinkoff.fintech.exchange.main.MainActivity.TO_CURRENCY;

public class ExchangeFragment extends Fragment {

    private ExchangeViewModel model;
    private RecyclerView recyclerView;
    private ExchangeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textView;


    public ExchangeFragment() {}

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        model = ViewModelProviders.of(this).get(ExchangeViewModel.class);

        textView = view.findViewById(R.id.selected_currency);

        recyclerView = view.findViewById(R.id.exchange_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView view = (TextView) v;
                model.updateOnLongClick((Integer) view.getTag());
                textView.setText(view.getText());
                adapter.notifyDataSetChanged();
                return true;
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView) v;
                ExchangePair pair = model.getCurrencyPair(view.getText().toString());

                Intent intent = new Intent(getContext(), ExchangeActivity.class);
                intent.putExtra(TO_CURRENCY, pair.getToCurrency());
                intent.putExtra(FROM_CURRENCY, pair.getFromCurrency());
                startActivityForResult(intent,2);
            }
        };

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                model.updateOnCheckChanged((Integer) compoundButton.getTag(), isChecked);
                adapter.notifyDataSetChanged();
            }
        };


        adapter = new ExchangeRecyclerAdapter(new ArrayList<Currency>(), onCheckedChangeListener, onClickListener, onLongClickListener);
        recyclerView.setAdapter(adapter);

        model.getCurrencies().observe(this, currencies -> {
            adapter.addItems(currencies);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        textView.setText("");
        model.updateCurrencies();
    }
}
