package com.logical.communityapp.model_class.i_report_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 05/12/2019.
 */
public class CommentData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("report_id")
    @Expose
    private String reportId;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comm_time")
    @Expose
    private String commTime;
    @SerializedName("member_name")
    @Expose
    private String memberName;
    @SerializedName("member_image")
    @Expose
    private String memberImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommTime() {
        return commTime;
    }

    public void setCommTime(String commTime) {
        this.commTime = commTime;
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

}
