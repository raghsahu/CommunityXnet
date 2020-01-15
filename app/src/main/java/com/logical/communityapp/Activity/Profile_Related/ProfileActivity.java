package com.logical.communityapp.Activity.Profile_Related;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.profile_model.ProfileDeailsModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

public class ProfileActivity extends AppCompatActivity {

    ImageView iv_back,profile_image;
    TextView tv_name,tv_city,tv_mobile,tv_email,tv_location,tv_gender,
            tv_occupation,tv_joined_commu,tv_self_commu,tv_edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv_back=findViewById(R.id.iv_back);
        tv_city=findViewById(R.id.tv_city);
        tv_name=findViewById(R.id.tv_name);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_location=findViewById(R.id.tv_location);
        tv_email=findViewById(R.id.tv_email);
        tv_gender=findViewById(R.id.tv_gender);
        tv_occupation=findViewById(R.id.tv_occupation);
        tv_joined_commu=findViewById(R.id.tv_joined_commu);
        tv_self_commu=findViewById(R.id.tv_self_commu);
        tv_edit=findViewById(R.id.tv_edit);
        profile_image=findViewById(R.id.profile_image);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Conectivity.isConnected(ProfileActivity.this)){

            getProfileDetails();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
               // finish();

            }
        });

        tv_self_commu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, SeeListMyselfCommunty.class);
                startActivity(intent);
            }
        });

        tv_joined_commu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, SeeListJoinedCommunity.class);
                startActivity(intent);
            }
        });

    }

    private void getProfileDetails() {
        Log.e("user_idlog", Shared_Preference.getUser_Id(ProfileActivity.this));

        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<ProfileDeailsModel> call = apiInterface.GetProfileImage(Shared_Preference.getUser_Id(ProfileActivity.this));

        call.enqueue(new Callback<ProfileDeailsModel>() {
            @Override
            public void onResponse(Call<ProfileDeailsModel> call, Response<ProfileDeailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("er_Dr_update",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            tv_name.setText(response.body().getData().getFirstName());
                            if (response.body().getData().getCity()!=null){
                                tv_city.setText(response.body().getData().getCity());
                                tv_location.setText(response.body().getData().getCity());
                            }

                            tv_mobile.setText(response.body().getData().getPhoneNumber());
                            tv_email.setText(response.body().getData().getEmail());
                            tv_gender.setText(response.body().getData().getGender());
                            tv_occupation.setText(response.body().getData().getOccupation());
                            tv_joined_commu.setText(response.body().getData().getMemberTotalJoinedCommunity().toString());
                            tv_self_commu.setText(response.body().getData().getTotalSelfCommunity().toString());

                            Glide.with(ProfileActivity.this)
                                    .load(member_pic_BaseUrl+response.body().getData().getImage())
                                    // .apply(options)
                                    .placeholder(R.drawable.man)
                                    .into(profile_image);

                        }else {
                            Toast.makeText(ProfileActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_l", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileDeailsModel> call, Throwable t) {
                progressDialog.dismiss();
               // Log.e("error_Dr_login",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        Intent intent=new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
