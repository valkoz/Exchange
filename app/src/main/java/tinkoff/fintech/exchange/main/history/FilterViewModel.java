package tinkoff.fintech.exchange.main.history;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.util.CalendarIterator;

public class FilterViewModel extends AndroidViewModel {

    MutableLiveData<Date> mStartDate;
    MutableLiveData<Date> mEndDate;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        setDates();
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

}
