package com.logical.communityapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.Community.SeeAllCommunityActivity;
import com.logical.communityapp.adapter.community_adapter.AllCommunityAdapter;
import com.logical.communityapp.adapter.community_adapter.CommunityLatestPostAdapter;
import com.logical.communityapp.adapter.LatestPostAdapter;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.model_class.community_model.CommunityLatestPostModel;
import com.logical.communityapp.model_class.LatestPostModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_communities;
import static com.logical.communityapp.Api_Url.Base_Url.get_my_joined_community;
import static com.logical.communityapp.Api_Url.Base_Url.get_news;

public class Home_fragment extends Fragment {

    RecyclerView recycler_latest_post,recycler_home_commu;
    TextView tv_see_all;
    LatestPostAdapter mAdapter;
    ArrayList<LatestPostModel>latestPostModels=new ArrayList<>();
    CommunityLatestPostAdapter communityLatestPostAdapter;

    AllCommunityAdapter allCommunityAdapter;
    ArrayList<AllCommunityModel>allCommunityModels=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        getActivity().setTitle("Home");
        tv_see_all=root.findViewById(R.id.tv_see_all);
        recycler_latest_post=root.findViewById(R.id.recycler_latest_post);
        recycler_home_commu=root.findViewById(R.id.recycler_home_commu);

