package tinkoff.fintech.exchange.main.operation;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

public class ExchangeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Currency>> currencies;

    public ExchangeViewModel(@NonNull Application application) {
        super(application);
        currencies = new MutableLiveData<List<Currency>>();
        currencies.postValue(getFromBd());
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencies;
    }

    private List<Currency> getFromBd() {
        List<Currency> list = AppDatabase.getAppDatabase(getApplication()).currencyDao().getAll();
        Collections.sort(list);
        return list;
    }


}
