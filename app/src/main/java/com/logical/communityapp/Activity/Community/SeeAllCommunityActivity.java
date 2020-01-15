package com.logical.communityapp.Activity.Community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.adapter.community_adapter.AllCommunityAdapter;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_communities;

public class SeeAllCommunityActivity extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView recycler_all_community;
    AllCommunityAdapter mAdapter;
    TextView tv_create_community;
    List<AllCommunityModel> allCommunityModels=new ArrayList<>();
    private AllCommunityModel allCommunityModel;
    SearchView searchView;

    // ActivitySeeAllCommunityBiding activitySeeAllCommunityBiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // activitySeeAllCommunityBiding= DataBindingUtil.setContentView(this,R.layout.activity_see_all_community);

        setContentView(R.layout.activity_see_all_community);

        iv_back=findViewById(R.id.iv_back);
        recycler_all_community=findViewById(R.id.recycler_all_community);
        tv_create_community=findViewById(R.id.tv_create_community);
        searchView=findViewById(R.id.search_hint);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });


        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.clearFocus();
        searchView.setFocusable(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });

        tv_create_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent intent = new Intent(SeeAllCommunityActivity.this, CommunityNew_CreateActivity.class);
                intent.putParcelableArrayListExtra("AllCommunityModel", (ArrayList<? extends Parcelable>) allCommunityModels);
                startActivity(intent);



            }
        });

        if (Conectivity.isConnected(SeeAllCommunityActivity.this)){
            getAllCommunity("AllCommu");
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


    }

    //*********************************************************
    private void filter(String newText) {
        // ArrayList<SearchProductList> temp = new ArrayList();
        ArrayList <AllCommunityModel> Contact_li= new ArrayList<AllCommunityModel>();
        for (AllCommunityModel smodel : allCommunityModels) {
            //use .toLowerCase() for better matches
            if (smodel.name.toLowerCase().startsWith(newText.toLowerCase())) {
                Contact_li.add(smodel);
            }
        }
        //update recyclerview
        mAdapter.updateList(Contact_li);

    }

    private void getAllCommunity(String allCommu) {

        final ProgressDialog progressDialog = new ProgressDialog(SeeAllCommunityActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_communities;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(SeeAllCommunityActivity.this))
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

                                    allCommunityModel=new AllCommunityModel();

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
                                    allCommunityModel.join_status = dataObject.getString("join_status");


                                    allCommunityModels.add(allCommunityModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new AllCommunityAdapter(allCommunityModels, SeeAllCommunityActivity.this,allCommu);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(SeeAllCommunityActivity.this);
                            recycler_all_community.setLayoutManager(mLayoutManger);
                            recycler_all_community.setLayoutManager(new GridLayoutManager(SeeAllCommunityActivity.this, 2));
                            recycler_all_community.setItemAnimator(new DefaultItemAnimator());
                            recycler_all_community.setAdapter(mAdapter);
                            recycler_all_community.setFocusable(false);
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


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(SeeAllCommunityActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
