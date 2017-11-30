package tinkoff.fintech.exchange.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import tinkoff.fintech.exchange.model.HistoryQuery;

@Dao
public interface HistoryQueryDao {

    @Query("SELECT * FROM historyquery LIMIT 1")
    HistoryQuery get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryQuery... historyQueries);

    @Query("DELETE FROM historyquery")
    void deleteAll();

}
