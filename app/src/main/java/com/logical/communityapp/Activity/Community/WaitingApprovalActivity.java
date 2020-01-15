package com.logical.communityapp.Activity.Community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.logical.communityapp.R;
import com.logical.communityapp.databinding.ActivityWaitingApprovalBinding;


public class WaitingApprovalActivity extends AppCompatActivity {
    ActivityWaitingApprovalBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_waiting_approval);
       // setContentView(R.layout.activity_waiting_approval);



        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        Intent intent=new Intent(WaitingApprovalActivity.this, SeeAllCommunityActivity.class);
        startActivity(intent);
        finish();
    }
}
