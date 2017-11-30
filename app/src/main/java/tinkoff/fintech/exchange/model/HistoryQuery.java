package tinkoff.fintech.exchange.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import tinkoff.fintech.exchange.util.ArrayToStringConverter;
import tinkoff.fintech.exchange.util.TimestampConverter;

@Entity
public class HistoryQuery {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @TypeConverters({TimestampConverter.class})
    private Date fromDate;

    @TypeConverters({TimestampConverter.class})
    private Date toDate;

    @TypeConverters({ArrayToStringConverter.class})
    private List<String> currencies;

    public HistoryQuery() {}

    public HistoryQuery(Date fromDate, Date toDate, List<String> choosenCurrencies) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        currencies = choosenCurrencies;
    }

    public long getId() {
        return id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }
}
