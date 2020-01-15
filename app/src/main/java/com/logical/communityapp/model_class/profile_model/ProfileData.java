package com.logical.communityapp.model_class.profile_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("occupation")
    @Expose
    private String occupation;
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
    @SerializedName("join_date")
    @Expose
    private String joinDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("email_status")
    @Expose
    private String emailStatus;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("email_varification_code")
    @Expose
    private String emailVarificationCode;
    @SerializedName("pass_reset_code")
    @Expose
    private String passResetCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("states_name")
    @Expose
    private String statesName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("member_total_joined_community")
    @Expose
    private Integer memberTotalJoinedCommunity;
    @SerializedName("total_self_community")
    @Expose
    private Integer totalSelfCommunity;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getEmailVarificationCode() {
        return emailVarificationCode;
    }

    public void setEmailVarificationCode(String emailVarificationCode) {
        this.emailVarificationCode = emailVarificationCode;
    }

    public String getPassResetCode() {
        return passResetCode;
    }

    public void setPassResetCode(String passResetCode) {
        this.passResetCode = passResetCode;
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

    public Integer getMemberTotalJoinedCommunity() {
        return memberTotalJoinedCommunity;
    }

    public void setMemberTotalJoinedCommunity(Integer memberTotalJoinedCommunity) {
        this.memberTotalJoinedCommunity = memberTotalJoinedCommunity;
    }

    public Integer getTotalSelfCommunity() {
        return totalSelfCommunity;
    }

    public void setTotalSelfCommunity(Integer totalSelfCommunity) {
        this.totalSelfCommunity = totalSelfCommunity;
    }

}
