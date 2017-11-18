package tinkoff.fintech.exchange.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tinkoff.fintech.exchange.BuildConfig;

public class RetrofitClient {

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

        Gson gson = new GsonBuilder().registerTypeAdapter(RateObject.class, new RatesDeserializer()).create();
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

}
