package com.logical.communityapp.model_class.community_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 20/12/2019.
 */
public class CommunityLatestPostModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<CommunityLatestPostModelData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<CommunityLatestPostModelData> getData() {
        return data;
    }

    public void setData(List<CommunityLatestPostModelData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
