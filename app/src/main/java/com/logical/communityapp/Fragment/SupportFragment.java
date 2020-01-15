package com.logical.communityapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.logical.communityapp.Activity.MarketPlace.MyMarketPlace;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.NotificationAdapter;
import com.logical.communityapp.databinding.SupportFragmentBinding;
import com.logical.communityapp.model_class.notification_model.NotificationModel;
import com.logical.communityapp.model_class.support_model.SupportFillData;
import com.logical.communityapp.model_class.support_model.SupportSubmitModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Raghvendra Sahu on 27/12/2019.
 */
public class SupportFragment extends Fragment {

    SupportFillData obj;
    SupportFragmentBinding binding;

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.support_fragment, container, false);
        View root = binding.getRoot();
        //View root= inflater.inflate(R.layout.support_fragment, container, false);
        getActivity().setTitle("Support");

        obj = new SupportFillData(getActivity());
        obj.setTitle_query("");
        obj.setQuery_desc("");
        binding.setSupportdata(obj);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!obj.getTitle_query().isEmpty() && !obj.getQuery_desc().isEmpty()){

                    if (Conectivity.isConnected(getActivity())){

                      SubmitSupportApi(obj.getTitle_query(),obj.getQuery_desc());
                       // Toast.makeText(getActivity(), ""+obj.getQuery_desc()+obj.getTitle_query(), Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return  root;
    }

    @SuppressLint("CheckResult")
    private void SubmitSupportApi(String title_query, String query_desc) {
        final ProgressDialog progressDialog =new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SendSupport(title_query,query_desc,Shared_Preference.getUser_Id(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SupportSubmitModel>() {
                    @Override
                    public void onNext(SupportSubmitModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_mr_product", response.getResult());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            if (response.getResult().equalsIgnoreCase("true")){
                                 Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                                obj.setTitle_query("");
                                obj.setQuery_desc("");
                                binding.setSupportdata(obj);

                            }else {

                                Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
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
