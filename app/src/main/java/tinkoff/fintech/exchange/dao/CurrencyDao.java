package tinkoff.fintech.exchange.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tinkoff.fintech.exchange.model.Currency;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency")
    List<Currency> getAll();

    @Query("SELECT name FROM currency WHERE name != :name ORDER BY isFavourite DESC, useFrequency DESC")
    List<String> getCurrencyNamesExcept(String name);

    @Insert
    void insertAll(Currency... currencies);

    @Update
    void update(Currency... currencies);

    @Delete
    void delete(Currency currency);

    @Query("UPDATE currency SET useFrequency = useFrequency + 1 WHERE name IN(:currencies) ")
    void incrementUseFrequency(String... currencies);


}
