package tinkoff.fintech.exchange.main.history;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.main.operation.ExchangeActivity;
import tinkoff.fintech.exchange.main.operation.ExchangeListAdapter;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

import static tinkoff.fintech.exchange.main.MainActivity.FROM_CURRENCY;
import static tinkoff.fintech.exchange.main.MainActivity.TO_CURRENCY;


public class FilterListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<String> currencies;
    private List<String> choosenCurrencies = new ArrayList<>();

    public FilterListAdapter(Activity context, List<String> list) {
        super(context, R.layout.item_list, list);
        this.context = context;
        this.currencies = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {

        View viewItem;
        if (convertView == null) {

            final LayoutInflater inflator = context.getLayoutInflater();
            viewItem = inflator.inflate(R.layout.item_list, null);
            final FilterListAdapter.ViewHolder viewHolder = new FilterListAdapter.ViewHolder();
            viewHolder.text = viewItem.findViewById(R.id.label);
            viewHolder.checkbox = viewItem.findViewById(R.id.check);

            viewHolder.checkbox
                    .setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked)
                            choosenCurrencies.add(viewHolder.text.getText().toString());
                        else
                            choosenCurrencies.remove(viewHolder.text.getText().toString());
                    });

            viewItem.setTag(viewHolder);
            viewHolder.checkbox.setTag(currencies.get(position));
        } else {
            viewItem = convertView;
            ((FilterListAdapter.ViewHolder) viewItem.getTag()).checkbox.setTag(currencies.get(position));
        }
        FilterListAdapter.ViewHolder holder = (FilterListAdapter.ViewHolder) viewItem.getTag();
        holder.text.setText(currencies.get(position));

        return viewItem;
    }

    public List<String> getChoosenCurrencies() {
        return choosenCurrencies;
    }

}