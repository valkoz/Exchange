package tinkoff.fintech.exchange.network;

public interface RateCallback {

    void onSuccess(RateObject rate);
    void onError(ErrorType type);
}
