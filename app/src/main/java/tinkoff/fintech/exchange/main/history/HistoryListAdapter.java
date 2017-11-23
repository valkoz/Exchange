package tinkoff.fintech.exchange.main.history;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.util.Formatter;


public class HistoryListAdapter extends ArrayAdapter<ExchangeOperation> {

    private final Activity context;
    private List<ExchangeOperation> history;

    public HistoryListAdapter(Activity context, List<ExchangeOperation> list) {
        super(context, R.layout.item_list, list);
        this.context = context;
        this.history = list;
    }

    static class ViewHolder {
        protected TextView datetime;
        protected TextView from;
        protected TextView to;
        protected TextView fromValue;
        protected TextView toValue;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {

        View viewItem;
        if (convertView == null) {

            final LayoutInflater inflator = context.getLayoutInflater();
            viewItem = inflator.inflate(R.layout.item_history_list, null);
            final HistoryListAdapter.ViewHolder viewHolder = new HistoryListAdapter.ViewHolder();
            viewHolder.datetime = viewItem.findViewById(R.id.history_datetime);
            viewHolder.from = viewItem.findViewById(R.id.history_from);
            viewHolder.to = viewItem.findViewById(R.id.history_to);
            viewHolder.fromValue = viewItem.findViewById(R.id.history_from_value);
            viewHolder.toValue = viewItem.findViewById(R.id.history_to_value);

            viewItem.setTag(viewHolder);

        } else {
            viewItem = convertView;
        }
        HistoryListAdapter.ViewHolder holder = (HistoryListAdapter.ViewHolder) viewItem.getTag();

        holder.datetime.setText(Formatter.dateToLongString(history.get(position).getCreatedDateTime()));
        holder.from.setText(history.get(position).getFrom());
        holder.to.setText(history.get(position).getTo());

        holder.fromValue.setText(Formatter.doubleToString(history.get(position).getFromValue()));
        holder.toValue.setText(Formatter.doubleToString(history.get(position).getToValue()));

        return viewItem;
    }

}
