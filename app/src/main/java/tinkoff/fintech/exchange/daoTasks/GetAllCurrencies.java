package tinkoff.fintech.exchange.daoTasks;

import android.os.AsyncTask;

import java.util.List;

import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.model.Currency;


public class GetAllCurrencies extends AsyncTask<Void, Void, List<Currency>> {

    private AppDatabase db;

    public GetAllCurrencies(AppDatabase appDatabase) {
        db = appDatabase;
    }

    @Override
    protected List<Currency> doInBackground(Void... voids) {
        return db.currencyDao().getAll();
    }
}
