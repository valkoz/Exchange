package tinkoff.fintech.exchange.network;

import java.util.Date;


public interface RateWithDateCallback {

    void onSuccess(RateObject rate, Date date);
    void onError(ErrorType type);
}
