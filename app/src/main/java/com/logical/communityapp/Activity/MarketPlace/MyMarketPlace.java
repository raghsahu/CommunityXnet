package com.logical.communityapp.Activity.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.logical.communityapp.R;
import com.logical.communityapp.databinding.ActivityMyMarketPlace2Binding;

public class MyMarketPlace extends AppCompatActivity {
    ActivityMyMarketPlace2Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_my_market_place2);
        //setContentView(R.layout.activity_my_market_place2);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.cardUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyMarketPlace.this, UploadProductActivity.class);
                startActivity(intent);

            }
        });

        binding.cardMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyMarketPlace.this, MyProductActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
