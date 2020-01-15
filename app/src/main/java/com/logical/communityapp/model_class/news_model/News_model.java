package com.logical.communityapp.model_class.news_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 13/12/2019.
 */
public class News_model {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<NewsModelData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<NewsModelData> getData() {
        return data;
    }

    public void setData(List<NewsModelData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
