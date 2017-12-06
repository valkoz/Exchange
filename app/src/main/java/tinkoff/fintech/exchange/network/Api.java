package tinkoff.fintech.exchange.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tinkoff.fintech.exchange.pojo.ApiResponse;

public interface Api {

    @GET("latest")
    Call<ApiResponse> latest(
            @Query("base") String from,
            @Query("symbols") String to
    );

    @GET("{dateToLongString}") //format: 2000-01-03
    Call<ApiResponse> byDate(
            @Path("dateToLongString") String date,
            @Query("symbols") String to
    );
}
