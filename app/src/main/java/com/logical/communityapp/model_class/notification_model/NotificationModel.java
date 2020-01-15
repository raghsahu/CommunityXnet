package com.logical.communityapp.model_class.notification_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {
    @SerializedName("data")
    @Expose
    private List<NotificationModelData> data = null;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<NotificationModelData> getData() {
        return data;
    }

    public void setData(List<NotificationModelData> data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
