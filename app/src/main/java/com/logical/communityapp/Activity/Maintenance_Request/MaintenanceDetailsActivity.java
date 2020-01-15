package com.logical.communityapp.Activity.Maintenance_Request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.All_Chat.Chat_Activity;
import com.logical.communityapp.Activity.MarketPlace.MarketProductDetailsActivity;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestData;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityMaintenanceDetailsBinding;

import static com.logical.communityapp.Api_Url.Base_Url.maintenance_request_images_URL;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

public class MaintenanceDetailsActivity extends AppCompatActivity {
    ActivityMaintenanceDetailsBinding activityMaintenanceDetailsBinding;
    Context context;
    String Post_id;
    MaintanceRequestData maintanceRequestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMaintenanceDetailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_maintenance_details);
       // setContentView(R.layout.activity_maintenance_details);
        //initialize must be important
        AndroidNetworking.initialize(getApplicationContext());
        context=MaintenanceDetailsActivity.this;

        try {
            if (getIntent()!=null){
                Post_id=getIntent().getStringExtra("Post_id");
                maintanceRequestData=(MaintanceRequestData)getIntent().getSerializableExtra("MaintanceRequestData");
            }
        }catch (Exception e){

        }

        //backpress call
        activityMaintenanceDetailsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        activityMaintenanceDetailsBinding.llChatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MaintenanceDetailsActivity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });


        //set all data in views
        activityMaintenanceDetailsBinding.tvPName.setText(maintanceRequestData.getMemberName());
        activityMaintenanceDetailsBinding.tvPEmail.setText(maintanceRequestData.getMemberEmail());
        activityMaintenanceDetailsBinding.tvDate.setText(maintanceRequestData.getDate());
        activityMaintenanceDetailsBinding.tvDesc.setText(maintanceRequestData.getDescription());
        activityMaintenanceDetailsBinding.tvTitle.setText(maintanceRequestData.getTitleOfRequest());
        activityMaintenanceDetailsBinding.tvCategory.setText(maintanceRequestData.getCategoryName());
        activityMaintenanceDetailsBinding.tvBudget.setText(maintanceRequestData.getCurrency()+" "+maintanceRequestData.getBudget());

        Log.e("post_img_maint", maintanceRequestData.getImage());

        if (maintanceRequestData.getMemberImage()!=null){
            Glide.with(context)
                    .load(member_pic_BaseUrl+maintanceRequestData.getMemberImage())
                    // .apply(options)
                    .placeholder(R.drawable.man)
                    .into(activityMaintenanceDetailsBinding.imgPerson);
        }

        if (maintanceRequestData.getImage()!=null && !maintanceRequestData.getImage().isEmpty()){
            activityMaintenanceDetailsBinding.postImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(maintenance_request_images_URL+maintanceRequestData.getImage())
                    // .apply(options)
                    .placeholder(R.drawable.star)
                    .into(activityMaintenanceDetailsBinding.postImage);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
