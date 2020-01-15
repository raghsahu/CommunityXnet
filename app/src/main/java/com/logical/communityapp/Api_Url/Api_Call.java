package com.logical.communityapp.Api_Url;

import com.logical.communityapp.model_class.community_model.CommunityLatestPostModel;
import com.logical.communityapp.model_class.directory_listing_model.BusinessDeleteModel;
import com.logical.communityapp.model_class.directory_listing_model.BusinessProfileModel;
import com.logical.communityapp.model_class.directory_listing_model.JoinArtisansModel;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestModel;
import com.logical.communityapp.model_class.city_state_country.CityDetailsModel;
import com.logical.communityapp.model_class.community_model.CommunityDeailsModel;
import com.logical.communityapp.model_class.city_state_country.CountryDeailsModel;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductDetailsModel;
import com.logical.communityapp.model_class.notification_model.NotificationModel;
import com.logical.communityapp.model_class.profile_model.ProfileDeailsModel;
import com.logical.communityapp.model_class.city_state_country.StateDeailsModel;
import com.logical.communityapp.model_class.i_report_model.I_reportDetailsModel;
import com.logical.communityapp.model_class.i_report_model.I_reportModel;
import com.logical.communityapp.model_class.i_report_model.I_report_post_model;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductModel;
import com.logical.communityapp.model_class.support_model.SupportSubmitModel;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.logical.communityapp.Api_Url.Base_Url.add_directory_listing;
import static com.logical.communityapp.Api_Url.Base_Url.delete_directory_listing;
import static com.logical.communityapp.Api_Url.Base_Url.edit_directory_listing;
import static com.logical.communityapp.Api_Url.Base_Url.get_category_wise_maintance;
import static com.logical.communityapp.Api_Url.Base_Url.get_directory_listing;
import static com.logical.communityapp.Api_Url.Base_Url.get_market_place_my_product;
import static com.logical.communityapp.Api_Url.Base_Url.get_market_place_product;
import static com.logical.communityapp.Api_Url.Base_Url.get_market_place_product_detail;
import static com.logical.communityapp.Api_Url.Base_Url.get_notifications;
import static com.logical.communityapp.Api_Url.Base_Url.post_support_request;
import static com.logical.communityapp.Api_Url.Base_Url.update_directory_listing;

public interface Api_Call {

    @FormUrlEncoded
    @POST(Base_Url.get_community_detail)
    Call<CommunityDeailsModel> GetCommunityDetails(
            @Field("id") String community_id) ;

    @FormUrlEncoded
    @POST(Base_Url.get_member_profile)
    Call<ProfileDeailsModel> GetProfileImage(
            @Field("id") String user_id);


    @GET(Base_Url.get_countries)
    Call<CountryDeailsModel> GetAllCountry();


    @FormUrlEncoded
    @POST(Base_Url.get_states)
    Call<StateDeailsModel> GetAllState(
            @Field("country_id") String countryId);


    @FormUrlEncoded
    @POST(Base_Url.get_cities)
    Call<CityDetailsModel> GetAllCity(
            @Field("state_id") String stateId);

    @FormUrlEncoded
    @POST(Base_Url.get_i_report)
    Call<I_reportModel> GetI_Report(
            @Field("member_id") String user_id1);

    @Multipart
    @POST(Base_Url.post_i_report)
    Call<I_report_post_model> post_irepot(
            @Part("member_id") RequestBody user_id1,
            @Part("texts") RequestBody et_comment1,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part img_part,
            @Part("video") RequestBody video);


    @FormUrlEncoded
    @POST(Base_Url.i_report_detail)
    Call<I_reportDetailsModel> GetI_Report_Details(
            @Field("member_id") String user_id,
            @Field("i_report_id") String i_report_id);


//    @GET(Base_Url.get_maintenance_request)
//    Observable<MaintanceRequestModel> GetMaintanceRequest();

    @FormUrlEncoded
    @POST(Base_Url.get_maintenance_request)
    Call<MaintanceRequestModel> GetMaintanceRequest(
            @Field("by_category")  String category_ID,
            @Field("member_id")  String member_id);


    @FormUrlEncoded
    @POST(Base_Url.get_latest_posts)
    Call<CommunityLatestPostModel> GetLatestPost(
            @Field("member_id") String user_id);


    @POST(add_directory_listing)
    @FormUrlEncoded
    Observable<JoinArtisansModel>  JoinArtisans(
            @Field("member_id")  String user_id,
            @Field("category_id") String category_id,
            @Field("directory_name") String directoryName,
            @Field("email")  String directoryEmail,
            @Field("phone_number") String directoryNumber);

    @GET(Base_Url.get_maintenace_category)
    Observable<Main_category_model>  GetCategoryArtisans();

    @POST(get_directory_listing)
    @FormUrlEncoded
    Observable<BusinessProfileModel>  GetBusinessArtisans(
            @Field("member_id")   String user_id);


    @POST(delete_directory_listing)
    @FormUrlEncoded
    Observable<BusinessDeleteModel>  DeleteBusinessArtisans(
            @Field("id") String id);


    @POST(update_directory_listing)
    @FormUrlEncoded
    Observable<JoinArtisansModel>  UpdateArtisans(
            @Field("id")  String user_id,
            @Field("category_id") String category_id,
            @Field("directory_name") String directoryName,
            @Field("email")  String directoryEmail,
            @Field("phone_number") String directoryNumber);


    @POST(edit_directory_listing)
    @FormUrlEncoded
    Observable<BusinessProfileModel> GetBusinessEdit(
            @Field("id") String businessid);

    @POST(get_category_wise_maintance)
    @FormUrlEncoded
    Observable<MaintanceRequestModel> GetDirectoryList(
            @Field("member_id") String user_id);

    @POST(get_market_place_product)
    @FormUrlEncoded
    Observable<MarketPlaceProductModel> GetMarketProduct(
            @Field("by_category") String category_ID);

    @POST(get_market_place_my_product)
    @FormUrlEncoded
    Observable<MarketPlaceProductModel> GetMyMarketProduct(
            @Field("member_id") String user_id);

    @POST(get_market_place_product_detail)
    @FormUrlEncoded
    Observable<MarketPlaceProductDetailsModel> GetMarketProductDetails(
            @Field("id") String product_id);

    @POST(get_notifications)
    @FormUrlEncoded
    Observable<NotificationModel> GetNotification(
            @Field("member_id") String user_id);


    @POST(post_support_request)
    @FormUrlEncoded
    Observable<SupportSubmitModel> SendSupport(
            @Field("member_id") String title_query,
            @Field("query_title")  String query_desc,
            @Field("query_description")  String user_id);


//    @Multipart
//    @POST(Base_Url.post_i_report)
//    Call<I_report_post_model> post_irepot(
//            @Part("member_id") String user_id,
//            @Part("texts") String et_comment,
//            @Part("type") String type,
//            @Part MultipartBody.Part img_part,
//            @Part("video") String s);


}
