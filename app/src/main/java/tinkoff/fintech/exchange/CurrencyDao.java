package tinkoff.fintech.exchange;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency")
    List<Currency> getAll();

    @Insert
    void insertAll(Currency... currencies);

    @Update
    void update(Currency... currencies);

    @Delete
    void delete(Currency currency);


}
