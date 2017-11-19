package tinkoff.fintech.exchange.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tinkoff.fintech.exchange.R;
import tinkoff.fintech.exchange.network.AnalyticsResponse;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.views.GraphView;

@SuppressWarnings("ConstantConditions")
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

        sendRequest("2000-01-03");

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
                switch (checkedId){
                    case R.id.analytics_filter_week :
                        break;
                    case R.id.analytics_filter_two_weeks :
                        break;
                    case R.id.analytics_filter_month :
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void sendRequest(String date) {
        Call<AnalyticsResponse> responseCall = RetrofitClient.getInstance().getService().byDate(date);
        responseCall.enqueue(new retrofit2.Callback<AnalyticsResponse>() {
            @Override
            public void onResponse(Call<AnalyticsResponse> call, Response<AnalyticsResponse> response) {
                Log.i("Retrofit2", response.body().getRates().toString());
            }

            @Override
            public void onFailure(Call<AnalyticsResponse> call, Throwable t) {
                Log.i("Retrofit2", t.getMessage());
            }
        });
    }
}
