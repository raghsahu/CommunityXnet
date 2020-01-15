package com.logical.communityapp.model_class.i_report_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 23/11/2019.
 */
public class I_reportModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<I_ReportModel_Details> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<I_ReportModel_Details> getData() {
        return data;
    }

    public void setData(List<I_ReportModel_Details> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}
