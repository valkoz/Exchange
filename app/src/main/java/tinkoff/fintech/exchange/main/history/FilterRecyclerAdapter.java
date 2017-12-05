package tinkoff.fintech.exchange.main.history;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.Currency;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder> {

    private List<Currency> currencies;
    private CompoundButton.OnCheckedChangeListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.label);
            checkbox = v.findViewById(R.id.check);
        }

    }

    public FilterRecyclerAdapter(List<Currency> currencies, CompoundButton.OnCheckedChangeListener listener) {
        this.currencies = currencies;
        this.listener = listener;
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

        holder.checkbox.setOnCheckedChangeListener(null); // TODO Выпилить на метод viewHolder https://stackoverflow.com/questions/27070220/android-recyclerview-notifydatasetchanged-illegalstateexception
        holder.text.setText(currencies.get(position).getName());
        holder.checkbox.setChecked(currencies.get(position).isFavourite());
        holder.checkbox.setTag(position);
        holder.checkbox.setOnCheckedChangeListener(listener);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void addItems(List<Currency> currencies) {
        this.currencies = currencies;
    }

}
