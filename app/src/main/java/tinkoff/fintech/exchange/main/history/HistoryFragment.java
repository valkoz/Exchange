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

import java.util.ArrayList;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;

public class HistoryFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private HistoryViewModel model;
    private RecyclerView mRecyclerView;
    private HistoryListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HistoryFragment() { }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = view.findViewById(R.id.history_list_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryListAdapter(new ArrayList<ExchangeOperation>());
        mRecyclerView.setAdapter(mAdapter);


        model = ViewModelProviders.of(this).get(HistoryViewModel.class);
        model.getCurrencies().observe(this, exchangeOperations -> mAdapter.addAll(exchangeOperations));

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
