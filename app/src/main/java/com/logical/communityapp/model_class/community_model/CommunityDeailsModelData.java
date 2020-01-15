package com.logical.communityapp.model_class.community_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logical.communityapp.model_class.BannerData;

import java.util.List;

public class CommunityDeailsModelData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("community_id")
    @Expose
    private String communityId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lga")
    @Expose
    private String lga;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("do_you_live_in_com")
    @Expose
    private String doYouLiveInCom;
    @SerializedName("are_you_property_owner")
    @Expose
    private String areYouPropertyOwner;
    @SerializedName("do_you_work_in_com")
    @Expose
    private String doYouWorkInCom;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("states_name")
    @Expose
    private String statesName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("banners")
    @Expose
    private List<BannerData> banners = null;
    @SerializedName("total_member")
    @Expose
    private Integer totalMember;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getDoYouLiveInCom() {
        return doYouLiveInCom;
    }

    public void setDoYouLiveInCom(String doYouLiveInCom) {
        this.doYouLiveInCom = doYouLiveInCom;
    }

    public String getAreYouPropertyOwner() {
        return areYouPropertyOwner;
    }

    public void setAreYouPropertyOwner(String areYouPropertyOwner) {
        this.areYouPropertyOwner = areYouPropertyOwner;
    }

    public String getDoYouWorkInCom() {
        return doYouWorkInCom;
    }

    public void setDoYouWorkInCom(String doYouWorkInCom) {
        this.doYouWorkInCom = doYouWorkInCom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatesName() {
        return statesName;
    }

    public void setStatesName(String statesName) {
        this.statesName = statesName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<BannerData> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerData> banners) {
        this.banners = banners;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }
}
