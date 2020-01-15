package com.logical.communityapp.Activity.Community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.adapter.community_adapter.CommunityPostAdapter;
import com.logical.communityapp.adapter.MyPagerAdapter;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.community_model.CommunityDeailsModel;
import com.logical.communityapp.model_class.community_model.Post_CommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_posts;
import static com.logical.communityapp.Api_Url.Base_Url.join_community;

public class CommunityDetailsActivity extends AppCompatActivity {

    RecyclerView recycler_post_view;
    Button btn_commu_join_now,community_news_post;
    Context context;
    private  ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.comm1, R.drawable.commu2,R.drawable.commu3};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    CommunityPostAdapter mAdapter;
    ArrayList<Post_CommunityModel>post_communityModel=new ArrayList<>();
    private String Community_Id,Join_status;
    TextView tv_commu_name,tv_nodetails,tv_total_member,tv_description,tv_address,tv_lga,tv_city;
    NestedScrollView ll_commu_details;
    ImageView iv_back,iv_comm_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_details);
        context=CommunityDetailsActivity.this;

        InitAllView();

        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(CommunityDetailsActivity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void InitAllView() {
        recycler_post_view=findViewById(R.id.recycler_post_view);
        btn_commu_join_now=findViewById(R.id.btn_commu_join_now);
        community_news_post=findViewById(R.id.community_news_post);
        tv_commu_name=findViewById(R.id.tv_commu_name);
        tv_nodetails=findViewById(R.id.tv_nodetails);
        ll_commu_details=findViewById(R.id.ll_commu_details);
        tv_total_member=findViewById(R.id.tv_total_member);
        iv_comm_pro=findViewById(R.id.iv_comm_pro);
        tv_description=findViewById(R.id.tv_description);
        tv_address=findViewById(R.id.tv_address);
        tv_lga=findViewById(R.id.tv_lga);
        tv_city=findViewById(R.id.tv_city);
        iv_back=findViewById(R.id.iv_back);

        try {
            if (getIntent()!=null){
                Community_Id=getIntent().getStringExtra("Community_Id");
                Join_status=getIntent().getStringExtra("Join_status");

                if (Join_status.equalsIgnoreCase("0")){
                    btn_commu_join_now.setText("Join");
                }else {
                   btn_commu_join_now.setText("Joined");
                }
         }

        }catch (Exception e){

        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //**********post community news get by api**
        if (Conectivity.isConnected(CommunityDetailsActivity.this)){
           // Log.e("Community_Id", Community_Id);
            getCommunityDetails();
            getCommunityPost();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


        btn_commu_join_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(context, CommunityJoin_NowActivity.class);
               // startActivity(intent);

                if (btn_commu_join_now.getText().equals("Joined")){
                    Toast.makeText(CommunityDetailsActivity.this, "Already joined", Toast.LENGTH_SHORT).show();
                }else {
                    if (Conectivity.isConnected(CommunityDetailsActivity.this)){
                        Joined_Community();

                    }
                }

            }
        });

        community_news_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Join_status.equalsIgnoreCase("0")){
                    Toast.makeText(CommunityDetailsActivity.this, "Please join this community", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context, CommunityNewPostActivity.class);
                    intent.putExtra("Community_Id", Community_Id);
                    intent.putExtra("Join_status", Join_status);
                    startActivity(intent);
                }

            }
        });

    }

    private void Joined_Community() {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+join_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .addBodyParameter("community_id",Community_Id )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("rrrrrr", jsonObject.toString());
                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Join_status = jsonObject.getString("join_status");

                                Toast.makeText(context, "Joined successfully", Toast.LENGTH_SHORT).show();
                                btn_commu_join_now.setText("Joined");
                                // Intent intent = new Intent(context, CommunityJoin_NowActivity.class);
                                // context.startActivity(intent);
                               // Join_status=
                            } else {

                                String message = jsonObject.getString("msg");
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }



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

    private void getCommunityDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<CommunityDeailsModel> call = apiInterface.GetCommunityDetails(Community_Id);

        call.enqueue(new Callback<CommunityDeailsModel>() {
            @Override
            public void onResponse(Call<CommunityDeailsModel> call, Response<CommunityDeailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("error_Dr_login",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                           // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();

                            tv_commu_name.setText(response.body().getData().getName());
                            tv_total_member.setText(response.body().getData().getTotalMember().toString()+" member");
                            tv_description.setText(getString(R.string.about)+" "+response.body().getData().getDescription());
                            tv_address.setText(getString(R.string.address)+" "+response.body().getData().getAddress());
                            tv_lga.setText(getString(R.string.lga)+" "+response.body().getData().getLga().toString());
                            tv_city.setText(getString(R.string.city)+" "+response.body().getData().getCity());


                            if (!response.body().getData().getIcon().equalsIgnoreCase("")){
                                Picasso.with(context).load(community_icon_BaseUrl+response.body().getData().getIcon())
                                        .fit()
                                        .centerCrop()
                                        .placeholder(R.drawable.star)
                                        .error(R.drawable.star)
                                        .into(iv_comm_pro);
                            }


                        }else {
                            Toast.makeText(CommunityDetailsActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            ll_commu_details.setVisibility(View.GONE);
                            community_news_post.setVisibility(View.GONE);
                            tv_nodetails.setVisibility(View.VISIBLE);
                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_login", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommunityDeailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_Dr_login",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getCommunityPost() {
        final ProgressDialog progressDialog = new ProgressDialog(CommunityDetailsActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_posts;
        AndroidNetworking.post(url)
                .addBodyParameter("community_id", Community_Id)
                // .addBodyParameter("password",password )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            Log.e("commu_post", jsonObject.toString());

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    Post_CommunityModel allCommunityModel=new Post_CommunityModel();

                                    allCommunityModel.id = dataObject.getString("id");
                                    allCommunityModel.member_id = dataObject.getString("member_id");
                                    allCommunityModel.community_id = dataObject.getString("community_id");
                                    allCommunityModel.date = dataObject.getString("date");
                                    allCommunityModel.description = dataObject.getString("description");
                                    allCommunityModel.image = dataObject.getString("image");
                                    allCommunityModel.first_name = dataObject.getString("first_name");
                                    allCommunityModel.last_name = dataObject.getString("last_name");
                                    allCommunityModel.member_image = dataObject.getString("member_image");


                                    post_communityModel.add(0,allCommunityModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new CommunityPostAdapter(post_communityModel, CommunityDetailsActivity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(CommunityDetailsActivity.this);
                            recycler_post_view.setLayoutManager(mLayoutManger);
                            recycler_post_view.setLayoutManager(new LinearLayoutManager(CommunityDetailsActivity.this, RecyclerView.VERTICAL, false));
                            recycler_post_view.setItemAnimator(new DefaultItemAnimator());

                            recycler_post_view.setAdapter(mAdapter);
                            recycler_post_view.setFocusable(false);
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
        super.onBackPressed();
    }
}
