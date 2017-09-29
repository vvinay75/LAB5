package com.vinay.imageloader;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class Store_Response {
    @SerializedName("ispuEligible")
    private String ispuEligible;
    @SerializedName("stores")
    private ArrayList<Store> stores;

    public String getIspuEligible() {
        return ispuEligible;
    }

    public void setIspuEligible(String ispuEligible) {
        this.ispuEligible = ispuEligible;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }
}
