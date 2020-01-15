package com.logical.communityapp.model_class.i_report_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 05/12/2019.
 */
public class I_reportDetailsModelData {

    @SerializedName("report_id")
    @Expose
    private String reportId;
    @SerializedName("texts")
    @Expose
    private String texts;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("type")
    @Expose
    private String type;
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
    private List<CommentData> comments = null;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<CommentData> getComments() {
        return comments;
    }

    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }
}
