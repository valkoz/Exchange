package tinkoff.fintech.exchange.main.history;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.util.Formatter;

public class HistoryFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private TextView currentFilterTextView;
    private HistoryViewModel model;
    private RecyclerView recyclerView;
    private HistoryListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public HistoryFragment() { }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        currentFilterTextView = view.findViewById(R.id.history_chosen_filter_info);
        recyclerView = view.findViewById(R.id.history_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryListAdapter(new ArrayList<ExchangeOperation>());
        recyclerView.setAdapter(adapter);


        model = ViewModelProviders.of(this).get(HistoryViewModel.class);
        model.getOperations().observe(this, exchangeOperations -> adapter.addAll(exchangeOperations));
        model.getLastQuery().observe(this, historyQuery -> {
            if (historyQuery != null) {
                String lastFilter;
                if (historyQuery.getCurrencies().isEmpty()) {
                    lastFilter = "From: " + Formatter.dateToString(historyQuery.getFromDate()) + "\n" +
                            "To: " + Formatter.dateToString(historyQuery.getToDate());
                } else {
                    lastFilter = "From: " + Formatter.dateToString(historyQuery.getFromDate()) + "\n" +
                            "To: " + Formatter.dateToString(historyQuery.getToDate()) + "\n" +
                            "With: " + historyQuery.getCurrencies().toString().substring(1, historyQuery.getCurrencies().toString().length() - 1);
                }
                currentFilterTextView.setText(lastFilter);
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> {startActivityForResult(new Intent(getContext(), FilterActivity.class), REQUEST_CODE);});

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CODE) {
            ArrayList<String> names = data.getExtras().getStringArrayList("currencies");
            long fromDate = data.getExtras().getLong("from");
            long toDate = data.getExtras().getLong("to");
            model.get(names, fromDate, toDate);
        }
    }
}
