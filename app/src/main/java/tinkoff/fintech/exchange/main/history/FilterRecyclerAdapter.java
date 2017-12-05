package tinkoff.fintech.exchange.main.history;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder> {

    private List<Currency> mCurrencies;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public CheckBox mCheckbox;

        public ViewHolder(View v) {
            super(v);
            mText = v.findViewById(R.id.label);
            mCheckbox = v.findViewById(R.id.check);
        }

    }

    public FilterRecyclerAdapter(List<Currency> currencies) {
        mCurrencies = currencies;
    }

    @Override
    public FilterRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mText.setText(mCurrencies.get(position).getName());
        holder.mCheckbox.setChecked(mCurrencies.get(position).isFavourite());

        holder.mCheckbox
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                        mCurrencies.get(position).setFavourite(isChecked);
                });
    }

    @Override
    public int getItemCount() {
        return mCurrencies.size();
    }

    public List<String> getChosenCurrencies() {
        List<String> list = new ArrayList<>();
        for (Currency currency:
             mCurrencies) {
            if (currency.isFavourite())
                list.add(currency.getName());
        }
        return list;
    }

}
