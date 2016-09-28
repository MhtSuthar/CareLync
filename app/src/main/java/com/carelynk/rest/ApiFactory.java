package com.carelynk.rest;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 19-Mar-16.
 */
public class ApiFactory {

    //users?order=desc&site=stackoverflow
    static String API_BASE_URL="http://api.androidhive.info";

    private static Retrofit provideRestAdapter(){
        return new Retrofit.Builder().baseUrl(API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
    }

   /* public static StackExchangeInterface stackExchange(){
        return provideRestAdapter().create(StackExchangeInterface.class);
    }*/

}
