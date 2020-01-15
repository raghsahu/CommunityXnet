package com.logical.communityapp.model_class.market_place_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 01/01/2020.
 */
public class MarketPlaceProductModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<MarketPlaceProductData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MarketPlaceProductData> getData() {
        return data;
    }

    public void setData(List<MarketPlaceProductData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
