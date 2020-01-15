package com.logical.communityapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.logical.communityapp.Activity.DirectoryListing.DirectoryBusinessActivity;
import com.logical.communityapp.Activity.Maintenance_Request.Post_NewService_Request;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.DirectoryListingAdapter;
import com.logical.communityapp.R;
import com.logical.communityapp.databinding.FragmentDirectoryListFragmentBinding;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class Directory_List_fragment extends Fragment {
    FragmentDirectoryListFragmentBinding binding;
    //RecyclerView recycler_artisan_list;
    DirectoryListingAdapter allArtisanAdapter;



    public Directory_List_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);
        //View v = (View) menu.findItem(R.id.action_add).getActionView();
        MenuItem iv_add = menu.findItem(R.id.action_add);

        iv_add.setVisible(true);

        iv_add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

               // Intent intent=new Intent(getActivity(), PostArtisanActivity.class);
                Intent intent=new Intent(getActivity(), Post_NewService_Request.class);
                intent.putExtra("Services", "Professional Services");
                startActivity(intent);
                //getActivity().finish();

                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // View root= inflater.inflate(R.layout.fragment_directory__list_fragment, container, false);
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_directory__list_fragment, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        getActivity().setTitle("Directory listing");

        if (Conectivity.isConnected(getActivity())){
            GetArtisanList();//api call using rx java with retrofit
        }else {
            Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
        }


        binding.tvArtisans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), DirectoryBusinessActivity.class);
                startActivity(intent);


            }
        });


        return root;
    }

    @SuppressLint("CheckResult")
    private void GetArtisanList() {

        final ProgressDialog progressDialog =new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetDirectoryList(Shared_Preference.getUser_Id(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MaintanceRequestModel>() {
                    @Override
                    public void onNext(MaintanceRequestModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_joinArtisans", response.getMsg());

                            if (response.getResult().equalsIgnoreCase("true")){
                              //  Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                                allArtisanAdapter = new DirectoryListingAdapter(response.getData(), getActivity());
                                binding.setMyAdapter(allArtisanAdapter);//set databinding adapter
                                allArtisanAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(getActivity(), ""+response.getMsg(), Toast.LENGTH_SHORT).show();
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


}
