package com.vinay.imageloader;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class Products {
    @SerializedName("sku")
    ArrayList<String> sku;

    public ArrayList<String> getSku() {
        return sku;
    }

    public void setSku(ArrayList<String> sku) {
        this.sku = sku;
    }
}
