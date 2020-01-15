package com.logical.communityapp.model_class.directory_listing_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class BusinessProfileModel {


    @SerializedName("data")
    @Expose
    private BusinessProfileModelData data;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public BusinessProfileModelData getData() {
        return data;
    }

    public void setData(BusinessProfileModelData data) {
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
