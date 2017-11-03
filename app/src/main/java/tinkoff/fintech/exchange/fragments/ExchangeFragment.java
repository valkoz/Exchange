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
import android.widget.ListView;
import android.widget.Toast;

import tinkoff.fintech.exchange.ExchangeActivity;
import tinkoff.fintech.exchange.R;

import static tinkoff.fintech.exchange.MainActivity.EXTRA_MESSAGE;

//TODO: Custom ListView with favorite button
//TODO: Custom ListAdapter sort elements as long click chosen -> favourites -> most frequently used -> others

public class ExchangeFragment extends ListFragment {



    private final String[] coins = {"USD", "EUR", "RUB", "JPY", "GBP", "ETH", "BTC", "LOL", "OMG", "WTF", "KKK", "ZZZ", "VVV"};

    public ExchangeFragment() {
    }


    public static ExchangeFragment newInstance() {
        ExchangeFragment fragment = new ExchangeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, coins);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fragmentCreated", getClass().getCanonicalName());
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ExchangeActivity.class);
        String item = (String) getListAdapter().getItem(position);
        intent.putExtra(EXTRA_MESSAGE, item);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                Toast toast = Toast.makeText(getContext(), "Long Clicked", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });
    }
}
