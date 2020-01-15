package com.logical.communityapp.model_class.market_place_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketPlaceProductDetailsModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private MarketPlaceProductDetailsData data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public MarketPlaceProductDetailsData getData() {
        return data;
    }

    public void setData(MarketPlaceProductDetailsData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
