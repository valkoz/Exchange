package tinkoff.fintech.exchange.daoTasks;

import android.os.AsyncTask;

import java.util.List;

import tinkoff.fintech.exchange.util.AppDatabase;
import tinkoff.fintech.exchange.model.ExchangeOperation;


public class GetAllOperationHistory extends AsyncTask<Void, Void, List<ExchangeOperation>> {

    private AppDatabase db;

    public GetAllOperationHistory(AppDatabase appDatabase) {
        db = appDatabase;
    }

    @Override
    protected List<ExchangeOperation> doInBackground(Void... voids) {
        return db.exchangeOperationDao().getAll();
    }
}