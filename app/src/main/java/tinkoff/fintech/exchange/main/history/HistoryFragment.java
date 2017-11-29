package tinkoff.fintech.exchange.main.history;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.daoTasks.GetAllOperationHistory;
import tinkoff.fintech.exchange.model.ExchangeOperation;

public class HistoryFragment extends ListFragment {

    final int REQUEST_CODE = 1;

    public HistoryFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), FilterActivity.class), REQUEST_CODE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CODE) {
            ArrayList<String> names = data.getExtras().getStringArrayList("currencies");
            long fromDate = data.getExtras().getLong("from");
            long toDate = data.getExtras().getLong("to");

            ArrayAdapter<ExchangeOperation> adapter = null;
            if (names.isEmpty()) {
                adapter = new HistoryListAdapter(getActivity(),
                        AppDatabase.getAppDatabase(getContext()).exchangeOperationDao().getByDate(fromDate, toDate));
            } else {
                adapter = new HistoryListAdapter(getActivity(),
                        AppDatabase.getAppDatabase(getContext()).exchangeOperationDao().getByDateAndName(names, fromDate, toDate));
            }
            setListAdapter(adapter);
        }
    }
}
