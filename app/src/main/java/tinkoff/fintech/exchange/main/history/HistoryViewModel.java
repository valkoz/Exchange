package tinkoff.fintech.exchange.main.history;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.util.AppDatabase;

public class HistoryViewModel extends AndroidViewModel {

    MutableLiveData<List<ExchangeOperation>> operations;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        operations = new MutableLiveData<List<ExchangeOperation>>();
        operations.postValue(getFromBd());
    }

    public LiveData<List<ExchangeOperation>> getCurrencies() {
        return operations;
    }

    private List<ExchangeOperation> getFromBd() {
        List<ExchangeOperation> list = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getAll();
        return list;
    }

    public void getByDateAndName(ArrayList<String> names, long fromDate, long toDate) {
        List<ExchangeOperation> i;
        if (names.isEmpty())
            i = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getByDate(fromDate, toDate);
        else
            i = AppDatabase.getAppDatabase(getApplication()).exchangeOperationDao().getByDateAndName(names, fromDate, toDate);
        operations.postValue(i);
    }
}
