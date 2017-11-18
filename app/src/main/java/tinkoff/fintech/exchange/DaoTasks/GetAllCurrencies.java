package tinkoff.fintech.exchange.DaoTasks;

import android.os.AsyncTask;

import java.util.List;

import tinkoff.fintech.exchange.AppDatabase;
import tinkoff.fintech.exchange.Currency;


public class GetAllCurrencies extends AsyncTask<Void, Void, List<Currency>> {

    private AppDatabase db;

    public GetAllCurrencies(AppDatabase appDatabase) {
        db = appDatabase;
    }

    @Override
    protected List<Currency> doInBackground(Void... voids) {
        List<Currency> nodes = db.currencyDao().getAll();
        return nodes;
    }

    @Override
    protected void onPostExecute(List<Currency> nodes) {
        super.onPostExecute(nodes);
    }
}
