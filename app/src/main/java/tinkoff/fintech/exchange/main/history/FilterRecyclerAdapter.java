package tinkoff.fintech.exchange.main.history;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.R;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder> {

    private List<String> mCurrencies;
    private List<String> mChoosenCurrencies;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public CheckBox mCheckbox;

        public ViewHolder(View v) {
            super(v);
            mText = v.findViewById(R.id.label);
            mCheckbox = v.findViewById(R.id.check);
        }

    }

    public FilterRecyclerAdapter(List<String> currencies, List<String> choosenCurrencies) {
        mChoosenCurrencies = choosenCurrencies;
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

        holder.mText.setText(mCurrencies.get(position));

        for (String cur: mChoosenCurrencies) {
            if (cur.equals(mCurrencies.get(position))) {
                holder.mCheckbox.setChecked(true);
            }
        }

        holder.mCheckbox
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked)
                        mChoosenCurrencies.add(holder.mText.getText().toString());
                    else
                        mChoosenCurrencies.remove(holder.mText.getText().toString());
                });
    }

    @Override
    public int getItemCount() {
        return mCurrencies.size();
    }

    public ArrayList<String> getChoosenCurrencies() {
        Log.i("fromAdapter", mChoosenCurrencies.toString());
        return new ArrayList<>(mChoosenCurrencies);
    }

}
