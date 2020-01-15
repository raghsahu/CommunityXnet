package com.logical.communityapp.model_class.city_state_country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sortname")
    @Expose
    private String sortname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phonecode")
    @Expose
    private String phonecode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

}
