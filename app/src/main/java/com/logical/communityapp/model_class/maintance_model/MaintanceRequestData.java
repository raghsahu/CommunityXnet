package com.logical.communityapp.model_class.maintance_model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logical.communityapp.Api_Url.Base_Url;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 09/12/2019.
 */
public class MaintanceRequestData extends BaseObservable implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title_of_request")
    @Expose
    private String titleOfRequest;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("member_name")
    @Expose
    private String memberName;
    @SerializedName("member_image")
    @Expose
    private String memberImage;
    @SerializedName("member_email")
    @Expose
    private String memberEmail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleOfRequest() {
        return titleOfRequest;
    }

    public void setTitleOfRequest(String titleOfRequest) {
        this.titleOfRequest = titleOfRequest;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getPostUrl() {
        // The URL will usually come from a model (i.e Profile)
        return Base_Url.maintenance_request_images_URL+image;
    }

    @BindingAdapter("postImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                // .placeholder()
                .apply(new RequestOptions())
                .into(view);
    }
}
