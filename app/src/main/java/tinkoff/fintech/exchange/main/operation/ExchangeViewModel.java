package tinkoff.fintech.exchange.main.operation;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.pojo.ExchangePair;
import tinkoff.fintech.exchange.util.AppDatabase;

public class ExchangeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Currency>> currencies;

    private MutableLiveData<Currency> chosenCurrency;

    public ExchangeViewModel(@NonNull Application application) {
        super(application);
        currencies = new MutableLiveData<List<Currency>>();
        chosenCurrency = new MutableLiveData<>();
        currencies.postValue(getAllCurrenciesFromDb());
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencies;
    }

    private List<Currency> getAllCurrenciesFromDb() {
        return AppDatabase.getAppDatabase(getApplication()).currencyDao().getAll();
    }

    private List<Currency> getAllCurrenciesFromDbExcept(String name) {
        return AppDatabase.getAppDatabase(getApplication()).currencyDao().getCurrenciesExcept(chosenCurrency.getValue().getName());
    }

    public void updateCurrencies() {
        List<Currency> list = new ArrayList<>();
        if (chosenCurrency.getValue() != null) {
            list = getAllCurrenciesFromDbExcept(chosenCurrency.getValue().getName());
        }
        else {
            list = getAllCurrenciesFromDb();
        }
        currencies.postValue(list);
    }


    public void updateOnCheckChanged(Integer tag, boolean isChecked) {
        Currency element = currencies.getValue().get(tag);
        element.setFavourite(isChecked);
        updateSingleCurrency(element);
        updateCurrencies();
    }

    public void updateOnLongClick(Integer tag) {
        if (chosenCurrency.getValue() != null)
            currencies.getValue().add(chosenCurrency.getValue());
        Currency element = currencies.getValue().get(tag);
        chosenCurrency.setValue(element);
        updateCurrencies();
    }

    private void updateSingleCurrency(Currency element) {
       AppDatabase.getAppDatabase(getApplication()).currencyDao().update(element);
    }

    public ExchangePair getCurrencyPair(String text) {
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
        return new ExchangePair(fromCurrency, toCurrency);
    }

}
