package com.vinay.imageloader;

//Coded By @@@Vinay C
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Client_Interface {

    @GET("products/{sku}/stores.json")
    Call<Store_Response> getResponse(@Path("sku") String productcd,@Query("postalCode") String postalCode,@Query("apiKey") String apiKey);

    @GET("products(search={name})?format=json&show=sku&apiKey=w6xsra7wfxbfmk8d2jykg7m8")
    Call<Products> getResponse(@Path("name") String name);
}
