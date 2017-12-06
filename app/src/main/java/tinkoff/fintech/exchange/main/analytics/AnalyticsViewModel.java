package tinkoff.fintech.exchange.main.analytics;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.tinkoff.fintech.news.simpleplot.GraphPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.enums.CurrencyName;
import tinkoff.fintech.exchange.enums.ErrorType;
import tinkoff.fintech.exchange.pojo.RateObject;
import tinkoff.fintech.exchange.network.RateWithDateCallback;
import tinkoff.fintech.exchange.network.RetrofitClient;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.Formatter;

public class AnalyticsViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> currencyNames;

    private MutableLiveData<List<GraphPoint>> graphPoints;


    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        currencyNames = new MutableLiveData<List<String>>();
        graphPoints = new MutableLiveData<List<GraphPoint>>();
        currencyNames.postValue(getFromDb());
    }

    private List<String> getFromDb() {
         return AppDatabase.getAppDatabase(getApplication()).currencyDao().getCurrencyNamesExcept(CurrencyName.EUR.name());
    }

    public LiveData<List<String>> getCurrencyNames() {
        return currencyNames;
    }

    public LiveData<List<GraphPoint>> getGraphPoints() {
        return graphPoints;
    }

    public void loadGraph(List<Date> dates, String currencyName) {

        List<GraphPoint> items = new ArrayList<>();

        RateWithDateCallback rateCallback = new RateWithDateCallback() {

            @Override
            public void onSuccess(RateObject rate, Date date) {
                items.add(new GraphPoint(date, rate.getRate()));
                if (items.size() == dates.size()) {
                    Collections.sort(items, new Comparator<GraphPoint>() {
                        @Override
                        public int compare(GraphPoint graphPoint, GraphPoint t1) {
                            return  Float.compare(graphPoint.getX(), t1.getX());
                        }
                    });
                    graphPoints.postValue(items);
                }
            }

            @Override
            public void onError(ErrorType type) { }
        };

        for (Date date : dates) {
            items.clear();
            RetrofitClient.getInstance()
                    .sendRequestWithDate(rateCallback, Formatter.dateToString(date), currencyName);
        }
    }
}
