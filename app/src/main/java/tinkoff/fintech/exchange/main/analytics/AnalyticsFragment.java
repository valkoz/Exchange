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
import java.util.concurrent.ExecutionException;

import tinkoff.fintech.exchange.daoTasks.GetAllCurrencies;
import tinkoff.fintech.exchange.enums.CurrencyName;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.enums.Period;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.network.ErrorType;
import tinkoff.fintech.exchange.network.RateObject;
import tinkoff.fintech.exchange.network.RateWithDateCallback;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;
import tinkoff.fintech.exchange.util.Formatter;

public class AnalyticsFragment extends ListFragment {

    RadioGroup radioGroup;
    GraphView graph;
    RateWithDateCallback rateCallback;
    List<GraphPoint> items = new ArrayList<>();
    int responseCount = 7;

    public AnalyticsFragment() {}


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

        graph = getActivity().findViewById(R.id.graphView);
        radioGroup = getActivity().findViewById(R.id.radioGroup);
        initGraphStyle();

        List<String> coins = new ArrayList<>();
        List<Currency> currencies;
        try {
            currencies = new GetAllCurrencies(AppDatabase.getAppDatabase(getContext())).execute().get();
            Collections.sort(currencies);
            for (Currency c: currencies) {
                coins.add(c.getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        coins.remove(CurrencyName.EUR.name());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, coins);
        setListAdapter(adapter);


        rateCallback = new RateWithDateCallback() {
            @Override
            public void onSuccess(RateObject rate, Date date) {
                items.add(new GraphPoint(date, rate.getRate()));
                if (items.size() == responseCount) {
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
            }

            @Override
            public void onError(ErrorType type) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String currencyName = ((TextView) v).getText().toString();
        List<Date> dates = new ArrayList<>();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.analytics_filter_week:
                dates = CalendarIterator.getLast(Period.WEEK);
                responseCount = dates.size();
                break;
            case R.id.analytics_filter_two_weeks:
                dates = CalendarIterator.getLast(Period.TWO_WEEKS);
                responseCount = dates.size();
                break;
            case R.id.analytics_filter_month:
                dates = CalendarIterator.getLast(Period.MONTH);
                responseCount = dates.size();
                break;
            default:
                break;
        }

        for (Date date : dates) {
            items.clear();
            RetrofitClient.getInstance()
                    .sendRequestWithDate(rateCallback, Formatter.dateToString(date), currencyName);
        }
    }

    private void initGraphStyle() {
        Paint p1 = new Paint();
        p1.setColor(getResources().getColor(R.color.colorPrimary));
        p1.setStrokeWidth(5);
        graph.setPlotPaint(p1);

        Paint p2 = new Paint();
        p2.setColor(getResources().getColor(R.color.colorPrimary));
        p2.setTextSize(24f);
        p2.setFakeBoldText(true);

        graph.setTextPaint(p2);
        graph.setGridStep(10);
        graph.setLabelStep(5);
        graph.setScaleYLabelDateFormat(true);
    }
}