        tv_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SeeAllCommunityActivity.class);
                startActivity(intent);
            }
        });


        if(Conectivity.isConnected(getActivity())){
           // GetLatestPost();//admin post news
           // GetCommunityList();//all community list
            getMyJoinedCommunity("MyJoin");//my join community
           getCommunityLatestPost();//my join community latest post

        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }


        return root;

    }

    private void getCommunityLatestPost() {

        final ProgressDialog progressDialog =
                new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<CommunityLatestPostModel> call = apiInterface.GetLatestPost(Shared_Preference.getUser_Id(getActivity()));

        call.enqueue(new Callback<CommunityLatestPostModel>() {
            @Override
            public void onResponse(Call<CommunityLatestPostModel> call, Response<CommunityLatestPostModel> response) {

                try{
                    if (response!=null){
                        Log.e("res_i_report",""+response.body().getResult());

                        if (response.body().getResult().equalsIgnoreCase("true")){

                            communityLatestPostAdapter = new CommunityLatestPostAdapter(response.body().getData(), getActivity());
                            recycler_latest_post.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recycler_latest_post.setAdapter(communityLatestPostAdapter);
                            communityLatestPostAdapter.notifyDataSetChanged();
                            recycler_latest_post.setFocusable(true);

                        }else {
                            Toast.makeText(getActivity(), ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_i_report", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommunityLatestPostModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_i_report1",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getMyJoinedCommunity(String myJoin) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_my_joined_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(getActivity()))
                // .addBodyParameter("password",password )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Log.e("my_join_responce", jsonObject.toString());
                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    AllCommunityModel allCommunityModel=new AllCommunityModel();

                                    allCommunityModel.id = dataObject.getString("id");
                                    allCommunityModel.member_id = dataObject.getString("member_id");
                                    allCommunityModel.community_id = dataObject.getString("community_id");
                                    allCommunityModel.name = dataObject.getString("name");
                                    allCommunityModel.icon = dataObject.getString("icon");
                                    allCommunityModel.description = dataObject.getString("description");
                                    allCommunityModel.address = dataObject.getString("address");
                                   // allCommunityModel.lga = dataObject.getString("lga");
                                    allCommunityModel.city_id = dataObject.getString("city_id");
                                    allCommunityModel.state_id = dataObject.getString("state_id");
                                    allCommunityModel.country_id = dataObject.getString("country_id");
                                    allCommunityModel.do_you_live_in_com = dataObject.getString("do_you_live_in_com");
                                    allCommunityModel.are_you_property_owner = dataObject.getString("are_you_property_owner");
                                    allCommunityModel.do_you_work_in_com = dataObject.getString("do_you_work_in_com");
                                    allCommunityModel.status = dataObject.getString("status");
                                    allCommunityModel.created_date = dataObject.getString("created_date");
                                    allCommunityModel.country = dataObject.getString("country");
                                    allCommunityModel.states_name = dataObject.getString("states_name");
                                    allCommunityModel.city = dataObject.getString("city");
                                    allCommunityModel.default_status = dataObject.getString("default_status");
                                    allCommunityModel.join_status = dataObject.getString("join_status");

                                    allCommunityModels.add(allCommunityModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            allCommunityAdapter = new AllCommunityAdapter(allCommunityModels, getActivity(),myJoin);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recycler_home_commu.setLayoutManager(mLayoutManger);
                            recycler_home_commu.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                            recycler_home_commu.setItemAnimator(new DefaultItemAnimator());
                            recycler_home_commu.setAdapter(allCommunityAdapter);
                            allCommunityAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                        Log.e("error_my_join", anError.toString());


                    }
                });



    }

    private void GetCommunityList(String allCommu) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_communities;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(getActivity()))
               // .addBodyParameter("password",password )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                           // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                               // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    AllCommunityModel allCommunityModel=new AllCommunityModel();

                                    allCommunityModel.id = dataObject.getString("id");
                                    allCommunityModel.member_id = dataObject.getString("member_id");
                                    allCommunityModel.community_id = dataObject.getString("community_id");
                                    allCommunityModel.name = dataObject.getString("name");
                                    allCommunityModel.icon = dataObject.getString("icon");
                                    allCommunityModel.description = dataObject.getString("description");
                                    allCommunityModel.address = dataObject.getString("address");
                                    allCommunityModel.lga = dataObject.getString("lga");
                                    allCommunityModel.city_id = dataObject.getString("city_id");
                                    allCommunityModel.state_id = dataObject.getString("state_id");
                                    allCommunityModel.country_id = dataObject.getString("country_id");
                                    allCommunityModel.do_you_live_in_com = dataObject.getString("do_you_live_in_com");
                                    allCommunityModel.are_you_property_owner = dataObject.getString("are_you_property_owner");
                                    allCommunityModel.do_you_work_in_com = dataObject.getString("do_you_work_in_com");
                                    allCommunityModel.status = dataObject.getString("status");
                                    allCommunityModel.created_date = dataObject.getString("created_date");
                                    allCommunityModel.country = dataObject.getString("country");
                                    allCommunityModel.states_name = dataObject.getString("states_name");
                                    allCommunityModel.city = dataObject.getString("city");
                                    allCommunityModel.default_status = dataObject.getString("default_status");
                                    allCommunityModel.join_status = dataObject.getString("join_status");

                                    allCommunityModels.add(allCommunityModel);

                                }


                            } else {

                               // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            allCommunityAdapter = new AllCommunityAdapter(allCommunityModels, getActivity(), allCommu);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recycler_home_commu.setLayoutManager(mLayoutManger);
                            recycler_home_commu.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                            recycler_home_commu.setItemAnimator(new DefaultItemAnimator());
                            recycler_home_commu.setAdapter(allCommunityAdapter);
                            allCommunityAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();


                    }
                });



    }

    private void GetLatestPost() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_news;
        AndroidNetworking.get(url)
                //.addBodyParameter("email",email_id )
                // .addBodyParameter("password",password )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {


                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    LatestPostModel latestPostModel=new LatestPostModel();

                                    latestPostModel.id = dataObject.getString("id");
                                    latestPostModel.headline = dataObject.getString("headline");
                                    latestPostModel.description = dataObject.getString("description");
                                    latestPostModel.date = dataObject.getString("date");


                                    latestPostModels.add(latestPostModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new LatestPostAdapter(latestPostModels, getActivity());
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recycler_latest_post.setLayoutManager(mLayoutManger);
                            recycler_latest_post.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recycler_latest_post.setItemAnimator(new DefaultItemAnimator());

                            recycler_latest_post.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();


                    }
                });


    }

}
