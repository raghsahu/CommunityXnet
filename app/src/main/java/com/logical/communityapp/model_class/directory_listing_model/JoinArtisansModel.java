package com.logical.communityapp.model_class.directory_listing_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class JoinArtisansModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private JoinArtisansModelData data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JoinArtisansModelData getData() {
        return data;
    }

    public void setData(JoinArtisansModelData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
