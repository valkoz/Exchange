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

public class HistoryViewModel extends AndroidViewModel {

    MutableLiveData<List<ExchangeOperation>> operations;
    MutableLiveData<HistoryQuery> lastQuery;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        operations = new MutableLiveData<List<ExchangeOperation>>();
        lastQuery = new MutableLiveData<HistoryQuery>();
        getFromBd();
    }

    public LiveData<List<ExchangeOperation>> getOperations() {
        return operations;
    }

    public LiveData<HistoryQuery> getLastQuery() {
        return lastQuery;
    }

    private void getFromBd() {
        lastQuery.postValue(AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get());

        if (lastQuery.getValue() != null) {
            Log.i(getClass().getCanonicalName(),
                    lastQuery.getValue().getCurrencies().toString()
                            + lastQuery.getValue().getFromDate()
                            + lastQuery.getValue().getToDate());
            operations.postValue(getByDateAndName(lastQuery.getValue().getCurrencies(),
                    lastQuery.getValue().getFromDate().getTime(),
                    lastQuery.getValue().getToDate().getTime()));
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
        lastQuery.postValue(AppDatabase.getAppDatabase(getApplication()).historyQueryDao().get());
    }
}
