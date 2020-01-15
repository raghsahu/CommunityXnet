package com.logical.communityapp.model_class.maintance_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 10/12/2019.
 */
public class Main_currency_modelData {
    @SerializedName("code")
    @Expose
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
