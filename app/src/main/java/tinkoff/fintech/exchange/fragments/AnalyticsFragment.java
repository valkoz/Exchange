package tinkoff.fintech.exchange.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.views.GraphView;

public class AnalyticsFragment extends Fragment {

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
    }
}
