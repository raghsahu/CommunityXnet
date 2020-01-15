package com.logical.communityapp.model_class.maintance_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 10/12/2019.
 */
public class Main_category_model {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<Main_category_modelData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Main_category_modelData> getData() {
        return data;
    }

    public void setData(List<Main_category_modelData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
