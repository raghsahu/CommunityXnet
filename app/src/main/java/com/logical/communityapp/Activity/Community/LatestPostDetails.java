package com.logical.communityapp.Activity.Community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.logical.communityapp.model_class.community_model.CommunityLatestPostModelData;
import com.logical.communityapp.R;
import com.logical.communityapp.databinding.ActivityLatestPostDetailsBinding;

import static com.logical.communityapp.Api_Url.Base_Url.CommuImage_BaseUrl;

public class LatestPostDetails extends AppCompatActivity {
    ActivityLatestPostDetailsBinding activityLatestPostDetailsBinding;
    CommunityLatestPostModelData communityLatestPostModelData;
    private String LatestId;

    public void onClickBack(View view) {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityLatestPostDetailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_latest_post_details);
        //setContentView(R.layout.activity_latest_post_details);


        try {// find getIntent value
            if (getIntent()!=null){
                LatestId=getIntent().getStringExtra("LatestId");
                communityLatestPostModelData=(CommunityLatestPostModelData)getIntent().getSerializableExtra("LatestDetails");

                activityLatestPostDetailsBinding.setModel(communityLatestPostModelData);
                activityLatestPostDetailsBinding.setImageUrl(CommuImage_BaseUrl+communityLatestPostModelData.getImage());
            }
        }catch (Exception e){
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
