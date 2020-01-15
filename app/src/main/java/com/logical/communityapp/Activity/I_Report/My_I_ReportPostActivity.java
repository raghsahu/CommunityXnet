package com.logical.communityapp.Activity.I_Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.logical.communityapp.adapter.IReport_get_Adapter;
import com.logical.communityapp.model_class.i_report_model.I_reportModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_my_i_report;

public class My_I_ReportPostActivity extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView recycler_my_report;
    Context context;
     IReport_get_Adapter iReport_get_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__i__report_post);
        context=My_I_ReportPostActivity.this;

        iv_back=findViewById(R.id.iv_back);
        recycler_my_report=findViewById(R.id.recycler_my_report);

        if (Conectivity.isConnected(context)){
            getMyReport();
        }else {
            Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }

    private void getMyReport() {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+get_my_i_report)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .build()
                .getObjectObservable(I_reportModel.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<I_reportModel>() {
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
                    public void onNext(I_reportModel user) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_res1" , user.getResult());

                            if (user.getResult().equalsIgnoreCase("true")){

                                iReport_get_adapter = new IReport_get_Adapter(user.getData(), context,"My_IReport");
                                recycler_my_report.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                recycler_my_report.setAdapter(iReport_get_adapter);
                                iReport_get_adapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(context, ""+user.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
