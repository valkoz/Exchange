package tinkoff.fintech.exchange.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.AnalyticsResponseBuffer;
import tinkoff.fintech.exchange.CurrencyName;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.network.ErrorType;
import tinkoff.fintech.exchange.network.RateCallback;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RateWithDateCallback;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;

public class AnalyticsFragment extends ListFragment {

    RadioGroup radioGroup;
    RateWithDateCallback rateCallback;

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

        GraphView graph = getActivity().findViewById(R.id.graph);
        List<DataPoint> dataPoints = new ArrayList<>();

        rateCallback = new RateWithDateCallback() {
            @Override
            public void onSuccess(RateObject rate, Date date) {
                Log.i("Retrofit returns", rate.getName() + " " + rate.getRate() + " " + Formatter.dateToRestrofit(date));
                //Toast.makeText(getContext(), rate.getName() + " " + rate.getRate() + " " + Formatter.dateToRestrofit(date), Toast.LENGTH_SHORT).show();
                dataPoints.add(new DataPoint(date, rate.getRate()));
                if (dataPoints.size() == 7) {
                    Log.i("DataPoints", dataPoints.toString());
                    DataPoint[] dps = new DataPoint[dataPoints.size()];
                    Collections.sort(dataPoints, new Comparator<DataPoint>() {
                        @Override
                        public int compare(DataPoint dataPoint, DataPoint t1) {
                            if (dataPoint.getX() > dataPoint.getY())
                                return 1;
                            else
                                return 0;
                        }
                    });
                    dataPoints.toArray(dps);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dps);
                    graph.addSeries(series);
                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                    graph.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graph.getViewport().setMinX(69.5);
                    graph.getViewport().setMaxX(70.5);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getGridLabelRenderer().setHumanRounding(false);

                }
                //TODO: Add Point to graphList as items.add(new Point(rate.getDate(), rate.getRate));
            }

            @Override
            public void onError(ErrorType type) {
                /*Toast.makeText(getContext(),
                        "Error",
                        Toast.LENGTH_SHORT)
                        .show();*/
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
            Log.i("RetrofitRequest", Formatter.dateToRestrofit(date));
            RetrofitClient.getInstance().sendRequestWithDate(rateCallback, Formatter.dateToRestrofit(date), currencyName);
        }
        //TODO: After all data received and added to list - show graph
    }
}
