package tinkoff.fintech.exchange.main.operation;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.util.AppDatabase;

public class ExchangeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Currency>> currencies;

    private MutableLiveData<Currency> chosenCurrency;

    public ExchangeViewModel(@NonNull Application application) {
        super(application);
        currencies = new MutableLiveData<List<Currency>>();
        chosenCurrency = new MutableLiveData<>();
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


    public void updateOnCheckChanged(Integer tag, boolean isChecked) {
        Currency element = currencies.getValue().get(tag);
        element.setFavourite(isChecked);
        updateDb(element);
        Collections.sort(currencies.getValue());
    }

    public void updateOnLongClick(Integer tag) {
        if (chosenCurrency.getValue() != null)
            currencies.getValue().add(chosenCurrency.getValue());
        Currency element = currencies.getValue().get(tag);
        chosenCurrency.setValue(element);
        currencies.getValue().remove(element);
        Collections.sort(currencies.getValue());
    }

    private void updateDb(Currency element) {
        AsyncTask.execute(() -> AppDatabase.getAppDatabase(getApplication())
                .currencyDao()
                .update(element));
    }


    //TODO Выпилить этот бред
    public String getFromCurrency(String text) {
        String fromCurrency;
        String toCurrency;
        if (chosenCurrency.getValue() != null) {
            fromCurrency = chosenCurrency.getValue().getName();
            toCurrency = text;
        } else {
            fromCurrency = text;
            toCurrency = currencies.getValue().get(0).getName();
            if (fromCurrency.equals(toCurrency))
                toCurrency = currencies.getValue().get(1).getName();
        }
        return fromCurrency;
    }

    public String getToCurrency(String text) {
        String fromCurrency;
        String toCurrency;
        if (chosenCurrency.getValue() != null) {
            fromCurrency = chosenCurrency.getValue().getName();
            toCurrency = text;
        } else {
            fromCurrency = text;
            toCurrency = currencies.getValue().get(0).getName();
            if (fromCurrency.equals(toCurrency))
                toCurrency = currencies.getValue().get(1).getName();
        }
        return toCurrency;
    }
}
