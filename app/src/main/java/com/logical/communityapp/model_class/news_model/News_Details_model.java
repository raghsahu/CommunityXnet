package com.logical.communityapp.model_class.news_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 20/12/2019.
 */
public class News_Details_model {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private NewsDetailsData data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public NewsDetailsData getData() {
        return data;
    }

    public void setData(NewsDetailsData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

