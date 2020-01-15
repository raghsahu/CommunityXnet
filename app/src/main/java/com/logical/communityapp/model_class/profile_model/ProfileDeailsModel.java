package com.logical.communityapp.model_class.profile_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logical.communityapp.model_class.profile_model.ProfileData;

public class ProfileDeailsModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ProfileData data;

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

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }
}
