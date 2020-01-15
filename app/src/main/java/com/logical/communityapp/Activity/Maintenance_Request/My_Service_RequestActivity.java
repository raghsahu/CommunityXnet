package com.logical.communityapp.Activity.Maintenance_Request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.logical.communityapp.adapter.MaintanceAdapter;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_my_maintenance_request;

public class My_Service_RequestActivity extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView recycler_maintance;
    MaintanceAdapter myRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__service__request);

        //initialize must be important
        AndroidNetworking.initialize(getApplicationContext());
        //find view id all views
        iv_back=findViewById(R.id.iv_back);
        recycler_maintance=findViewById(R.id.recycler_maintance);

        //all views click listener
        OnClickListener();

        //get my request
        if (Conectivity.isConnected(My_Service_RequestActivity.this)){
            GetMyRequest();//using rxjava with android networking- own JAVA Object-JSON Parser

        }else {
            Toast.makeText(My_Service_RequestActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void GetMyRequest() {
        final ProgressDialog progressDialog = new ProgressDialog(My_Service_RequestActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+get_my_maintenance_request)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(My_Service_RequestActivity.this))
                .build()
                .getObjectObservable(MaintanceRequestModel.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MaintanceRequestModel>() {
                    @Override
                    public void onCompleted() {
                        // do anything onComplete
                        progressDialog.dismiss();
                        Log.e("rx_comp" , "complete");
                    }
                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        progressDialog.dismiss();
                        Log.e("rx_error" , e.getMessage());
                    }
                    @Override
                    public void onNext(MaintanceRequestModel user) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_res1" , user.getResult());

                            if (user.getResult().equalsIgnoreCase("true")){

                                myRequestAdapter = new MaintanceAdapter(user.getData(), My_Service_RequestActivity.this,"MySelfPost");
                                recycler_maintance.setLayoutManager(new LinearLayoutManager(My_Service_RequestActivity.this, RecyclerView.VERTICAL, false));
                                recycler_maintance.setItemAnimator(new DefaultItemAnimator());
                                recycler_maintance.setAdapter(myRequestAdapter);
                                myRequestAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(My_Service_RequestActivity.this, ""+user.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });
    }

    private void OnClickListener() {
        //back press imageview
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
