package tinkoff.fintech.exchange.main.history;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.pojo.CheckableCurrency;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;

public class FilterViewModel extends AndroidViewModel {

    private Calendar calendar;
    private MutableLiveData<Date> startDate;
    private MutableLiveData<Date> endDate;
    private MutableLiveData<List<CheckableCurrency>> currencies;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        calendar = Calendar.getInstance();
        setDates();
        setCurrencies();
    }

    private void setCurrencies() {
        currencies = new MutableLiveData<List<CheckableCurrency>>();
        List<String> coins = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getExistingCurrencies();
        HistoryQuery hq = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();

        List<CheckableCurrency> currencyList = new ArrayList<CheckableCurrency>();
        if (hq != null) {
            for (String coinName: coins) {
                if (hq.getCurrencies().contains(coinName)) {
                    currencyList.add(new CheckableCurrency(coinName, true));
                } else {
                    currencyList.add(new CheckableCurrency(coinName, false));
                }
            }
        } else {
            for (String coinName: coins) {
                currencyList.add(new CheckableCurrency(coinName, true));
            }
        }
        currencies.postValue(currencyList);
    }

    private void setDates() {
        startDate = new MutableLiveData<Date>();
        endDate = new MutableLiveData<Date>();
        HistoryQuery i = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();
        if (i != null) {
            startDate.postValue(i.getFromDate());
            endDate.postValue(i.getToDate());
        } else {
            updateDates(Calendar.DAY_OF_YEAR);
        }
    }

    public LiveData<Date> getStartDate() {
        return startDate;
    }

    public LiveData<Date> getEndDate() {
        return endDate;
    }

    public void setStartDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);

        startDate.postValue(calendar.getTime());
    }

    public void setEndDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        endDate.postValue(calendar.getTime());
    }

    public void updateDates(int period) {
        startDate.postValue(CalendarIterator.get(period));
        endDate.postValue(new Date());
    }

    public void updateCurrencies(Object tag, boolean b) {
        currencies.getValue().get((Integer) tag).setChecked(b);
    }

    public LiveData<List<CheckableCurrency>> getCurrencies() {
        return currencies;
    }

    public List<String> getChosenCurrencies() {
        List<String> list = new ArrayList<>();
        List<CheckableCurrency> currencyList = currencies.getValue();
        for (CheckableCurrency currency: currencyList) {
            if (currency.isChecked())
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

    public int getCurrentDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getCurrentYear() {
        return calendar.get(Calendar.YEAR);
    }
}
