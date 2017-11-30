package tinkoff.fintech.exchange.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tinkoff.fintech.exchange.dao.CurrencyDao;
import tinkoff.fintech.exchange.dao.ExchangeOperationDao;
import tinkoff.fintech.exchange.dao.HistoryQueryDao;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.model.ExchangeOperation;
import tinkoff.fintech.exchange.model.HistoryQuery;

@Database(entities = {Currency.class, ExchangeOperation.class, HistoryQuery.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CurrencyDao currencyDao();

    public abstract ExchangeOperationDao exchangeOperationDao();

    public abstract HistoryQueryDao historyQueryDao();

    //TODO disable MainThreadQueries later
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "database-name")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

