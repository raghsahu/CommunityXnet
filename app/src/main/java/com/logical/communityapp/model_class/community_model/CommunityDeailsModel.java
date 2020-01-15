package com.logical.communityapp.model_class.community_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityDeailsModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CommunityDeailsModelData data;

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

    public CommunityDeailsModelData getData() {
        return data;
    }

    public void setData(CommunityDeailsModelData data) {
        this.data = data;
    }
}
