package com.logical.communityapp.model_class.news_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 20/12/2019.
 */
public class NewsDetailsData {
    @SerializedName("news_id")
    @Expose
    private String newsId;
    @SerializedName("texts")
    @Expose
    private String texts;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("member_name")
    @Expose
    private String memberName;
    @SerializedName("member_image")
    @Expose
    private String memberImage;
    @SerializedName("member_email")
    @Expose
    private String memberEmail;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("total_comments")
    @Expose
    private String totalComments;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }


}
