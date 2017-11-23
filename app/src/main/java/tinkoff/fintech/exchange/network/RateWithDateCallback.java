package tinkoff.fintech.exchange.network;

import java.util.Date;

/**
 * Created by valentin on 23.11.17.
 */

public interface RateWithDateCallback {

    void onSuccess(RateObject rate, Date date);
    void onError(ErrorType type);
}
