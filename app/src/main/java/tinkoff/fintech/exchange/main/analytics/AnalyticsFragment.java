package tinkoff.fintech.exchange.main.analytics;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.CurrencyName;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.network.ErrorType;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RateWithDateCallback;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;

public class AnalyticsFragment extends ListFragment {

    RadioGroup radioGroup;
    RateWithDateCallback rateCallback;
    List<GraphPoint> items = new ArrayList<>();

    public enum Period {
        WEEK,
        TWO_WEEKS,
        MONTH
    }

    public AnalyticsFragment() {
        // Required empty public constructor
    }


    public static AnalyticsFragment newInstance() {
        return new AnalyticsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> coins = new ArrayList<>();
        for (CurrencyName coin : CurrencyName.values()) {
            coins.add(coin.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, coins);
        setListAdapter(adapter);

        final GraphView graph = getActivity().findViewById(R.id.graphView);

        Paint p1 = new Paint();
        p1.setColor(Color.BLUE);
        p1.setStrokeWidth(5);
        graph.setPlotPaint(p1);
        graph.setGridStep(20);
        graph.setLabelStep(4);
        graph.setScaleYLabelDateFormat(true);

        rateCallback = new RateWithDateCallback() {
            @Override
            public void onSuccess(RateObject rate, Date date) {
                items.add(new GraphPoint(date, rate.getRate()));
                if (items.size() == 7) {
                    //List<GraphPoint> withoutDuplicates = new ArrayList<>(new HashSet<GraphPoint>(items));
                    Collections.sort(items, new Comparator<GraphPoint>() {
                        @Override
                        public int compare(GraphPoint graphPoint, GraphPoint t1) {
                            return  Float.compare(graphPoint.getX(), t1.getX());
                        }
                    });
                    Log.i("Collection Items", items.toString());
                    graph.setItems(items);
                    graph.invalidate();
                    items.clear();
                }
                //TODO: Add Point to graphList as items.add(new Point(rate.getDate(), rate.getRate));
            }

            @Override
            public void onError(ErrorType type) {
                Toast.makeText(getContext(),
                        "Error",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };

        radioGroup = getActivity().findViewById(R.id.radioGroup);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String currencyName = ((TextView) v).getText().toString();
        List<Date> dates = new ArrayList<>();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.analytics_filter_week:
                dates = CalendarIterator.getLast(Period.WEEK);
                break;
            case R.id.analytics_filter_two_weeks:
                dates = CalendarIterator.getLast(Period.TWO_WEEKS);
                break;
            case R.id.analytics_filter_month:
                dates = CalendarIterator.getLast(Period.MONTH);
                break;
            default:
                break;
        }

        for (Date date : dates) {
            items.clear();
            RetrofitClient.getInstance().sendRequestWithDate(rateCallback, Formatter.dateToString(date), currencyName);
        }
        //TODO: After all data received and added to list - show graph
    }
}
