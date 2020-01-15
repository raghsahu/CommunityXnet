package com.logical.communityapp.model_class.i_report_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 02/12/2019.
 */
public class I_report_post_model {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private I_report_post_Data data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public I_report_post_Data getData() {
        return data;
    }

    public void setData(I_report_post_Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
