package com.carelynk.rest;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 19-Mar-16.
 */
public class ApiFactory {


    public static String API_BASE_URL = "http://wcf.carelynk.com/api/";

    private static Retrofit retrofit = null;

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface provideInterface() {
        return getClient().create(ApiInterface.class);
    }

}
