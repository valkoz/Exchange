package tinkoff.fintech.exchange.network;

import java.util.Map;

public class AnalyticsResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

}
