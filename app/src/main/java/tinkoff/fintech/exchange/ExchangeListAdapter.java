package tinkoff.fintech.exchange;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static tinkoff.fintech.exchange.MainActivity.EXTRA_MESSAGE;

//TODO: Save state
//TODO: Delete longClicked Item - doesn't matters
//TODO: Exchange Activity

public class ExchangeListAdapter extends ArrayAdapter<Currency> {

    private final Activity context;
    private List<Currency> currencies;

    public ExchangeListAdapter(Activity context, List<Currency> list) {
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

        View viewItem = null;
        if (convertView == null) {

            LayoutInflater inflator = context.getLayoutInflater();
            viewItem = inflator.inflate(R.layout.item_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = viewItem.findViewById(R.id.label);
            viewHolder.checkbox = viewItem.findViewById(R.id.check);

            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            Currency element = (Currency) viewHolder.checkbox
                                    .getTag();
                            element.setChecked(buttonView.isChecked());

                            sort();
                            notifyDataSetChanged();

                        }
                    });

            viewHolder.text
                    .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ExchangeActivity.class);
                    String item = viewHolder.text.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE, item);
                    context.startActivity(intent);
                }
            });

            viewHolder.text
                    .setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    TextView tv = context.findViewById(R.id.selected_currency);
                    tv.setText(viewHolder.text.getText());
                    return true;
                }
            });

            viewItem.setTag(viewHolder);
            viewHolder.checkbox.setTag(currencies.get(position));
        } else {
            viewItem = convertView;
            ((ViewHolder) viewItem.getTag()).checkbox.setTag(currencies.get(position));
        }
        ViewHolder holder = (ViewHolder) viewItem.getTag();
        holder.text.setText(currencies.get(position).getName());
        holder.checkbox.setChecked(currencies.get(position).isChecked());

        return viewItem;
    }

    private void sort() {
        Collections.sort(currencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency a, Currency b) {
                return Boolean.compare(!a.isChecked(), !b.isChecked());
            }
        });
    }
}
