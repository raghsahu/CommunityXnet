package com.logical.communityapp.Activity.Login_Signup;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.logical.communityapp.R;
import com.logical.communityapp.databinding.ActivitySignupWaitingApprovalBinding;

public class SignupWaitingApproval extends AppCompatActivity {

    ActivitySignupWaitingApprovalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_signup_waiting_approval);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_signup_waiting_approval);

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
        //super.onBackPressed();

        Intent intent=new Intent(SignupWaitingApproval.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
