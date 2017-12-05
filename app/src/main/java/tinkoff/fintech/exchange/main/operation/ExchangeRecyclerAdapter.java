package tinkoff.fintech.exchange.main.operation;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


public class ExchangeRecyclerAdapter extends RecyclerView.Adapter<ExchangeRecyclerAdapter.ViewHolder> {

    private List<Currency> currencies;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public CheckBox mCheckbox;

        public ViewHolder(View v) {
            super(v);
            mText = v.findViewById(R.id.label);
            mCheckbox = v.findViewById(R.id.check);
        }

    }

    public ExchangeRecyclerAdapter(List<Currency> currencies, CompoundButton.OnCheckedChangeListener onCheckedChangeListener,
                                   View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        this.currencies = currencies;
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
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

        // TODO Выпилить на метод viewHolder https://stackoverflow.com/questions/27070220/android-recyclerview-notifydatasetchanged-illegalstateexception
        holder.mCheckbox.setOnCheckedChangeListener(null);
        holder.mText.setOnClickListener(null);
        holder.mText.setOnLongClickListener(null);

        holder.mText.setText(currencies.get(position).getName());
        holder.mCheckbox.setChecked(currencies.get(position).isFavourite());

        holder.mText.setTag(position);
        holder.mCheckbox.setTag(position);

        holder.mCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
        holder.mText.setOnClickListener(onClickListener);
        holder.mText.setOnLongClickListener(onLongClickListener);

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void addItems(List<Currency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }
}
