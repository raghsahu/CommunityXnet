package com.logical.communityapp.model_class.directory_listing_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class DirectoryListModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<DirectoryListModelData> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DirectoryListModelData> getData() {
        return data;
    }

    public void setData(List<DirectoryListModelData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
