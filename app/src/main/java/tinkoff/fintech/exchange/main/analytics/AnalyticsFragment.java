package tinkoff.fintech.exchange.main.analytics;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Paint;
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

import com.tinkoff.fintech.news.simpleplot.GraphView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.enums.Period;
import tinkoff.fintech.exchange.util.CalendarIterator;

public class AnalyticsFragment extends ListFragment {

    private RadioGroup radioGroup;
    private GraphView graph;
    private ArrayAdapter<String> adapter;
    private AnalyticsViewModel model;

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
        initViews();

        model = ViewModelProviders.of(this).get(AnalyticsViewModel.class);
        model.getCurrencyNames().observe(this, strings ->  { adapter.addAll(strings); });
        model.getGraphPoints().observe(this, graphPoints -> { graph.setItems(graphPoints); });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String currencyName = ((TextView) v).getText().toString();
        graph.setGraphName(currencyName);
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
        model.loadGraph(dates, currencyName);
    }

    private void initViews() {
        graph = getActivity().findViewById(R.id.graphView);
        radioGroup = getActivity().findViewById(R.id.radioGroup);
        initGraphStyle();
        adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        setListAdapter(adapter);
    }

    private void initGraphStyle() {
        Paint plotPaint = new Paint();
        plotPaint.setColor(getResources().getColor(R.color.colorPrimary));
        plotPaint.setStrokeWidth(5);
        graph.setPlotPaint(plotPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.colorPrimary));
        textPaint.setTextSize(24f);
        textPaint.setFakeBoldText(true);
        graph.setTextPaint(textPaint);

        Paint headingPaint = new Paint();
        headingPaint.setColor(getResources().getColor(R.color.colorPrimary));
        headingPaint.setTextSize(30f);
        graph.setHeadingPaint(headingPaint);

        graph.setGridStep(10);
        graph.setLabelStep(5);
        graph.setScaleYLabelDateFormat(true);
    }

}
