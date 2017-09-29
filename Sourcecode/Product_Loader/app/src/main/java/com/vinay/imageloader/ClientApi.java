package com.vinay.imageloader;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ClientApi {

    //"https://api.bestbuy.com/v1/products/4807511/stores.json?postalCode=55423&apiKey=YourAPIKey"
    public static final String BASE_URL = "https://api.bestbuy.com/v1/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}