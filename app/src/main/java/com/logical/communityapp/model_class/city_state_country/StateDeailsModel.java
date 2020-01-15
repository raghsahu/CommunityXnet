package com.logical.communityapp.model_class.city_state_country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateDeailsModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<StateData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<StateData> getData() {
        return data;
    }

    public void setData(List<StateData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
