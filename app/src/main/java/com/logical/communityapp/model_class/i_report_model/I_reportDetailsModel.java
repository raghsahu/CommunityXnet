package com.logical.communityapp.model_class.i_report_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 05/12/2019.
 */
public class I_reportDetailsModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private I_reportDetailsModelData data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public I_reportDetailsModelData getData() {
        return data;
    }

    public void setData(I_reportDetailsModelData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
