package com.logical.communityapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.NotificationAdapter;
import com.logical.communityapp.databinding.NotiFragmentBinding;
import com.logical.communityapp.model_class.notification_model.NotificationModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Notification_fragment extends Fragment {
    NotiFragmentBinding binding;
    NotificationAdapter notificationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
       binding = DataBindingUtil.inflate(inflater, R.layout.noti_fragment, container, false);
        View root = binding.getRoot();
        getActivity().setTitle("Notification");


        if (Conectivity.isConnected(getActivity())){
            getAllNotification();

        }else {
            Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
        }



        return root;

    }

    @SuppressLint("CheckResult")
    private void getAllNotification() {
        final ProgressDialog progressDialog =new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetNotification(Shared_Preference.getUser_Id(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<NotificationModel>() {
                    @Override
                    public void onNext(NotificationModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_mr_product", response.getResult());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            if (response.getResult().equalsIgnoreCase("true")){
                                //  Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
                                  notificationAdapter = new NotificationAdapter(response.getData(), getActivity());
                                binding.setMyAdapter(notificationAdapter);//set databinding adapter
                                notificationAdapter.notifyDataSetChanged();

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
