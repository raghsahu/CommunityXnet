package com.logical.communityapp.Activity.MarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.market_place_adapter.MarketPlaceProductAdapter;
import com.logical.communityapp.databinding.ActivityMyProductBinding;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyProductActivity extends AppCompatActivity {
    ActivityMyProductBinding binding;
    MarketPlaceProductAdapter marketPlaceProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_product);
        //setContentView(R.layout.activity_my_product);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //get product
        if (Conectivity.isConnected(MyProductActivity.this)){
            GetMyProduct();//using rxjava with retrofit
        }else {
            Toast.makeText(MyProductActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    private void GetMyProduct() {
        final ProgressDialog progressDialog =new ProgressDialog(MyProductActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetMyMarketProduct(Shared_Preference.getUser_Id(MyProductActivity.this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MarketPlaceProductModel>() {
                    @Override
                    public void onNext(MarketPlaceProductModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_mr_product", response.getMsg());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            if (response.getResult().equalsIgnoreCase("true")){
                                //  Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
                                marketPlaceProductAdapter = new MarketPlaceProductAdapter(response.getData(), MyProductActivity.this,"MySelfPost");
                                binding.setMyAdapter(marketPlaceProductAdapter);//set databinding adapter
                                marketPlaceProductAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(MyProductActivity.this, "You have no products", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Conectivity.isConnected(MyProductActivity.this)){
            GetMyProduct();//using rxjava with retrofit
        }else {
            Toast.makeText(MyProductActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
