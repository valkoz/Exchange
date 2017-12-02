package tinkoff.fintech.exchange.main.operation;

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

import java.util.Collections;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

import static tinkoff.fintech.exchange.main.MainActivity.FROM_CURRENCY;
import static tinkoff.fintech.exchange.main.MainActivity.TO_CURRENCY;

public class ExchangeListAdapter extends ArrayAdapter<Currency> {

    private final Activity context;
    private List<Currency> currencies;
    private Currency choosenCurrency;

    public ExchangeListAdapter(Activity context, List<Currency> list) {
        super(context, R.layout.item_list, list);
        this.context = context;
        this.currencies = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    //TODO move logic to ViewModel -> call model methods from here. Init model in constructor

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {

        View viewItem;
        if (convertView == null) {

            final LayoutInflater inflator = context.getLayoutInflater();
            viewItem = inflator.inflate(R.layout.item_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = viewItem.findViewById(R.id.label);
            viewHolder.checkbox = viewItem.findViewById(R.id.check);
            final TextView tv = context.findViewById(R.id.selected_currency);

            viewHolder.checkbox
                    .setOnCheckedChangeListener((buttonView, isChecked) -> {

                        //get clicked item
                        Currency element = (Currency) viewHolder.checkbox
                                .getTag();
                        //set him to favourite
                        element.setFavourite(buttonView.isChecked());
                        //update in db
                        updateDb(element);

                        Collections.sort(currencies);

                        notifyDataSetChanged();

                    });

            viewHolder.text
                    .setOnClickListener(view -> {

                        String fromCurrency;
                        String toCurrency;

                        if (choosenCurrency != null) {
                            fromCurrency = choosenCurrency.getName();
                            toCurrency = viewHolder.text.getText().toString();
                        } else {
                            fromCurrency = viewHolder.text.getText().toString();
                            toCurrency = currencies.get(0).getName();
                            if (fromCurrency.equals(toCurrency))
                                toCurrency = currencies.get(1).getName();
                        }

                        Intent intent = new Intent(context, ExchangeActivity.class);
                        intent.putExtra(TO_CURRENCY, toCurrency);
                        intent.putExtra(FROM_CURRENCY, fromCurrency);
                        context.startActivity(intent);
                    });

            viewHolder.text
                    .setOnLongClickListener(view -> {

                        //add to list
                        if (choosenCurrency != null)
                            currencies.add(choosenCurrency);

                        //get clicked item
                        Currency element = (Currency) viewHolder.checkbox
                                .getTag();

                        //remove from list
                        choosenCurrency = element;
                        currencies.remove(element);

                        Collections.sort(currencies);

                        notifyDataSetChanged();

                        tv.setText(viewHolder.text.getText());
                        return true;
                    });

            viewItem.setTag(viewHolder);
            viewHolder.checkbox.setTag(currencies.get(position));
        } else {
            viewItem = convertView;
            ((ViewHolder) viewItem.getTag()).checkbox.setTag(currencies.get(position));
        }
        ViewHolder holder = (ViewHolder) viewItem.getTag();
        holder.text.setText(currencies.get(position).getName());
        holder.checkbox.setChecked(currencies.get(position).isFavourite());

        return viewItem;
    }

    private void updateDb(Currency element) {
        AsyncTask.execute(() -> AppDatabase.getAppDatabase(context)
                .currencyDao()
                .update(element));
    }

}
