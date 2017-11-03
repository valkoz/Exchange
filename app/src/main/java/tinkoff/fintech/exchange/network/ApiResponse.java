package tinkoff.fintech.exchange.network;

public class ApiResponse {
    private String base;
    private RateObject rates;

    public String getBase() {
        return base;
    }

    public RateObject getRates() {
        return rates;
    }
}