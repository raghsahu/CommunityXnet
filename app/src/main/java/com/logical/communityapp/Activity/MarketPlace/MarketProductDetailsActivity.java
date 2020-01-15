package com.logical.communityapp.Activity.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.logical.communityapp.Activity.All_Chat.All_Chat_Activity;
import com.logical.communityapp.Activity.All_Chat.Chat_Activity;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityMarketProductDetailsBinding;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductDetailsModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MarketProductDetailsActivity extends AppCompatActivity {

     String Product_id;
    ActivityMarketProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_market_product_details);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_market_product_details);

        
        try {
            if (getIntent()!=null){
                Product_id=getIntent().getStringExtra("Product_id");
            }
            
        }catch (Exception e){
            
        }
        //check connectivity
        if (Conectivity.isConnected(this)){
            GetProductDetails();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }


        binding.llChatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MarketProductDetailsActivity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        
    }

    @SuppressLint("CheckResult")
    private void GetProductDetails() {
        final ProgressDialog progressDialog =new ProgressDialog(MarketProductDetailsActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetMarketProductDetails(Product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MarketPlaceProductDetailsModel>() {
                    @Override
                    public void onNext(MarketPlaceProductDetailsModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_product_details", response.getMsg());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            if (response.getResult().equalsIgnoreCase("true")){

                                //  Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
                            binding.setModel(response.getData());

                            if (!response.getData().getImage().isEmpty() && response.getData().getImage()!=null){
                                binding.productImage.setVisibility(View.VISIBLE);
                            }

                            }else {

                                Toast.makeText(MarketProductDetailsActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });


    }
}
