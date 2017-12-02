package tinkoff.fintech.exchange.main.operation;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.main.history.FilterRecyclerAdapter;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

import static tinkoff.fintech.exchange.main.MainActivity.FROM_CURRENCY;
import static tinkoff.fintech.exchange.main.MainActivity.TO_CURRENCY;


//TODO Use this adapter instead of ListAdapter
public class ExchangeRecyclerAdapter extends RecyclerView.Adapter<ExchangeRecyclerAdapter.ViewHolder> {

    private final Activity mContext;
    private List<Currency> mCurrencies;
    private Currency mChosenCurrency;
    private TextView mTextView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public CheckBox mCheckbox;

        public ViewHolder(View v) {
            super(v);
            mText = v.findViewById(R.id.label);
            mCheckbox = v.findViewById(R.id.check);
        }

    }

    public ExchangeRecyclerAdapter(Activity context, List<Currency> currencies) {
        mContext = context;
        mCurrencies = currencies;
        mTextView = mContext.findViewById(R.id.selected_currency);
    }

    @Override
    public ExchangeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        ExchangeRecyclerAdapter.ViewHolder vh = new ExchangeRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ExchangeRecyclerAdapter.ViewHolder holder, int position) {

        holder.mText.setText(mCurrencies.get(position).getName());
        holder.mCheckbox.setChecked(mCurrencies.get(position).isFavourite());

        holder.mCheckbox
                .setOnCheckedChangeListener((buttonView, isChecked) -> {


                    Currency element = mCurrencies.get(position);

                    //set him to favourite
                    element.setFavourite(isChecked);
                    //update in db
                    updateDb(element);
                    Collections.sort(mCurrencies);

                    notifyDataSetChanged();
                });

        holder.mText.setOnClickListener(view -> {

            String toCurrency = holder.mText.getText().toString();
            String fromCurrency = mCurrencies.get(0).getName();
            if (fromCurrency.equals(toCurrency))
                fromCurrency = mCurrencies.get(1).getName();
            if (mChosenCurrency != null) {
                fromCurrency = mChosenCurrency.getName();
            }

            Intent intent = new Intent(mContext, ExchangeActivity.class);
            intent.putExtra(TO_CURRENCY, toCurrency);
            intent.putExtra(FROM_CURRENCY, fromCurrency);
            mContext.startActivity(intent);

        });

        holder.mText.setOnLongClickListener(view -> {

            //add to list
            if (mChosenCurrency != null)
                mCurrencies.add(mChosenCurrency);

            //get clicked item
            Currency element = mCurrencies.get(position);
            element.increaseUseFrequency();

            //update db
            updateDb(element);

            //remove from list
            mChosenCurrency = element;
            mCurrencies.remove(element);

            Collections.sort(mCurrencies);

            notifyDataSetChanged();

            mTextView = mContext.findViewById(R.id.selected_currency);
            mTextView.setText(holder.mText.getText());
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return mCurrencies.size();
    }


    private void updateDb(Currency element) {
        AsyncTask.execute(() -> AppDatabase.getAppDatabase(mContext)
                .currencyDao()
                .update(element));
    }

    public void setCurrencies(List<Currency> currencies) {
        //Log.e("setCurrencies", currencies.toString());
        mCurrencies.clear();
        mCurrencies = currencies;
        notifyDataSetChanged();
    }
}
