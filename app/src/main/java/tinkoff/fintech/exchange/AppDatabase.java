package tinkoff.fintech.exchange;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tinkoff.fintech.exchange.dao.CurrencyDao;
import tinkoff.fintech.exchange.dao.ExchangeOperationDao;
import tinkoff.fintech.exchange.model.Currency;
import tinkoff.fintech.exchange.model.ExchangeOperation;

@Database(entities = {Currency.class, ExchangeOperation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CurrencyDao currencyDao();

    public abstract ExchangeOperationDao exchangeOperationDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

