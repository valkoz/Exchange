package tinkoff.fintech.exchange.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.HistoryListAdapter;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.daoTasks.GetAllOperationHistory;
import tinkoff.fintech.exchange.model.ExchangeOperation;

public class HistoryFragment extends ListFragment {


    public HistoryFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<ExchangeOperation> adapter = null;
        try {
            adapter = new HistoryListAdapter(getActivity(), getModel());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        setListAdapter(adapter);
    }

    private List<ExchangeOperation> getModel() throws ExecutionException, InterruptedException {
        return new GetAllOperationHistory(AppDatabase.getAppDatabase(getContext())).execute().get();
    }


    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}
