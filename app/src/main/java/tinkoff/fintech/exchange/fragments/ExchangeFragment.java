package tinkoff.fintech.exchange.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.daoTasks.GetAllCurrencies;
import tinkoff.fintech.exchange.ExchangeListAdapter;
import tinkoff.fintech.exchange.R;

//TODO: FIXME Incorrect ListView state when download, adapter.notifyDataSetChanged() after init doesn't helps
public class ExchangeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ExchangeFragment() {}

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getView().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            List<Currency> currencies = getModel();
            Collections.sort(currencies);
            mAdapter = new MyAdapter(currencies);
            mRecyclerView.setAdapter(mAdapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Currency> getModel() throws ExecutionException, InterruptedException {
        return new GetAllCurrencies(AppDatabase.getAppDatabase(getContext())).execute().get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mRecyclerView = getView().findViewById(R.id.my_recycler_view);
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

}
