package tinkoff.fintech.exchange.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tinkoff.fintech.exchange.model.ExchangeOperation;

@Dao
public interface ExchangeOperationDao {

    @Query("SELECT * FROM ExchangeOperation")
    List<ExchangeOperation> getAll();

    @Query("SELECT * FROM ExchangeOperation WHERE `from` IN(:names) and createdDateTime > :fromDate and createdDateTime < :toDate " +
            "UNION " +
            "SELECT * FROM ExchangeOperation WHERE `to` IN(:names) and createdDateTime > :fromDate and createdDateTime < :toDate ")
    List<ExchangeOperation> getByDateAndName(List<String> names, long fromDate, long toDate);

    @Query("SELECT * FROM ExchangeOperation WHERE createdDateTime > :fromDate and createdDateTime < :toDate " +
            "UNION " +
            "SELECT * FROM ExchangeOperation WHERE createdDateTime > :fromDate and createdDateTime < :toDate ")
    List<ExchangeOperation> getByDate(long fromDate, long toDate);

    @Query("SELECT `from` FROM exchangeoperation GROUP BY `from` UNION SELECT `to` FROM exchangeoperation GROUP BY `to`")
    List<String> getExistingCurrencies();

    @Insert
    void insertAll(ExchangeOperation... exchangeHistories);

    @Update
    void update(ExchangeOperation... exchangeHistories);

    @Delete
    void delete(ExchangeOperation exchangeHistory);
}
