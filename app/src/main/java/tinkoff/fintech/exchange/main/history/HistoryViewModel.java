package tinkoff.fintech.exchange.main.history;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.model.HistoryQuery;
import tinkoff.fintech.exchange.util.AppDatabase;
//TODO Refactor(simplify logic)
public class HistoryViewModel extends AndroidViewModel {

    MutableLiveData<List<ExchangeOperation>> operations;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        operations = new MutableLiveData<List<ExchangeOperation>>();
        getFromBd();
    }

    public LiveData<List<ExchangeOperation>> getCurrencies() {
        return operations;
    }

    private void getFromBd() {
        HistoryQuery hq = AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get();

        if (hq != null) {
            Log.i(getClass().getCanonicalName(), hq.getCurrencies().toString() + hq.getFromDate() + hq.getToDate());
            operations.postValue(getByDateAndName(hq.getCurrencies(), hq.getFromDate().getTime(), hq.getToDate().getTime()));
            return;
        }
        Log.i(getClass().getCanonicalName(), "is NULL");
        List<ExchangeOperation> list = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getAll();
        operations.postValue(list);
    }

    public List<ExchangeOperation> getByDateAndName(List<String> names, long fromDate, long toDate) {
        if (names.isEmpty())
            return AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getByDate(fromDate, toDate);
        else
            return AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getByDateAndName(names, fromDate, toDate);
    }

    public void get(ArrayList<String> names, long fromDate, long toDate) {
        operations.postValue(getByDateAndName(names, fromDate, toDate));
    }
}
