package tinkoff.fintech.exchange.network;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tinkoff.fintech.exchange.BuildConfig;
import tinkoff.fintech.exchange.enums.ErrorType;
import tinkoff.fintech.exchange.pojo.ApiResponse;
import tinkoff.fintech.exchange.pojo.RateObject;

public class RetrofitClient {

    private static final int TOO_MANY_REQUESTS = 429;
    private static RetrofitClient instance = null;
    private final String BASE_URL = "http://api.fixer.io/";

    private Api service;

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }

        return instance;
    }

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .build();

        Gson gson = new GsonBuilder().registerTypeAdapter(RateObject.class, new RatesDeserializer()).setDateFormat("yyyy-MM-dd").create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(Api.class);
    }

    public Api getService() {
        return this.service;
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    public static void sendRequest(final RateCallback callback, String from, String to) {
        Call<ApiResponse> responseCall = RetrofitClient.getInstance().getService().latest(from, to);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                RateObject rate = response.body().getRates();
                if (rate != null)
                    callback.onSuccess(rate);
                else
                    callback.onError(ErrorType.NULL_BODY);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                callback.onError(ErrorType.REQUEST_ERROR);
            }
        });
    }

    public void sendRequestWithDate(final RateWithDateCallback callback, String date, String to) {
        Call<ApiResponse> responseCall = RetrofitClient.getInstance().getService().byDate(date, to);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response != null) {
                    if (response.code() == TOO_MANY_REQUESTS) {
                        callback.onError(ErrorType.TOO_MANY_REQUESTS);
                    }
                    if (response.body() != null) {
                        RateObject rate = response.body().getRates();
                        Date date = response.body().getDate();
                        if (rate != null)
                            callback.onSuccess(rate, date);
                        else
                            callback.onError(ErrorType.NULL_BODY);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                callback.onError(ErrorType.REQUEST_ERROR);
            }
        });
    }

}
