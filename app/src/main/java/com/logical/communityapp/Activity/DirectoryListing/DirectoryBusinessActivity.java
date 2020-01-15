package com.logical.communityapp.Activity.DirectoryListing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.model_class.directory_listing_model.BusinessDeleteModel;
import com.logical.communityapp.model_class.directory_listing_model.BusinessProfileModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityDirectoryBusinessBinding;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DirectoryBusinessActivity extends AppCompatActivity {
    ActivityDirectoryBusinessBinding binding;
    private String Businessid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding= DataBindingUtil.setContentView(this,R.layout.activity_directory_business);
       // setContentView(R.layout.activity_directory_business);

        if (Conectivity.isConnected(DirectoryBusinessActivity.this)){
            getBusinessProfile(); //rx with retrofit
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnArtisanJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DirectoryBusinessActivity.this, JoinArtisanActivity.class);
                intent.putExtra("Update", "Join");
                startActivity(intent);
                finish();
            }
        });

        binding.btnArtisanDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Conectivity.isConnected(DirectoryBusinessActivity.this)){
                    DeleteMyBusinessAccount();
                }else {
                    Toast.makeText(DirectoryBusinessActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


        binding.btnArtisanUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DirectoryBusinessActivity.this, JoinArtisanActivity.class);
                intent.putExtra("Update", "Update");
                intent.putExtra("Businessid", Businessid);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("CheckResult")
    private void DeleteMyBusinessAccount() {
        final ProgressDialog progressDialog =new ProgressDialog(DirectoryBusinessActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.DeleteBusinessArtisans(Businessid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BusinessDeleteModel>() {
                    @Override
                    public void onNext(BusinessDeleteModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("deleteArtisans", response.getMsg());

                            String msg = response.getMsg();
                           Toast.makeText(DirectoryBusinessActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent=new Intent(DirectoryBusinessActivity.this, DirectoryBusinessActivity.class);
                            startActivity(intent);
                            finish();


                        }catch (Exception e){
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void getBusinessProfile() {
        final ProgressDialog progressDialog =
                new ProgressDialog(DirectoryBusinessActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetBusinessArtisans(Shared_Preference.getUser_Id(DirectoryBusinessActivity.this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BusinessProfileModel>() {
                    @Override
                    public void onNext(BusinessProfileModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_joinArtisans", response.getMsg());

                            if (response.getResult().equalsIgnoreCase("false")){
                                Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                                binding.llAccount.setVisibility(View.GONE);
                            }


                            binding.setModel(response.getData());
                            Businessid=response.getData().getId();


                        }catch (Exception e){
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }
}
