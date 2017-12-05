package tinkoff.fintech.exchange.main.history;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;

public class FilterViewModel extends AndroidViewModel {

    private MutableLiveData<Date> mStartDate;
    private MutableLiveData<Date> mEndDate;
    private MutableLiveData<List<Currency>> mCurrencies;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        setDates();
        setCurrencies();
    }

    private void setCurrencies() {
        mCurrencies = new MutableLiveData<List<Currency>>();
        List<String> coins = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getExistingCurrencies();
        HistoryQuery hq = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();

        List<Currency> currencies = new ArrayList<Currency>();
        if (hq != null) {
            for (String coinName: coins) {
                if (hq.getCurrencies().contains(coinName)) {
                    currencies.add(new Currency(coinName, true));
                } else {
                    currencies.add(new Currency(coinName, false));
                }
            }
        } else {
            for (String coinName: coins) {
                currencies.add(new Currency(coinName, true));
            }
        }
        mCurrencies.postValue(currencies);
    }

    private void setDates() {
        mStartDate = new MutableLiveData<Date>();
        mEndDate = new MutableLiveData<Date>();
        HistoryQuery i = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();
        if (i != null) {
            mStartDate.postValue(i.getFromDate());
            mEndDate.postValue(i.getToDate());
        } else {
            updateDates(Calendar.DAY_OF_YEAR);
        }
    }

    public LiveData<Date> getStartDate() {
        return mStartDate;
    }

    public LiveData<Date> getEndDate() {
        return mEndDate;
    }

    public void setStartDate(Calendar calendar) {
        mStartDate.postValue(calendar.getTime());
    }

    public void setEndDate(Calendar calendar) {
        mEndDate.postValue(calendar.getTime());
    }

    public void updateDates(int period) {
        mStartDate.postValue(CalendarIterator.get(period));
        mEndDate.postValue(new Date());
    }

    public void updateCurrencies(Object tag, boolean b) {
        mCurrencies.getValue().get((Integer) tag).setFavourite(b);
    }

    public LiveData<List<Currency>> getCurrencies() {
        return mCurrencies;
    }

    public List<String> getChosenCurrencies() {
        List<String> list = new ArrayList<>();
        List<Currency> currencies = mCurrencies.getValue();
        for (Currency currency: currencies) {
            if (currency.isFavourite())
                list.add(currency.getName());
        }
        return list;
    }

    public void updateHistoryQuery(Date start, Date end, List<String> chosenCurrencies) {
        HistoryQuery hq = new HistoryQuery(start, end, chosenCurrencies);
        AsyncTask.execute(() -> {
            AppDatabase.getAppDatabase(getApplication()).historyQueryDao().deleteAll();
            AppDatabase.getAppDatabase(getApplication()).historyQueryDao().insert(hq);
        });
    }
}
