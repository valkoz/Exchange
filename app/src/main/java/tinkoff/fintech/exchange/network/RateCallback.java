package tinkoff.fintech.exchange.network;

import tinkoff.fintech.exchange.enums.ErrorType;
import tinkoff.fintech.exchange.pojo.RateObject;

public interface RateCallback {

    void onSuccess(RateObject rate);
    void onError(ErrorType type);
}
