package com.logical.communityapp.model_class.directory_listing_model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class BusinessProfileModelData extends BaseObservable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("directory_name")
    @Expose
    private String directoryName;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

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

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
