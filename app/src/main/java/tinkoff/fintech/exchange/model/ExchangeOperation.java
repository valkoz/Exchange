package tinkoff.fintech.exchange.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import tinkoff.fintech.exchange.util.TimestampConverter;

@Entity
public class ExchangeOperation {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @TypeConverters({TimestampConverter.class})
    private Date createdDateTime;
    private String from;
    private String to;
    private double fromValue;
    private double toValue;

    public ExchangeOperation() {}

    public ExchangeOperation(Date date, String from, String to, double fromValue, double toValue) {
        createdDateTime = date;
        this.from = from;
        this.to = to;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }
}
