package tinkoff.fintech.exchange.pojo;

import java.util.Date;

public class ApiResponse {
    private String base;
    private Date date;
    private RateObject rates;

    public String getBase() {
        return base;
    }

    public RateObject getRates() {
        return rates;
    }

    public Date getDate() {
        return date;
    }
}