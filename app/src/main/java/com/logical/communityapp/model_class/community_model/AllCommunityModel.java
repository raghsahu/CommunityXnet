package com.logical.communityapp.model_class.community_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AllCommunityModel  implements Serializable, Parcelable {

  public   String id;
    public String member_id;
    public String community_id;
    public String name;
    public String icon;
    public String description;
    public String address;
    public String lga;
    public String city_id;
    public  String state_id;
    public String country_id;
    public String do_you_live_in_com;
    public String are_you_property_owner;
    public String do_you_work_in_com;
    public String status;
    public String created_date;
    public String country;
    public String states_name;
    public String city;
    public String join_status;
    public String default_status;

  public AllCommunityModel(Parcel in) {
    id = in.readString();
    member_id = in.readString();
    community_id = in.readString();
    name = in.readString();
    icon = in.readString();
    description = in.readString();
    address = in.readString();
    lga = in.readString();
    city_id = in.readString();
    state_id = in.readString();
    country_id = in.readString();
    do_you_live_in_com = in.readString();
    are_you_property_owner = in.readString();
    do_you_work_in_com = in.readString();
    status = in.readString();
    created_date = in.readString();
    country = in.readString();
    states_name = in.readString();
    city = in.readString();
    join_status = in.readString();

  }

  public AllCommunityModel() {

  }

  @Override
  public int describeContents() {
    return 0;
  }


  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(member_id);
    dest.writeString(community_id);
    dest.writeString(name);
    dest.writeString(icon);
    dest.writeString(description);
    dest.writeString(address);
    dest.writeString(lga);
    dest.writeString(city_id);
    dest.writeString(state_id);
    dest.writeString(country_id);
    dest.writeString(do_you_live_in_com);
    dest.writeString(are_you_property_owner);
    dest.writeString(do_you_work_in_com);
    dest.writeString(status);
    dest.writeString(created_date);
    dest.writeString(country);
    dest.writeString(states_name);
    dest.writeString(city);
    dest.writeString(join_status);
  }

  public static final Creator<AllCommunityModel> CREATOR = new Creator<AllCommunityModel>() {
    @Override
    public AllCommunityModel createFromParcel(Parcel in) {
      return new AllCommunityModel(in);
    }

    @Override
    public AllCommunityModel[] newArray(int size) {
      return new AllCommunityModel[size];
    }
  };
}
