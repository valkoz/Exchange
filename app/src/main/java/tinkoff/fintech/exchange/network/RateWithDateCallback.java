package tinkoff.fintech.exchange.network;

import java.util.Date;

import tinkoff.fintech.exchange.enums.ErrorType;
import tinkoff.fintech.exchange.pojo.RateObject;


public interface RateWithDateCallback {

    void onSuccess(RateObject rate, Date date);
    void onError(ErrorType type);
}
