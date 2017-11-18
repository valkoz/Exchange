package tinkoff.fintech.exchange.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.Currency;
import tinkoff.fintech.exchange.DaoTasks.GetAllCurrencies;
import tinkoff.fintech.exchange.ExchangeListAdapter;
import tinkoff.fintech.exchange.R;

//TODO: Custom ListView with favorite button
//TODO: Custom ListAdapter sort elements as long click chosen -> favourites -> most frequently used -> others

public class ExchangeFragment extends ListFragment {


    public ExchangeFragment() {}

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<Currency> adapter = null;
        try {
            adapter = new ExchangeListAdapter(getActivity(), getModel());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        setListAdapter(adapter);
    }

    private List<Currency> getModel() throws ExecutionException, InterruptedException {
        return new GetAllCurrencies(AppDatabase.getAppDatabase(getContext())).execute().get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fragmentCreated", getClass().getCanonicalName());
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

}
