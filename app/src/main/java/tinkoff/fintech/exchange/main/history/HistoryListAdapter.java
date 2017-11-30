package tinkoff.fintech.exchange.main.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.util.Formatter;


public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private List<ExchangeOperation> history;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView datetime;
        public TextView from;
        public TextView to;
        public TextView fromValue;
        public TextView toValue;

        public ViewHolder(View v) {
            super(v);
            datetime = v.findViewById(R.id.history_datetime);
            from = v.findViewById(R.id.history_from);
            to = v.findViewById(R.id.history_to);
            fromValue = v.findViewById(R.id.history_from_value);
            toValue = v.findViewById(R.id.history_to_value);
        }

    }

    public HistoryListAdapter(List<ExchangeOperation> list) {
        history = list;
    }

    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.datetime.setText(Formatter.dateToLongString(history.get(position).getCreatedDateTime()));
        holder.from.setText(history.get(position).getFrom());
        holder.to.setText(history.get(position).getTo());

        holder.fromValue.setText(Formatter.doubleToString(history.get(position).getFromValue()));
        holder.toValue.setText(Formatter.doubleToString(history.get(position).getToValue()));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public void addAll(List<ExchangeOperation> newsObjects) {
        history = newsObjects;
        notifyDataSetChanged();
    }

}
