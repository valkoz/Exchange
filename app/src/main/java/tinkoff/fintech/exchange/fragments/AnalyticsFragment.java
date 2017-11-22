package tinkoff.fintech.exchange.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.CurrencyName;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.network.ErrorType;
import tinkoff.fintech.exchange.network.RateCallback;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;
import tinkoff.fintech.exchange.views.GraphView;

public class AnalyticsFragment extends ListFragment {

    public enum Period {
        WEEK,
        TWO_WEEKS,
        MOUNTH
    }

    public AnalyticsFragment() {
        // Required empty public constructor
    }


    public static AnalyticsFragment newInstance() {
        AnalyticsFragment fragment = new AnalyticsFragment();
        return fragment;
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
        for (CurrencyName coin: CurrencyName.values()) {
            coins.add(coin.name());
        }

        ArrayAdapter<String> adapter  =  new  ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, coins);
        setListAdapter(adapter);

        RateCallback rateCallback = new RateCallback() {
            @Override
            public void onSuccess(RateObject rate) {
                Toast.makeText(getContext(), rate.getName() + rate.getRate(), Toast.LENGTH_SHORT).show();
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

        List<Point> items = new ArrayList<>();
        items.add(new Point(1, 63));
        items.add(new Point(2, 62));
        items.add(new Point(3, 61));
        items.add(new Point(4, 62));
        items.add(new Point(5, 60));
        items.add(new Point(6, 63));
        items.add(new Point(7, 63));
        final GraphView graph = getActivity().findViewById(R.id.graphView);
        graph.setItems(items);

        Paint p1 = new Paint();
        p1.setColor(Color.BLUE);
        p1.setStrokeWidth(5);
        graph.setPlotPaint(p1);

        graph.setGridStep(20);
        graph.setLabelStep(5);

        RadioGroup radioGroup = getActivity().findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<Date> dates = new ArrayList<>();

                switch (checkedId){
                    case R.id.analytics_filter_week :
                        dates = CalendarIterator.getLast(Period.WEEK);
                        break;
                    case R.id.analytics_filter_two_weeks :
                        dates = CalendarIterator.getLast(Period.TWO_WEEKS);
                        break;
                    case R.id.analytics_filter_month :
                        dates = CalendarIterator.getLast(Period.MOUNTH);
                        break;
                    default:
                        break;
                }

                for (Date date: dates) {
                    RetrofitClient.getInstance().sendRequestWithDate(rateCallback, Formatter.dateToRestrofit(date), "RUB" );
                }
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String s = ((TextView) v).getText().toString();
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
