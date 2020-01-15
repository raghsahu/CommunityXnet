package com.logical.communityapp.Activity.Profile_Related;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.adapter.community_adapter.MyJoinedCommunityAdapter;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_my_joined_community;

public class SeeListJoinedCommunity extends AppCompatActivity {

    ArrayList<AllCommunityModel> allCommunityModels=new ArrayList<>();
    RecyclerView recycler_mycommu;
    MyJoinedCommunityAdapter myJoinedCommunityAdapter;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_joined_community);

        recycler_mycommu=findViewById(R.id.recycler_mycommu);
        iv_back=findViewById(R.id.iv_back);

        if (Conectivity.isConnected(this)){
            getMyJoinedCommunity();
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getMyJoinedCommunity() {

        final ProgressDialog progressDialog = new ProgressDialog(SeeListJoinedCommunity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_my_joined_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(SeeListJoinedCommunity.this))
                // .addBodyParameter("password",password )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            Log.e("res_my", jsonObject.toString());
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
                                  //  allCommunityModel.lga = dataObject.getString("lga");
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
                                    // allCommunityModel.join_status = dataObject.getString("join_status");

                                    allCommunityModels.add(allCommunityModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }


                            myJoinedCommunityAdapter = new MyJoinedCommunityAdapter(allCommunityModels, SeeListJoinedCommunity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(SeeListJoinedCommunity.this);
                            recycler_mycommu.setLayoutManager(mLayoutManger);
                            recycler_mycommu.setLayoutManager(new GridLayoutManager(SeeListJoinedCommunity.this, 2));
                            recycler_mycommu.setItemAnimator(new DefaultItemAnimator());
                            recycler_mycommu.setAdapter(myJoinedCommunityAdapter);
                            recycler_mycommu.setFocusable(false);
                            myJoinedCommunityAdapter.notifyDataSetChanged();



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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
