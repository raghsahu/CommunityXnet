package com.logical.communityapp.model_class.city_state_country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryDeailsModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<CountryData> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<CountryData> getData() {
        return data;
    }

    public void setData(List<CountryData> data) {
        this.data = data;
    }
}
