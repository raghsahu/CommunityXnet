package com.logical.communityapp.Activity.DirectoryListing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.model_class.directory_listing_model.BusinessProfileModel;
import com.logical.communityapp.model_class.directory_listing_model.JoinArtisansModel;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.Main_category_modelData;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityJoinArtisanBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.observers.DisposableObserver;

public class JoinArtisanActivity extends AppCompatActivity {

    ActivityJoinArtisanBinding binding;
    private String Category_ID="";
    HashMap<Integer, Main_category_modelData> CategoryHashMap=new HashMap<Integer, Main_category_modelData>();
    private String type,Businessid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_join_artisan);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_join_artisan);
        //initialize must be important
        AndroidNetworking.initialize(getApplicationContext());

        try {
            if (getIntent()!=null){
                type=getIntent().getStringExtra("Update");

                if (type.equalsIgnoreCase("Update")){
                    binding.btnArtisanJoin.setText("Update");
                    Businessid =getIntent().getStringExtra("Businessid");

                    if (Conectivity.isConnected(JoinArtisanActivity.this)){
                        GetEditProfile();//using rxjava with retrofit- own JAVA Object-JSON Parser
                    }else {
                        Toast.makeText(JoinArtisanActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    binding.btnArtisanJoin.setText("Join");
                }

            }

        }catch (Exception e){

        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get category  from server
        if (Conectivity.isConnected(JoinArtisanActivity.this)){
            GetPostCategoryRx();//using rxjava with retrofit- own JAVA Object-JSON Parser


        }else {
            Toast.makeText(JoinArtisanActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        //spinner category select
        binding.spinCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (CategoryHashMap.get(position).getName().equals(selectedItem))
                {
                    Category_ID=CategoryHashMap.get(position).getId();
                    Log.e("cat_id", Category_ID);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        //***********btn join_artisans
        binding.btnArtisanJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DirectoryName=binding.etDirectoryName.getText().toString();
                String DirectoryEmail=binding.etEmail.getText().toString();
                String DirectoryNumber=binding.etNumber.getText().toString();

                if (!DirectoryEmail.isEmpty() && !DirectoryName.isEmpty() && !DirectoryNumber.isEmpty()){

                    if (Conectivity.isConnected(JoinArtisanActivity.this)){
                        if (type.equalsIgnoreCase("Update")){
                            UpdateDirectoryList(DirectoryName,DirectoryEmail,DirectoryNumber);
                        }else {
                            JoinDirectoryList(DirectoryName,DirectoryEmail,DirectoryNumber);
                        }

                    }else {
                        Toast.makeText(JoinArtisanActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(JoinArtisanActivity.this, "Please enter all field", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressLint("CheckResult")
    private void GetEditProfile() {
        final ProgressDialog progressDialog =new ProgressDialog(JoinArtisanActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetBusinessEdit(Businessid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BusinessProfileModel>() {
                    @Override
                    public void onNext(BusinessProfileModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_joinArtisans", response.getMsg());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("CheckResult")
    private void UpdateDirectoryList(String directoryName, String directoryEmail, String directoryNumber) {
        final ProgressDialog progressDialog =
                new ProgressDialog(JoinArtisanActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.UpdateArtisans(Businessid,
                Category_ID,directoryName, directoryEmail,directoryNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JoinArtisansModel>() {
                    @Override
                    public void onNext(JoinArtisansModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_joinArtisans", response.getMsg());
                            Toast.makeText(JoinArtisanActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.getResult().equalsIgnoreCase("true")){
                                finish();
                                Intent intent=new Intent(JoinArtisanActivity.this, DirectoryBusinessActivity.class);
                                startActivity(intent);
                                finish();
                            }



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
    private void GetPostCategoryRx() {
        final ProgressDialog progressDialog =
                new ProgressDialog(JoinArtisanActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetCategoryArtisans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Main_category_model>() {
                    @Override
                    public void onNext(Main_category_model user) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cate_res1" , user.getResult());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getName());
                                CategoryHashMap.put(i, new Main_category_modelData(user.getData().get(i).getId(), user.getData().get(i).getName()));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(JoinArtisanActivity.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            binding.spinCate.setAdapter(dataAdapter);

                        }catch (Exception e){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        //Handle error
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void JoinDirectoryList(String directoryName, String directoryEmail, String directoryNumber) {
        final ProgressDialog progressDialog =
                new ProgressDialog(JoinArtisanActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.JoinArtisans(Shared_Preference.getUser_Id(JoinArtisanActivity.this),
                Category_ID,directoryName, directoryEmail,directoryNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JoinArtisansModel>() {
                    @Override
                    public void onNext(JoinArtisansModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_joinArtisans", response.getMsg());
                            Toast.makeText(JoinArtisanActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            finish();
                            Intent intent=new Intent(JoinArtisanActivity.this, DirectoryBusinessActivity.class);
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
