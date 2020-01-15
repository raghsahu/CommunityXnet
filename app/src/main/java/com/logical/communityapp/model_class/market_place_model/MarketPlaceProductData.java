package com.logical.communityapp.model_class.market_place_model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logical.communityapp.Api_Url.Base_Url;

/**
 * Created by Raghvendra Sahu on 01/01/2020.
 */
public class MarketPlaceProductData extends BaseObservable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("stock_status")
    @Expose
    private String stockStatus;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getPostUrl() {
        // The URL will usually come from a model (i.e Profile)
        return Base_Url.market_place_URL+image;
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
