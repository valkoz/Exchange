package tinkoff.fintech.exchange.main.operation;


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


public class ExchangeRecyclerAdapter extends RecyclerView.Adapter<ExchangeRecyclerAdapter.ViewHolder> {

    private List<Currency> currencies;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.label);
            checkBox = v.findViewById(R.id.check);
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

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.textView.setOnClickListener(null);
        holder.textView.setOnLongClickListener(null);

        holder.textView.setText(currencies.get(position).getName());
        holder.checkBox.setChecked(currencies.get(position).isFavourite());

        holder.textView.setTag(position);
        holder.checkBox.setTag(position);

        holder.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        holder.textView.setOnClickListener(onClickListener);
        holder.textView.setOnLongClickListener(onLongClickListener);

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
