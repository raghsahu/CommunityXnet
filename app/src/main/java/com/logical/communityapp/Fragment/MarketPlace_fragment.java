package com.logical.communityapp.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.logical.communityapp.Activity.MarketPlace.MyMarketPlace;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Api_Url.Base_Url;
import com.logical.communityapp.Api_Url.RxApiClicent;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.market_place_adapter.MarketAllProductAdapter;
import com.logical.communityapp.databinding.FragmentMarketPlaceFragmentBinding;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.Main_category_modelData;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductData;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductModel;
import com.rxandroidnetworking.RxAndroidNetworking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.Observer;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_market_place_category;

public class MarketPlace_fragment extends Fragment {

    FragmentMarketPlaceFragmentBinding binding;
    MarketAllProductAdapter marketPlaceProductAdapter;
    FloatingActionButton fiter_product;
    private Dialog Hoadialog;
    Spinner  spin_cate;
    private String Category_ID;
    HashMap<Integer, Main_category_modelData> CategoryHashMap=new HashMap<Integer, Main_category_modelData>();
    List<MarketPlaceProductData> MarketplaceArrayList=new ArrayList<>();

    public MarketPlace_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

                Intent intent=new Intent(getActivity(), MyMarketPlace.class);
               // intent.putExtra("Services", "Professional Services");
                startActivity(intent);

                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // View root= inflater.inflate(R.layout.fragment_market_place_fragment, container, false);
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_place_fragment, container, false);
        View root = binding.getRoot();

        getActivity().setTitle("Market place");
        setHasOptionsMenu(true);

        //get product
        if (Conectivity.isConnected(getActivity())){
            GetAllProduct("");//using rxjava with retrofit
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        binding.fiterMaintanence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenfilterDialog();

            }
        });


        return  root;
    }

    private void OpenfilterDialog() {

        Hoadialog = new Dialog(getActivity());
        Hoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Hoadialog.setCancelable(true);
        Hoadialog.setContentView(R.layout.filter_mainten_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout ll_next=Hoadialog.findViewById(R.id.ll_next);
         spin_cate = Hoadialog.findViewById(R.id.spin_cate);

        GetPostCategory();

        //spinner category select
        spin_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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


        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Conectivity.isConnected(getActivity())){
                    Hoadialog.dismiss();
                    GetAllProduct(Category_ID);

                }else {
                    Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
                }




            }
        });


        try {
            if (!getActivity().isFinishing()){
                Hoadialog.show();
            }
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }

    private void GetPostCategory() {
        RxAndroidNetworking.get(BaseUrl+get_market_place_category)
                //.addPathParameter("userId", "1")
                .build()
                .getObjectObservable(Main_category_model.class)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<Main_category_model>() {
                    @Override
                    public void onCompleted() {
                        // do anything onComplete
                    }
                    @Override
                    public void onError(Throwable e) {
                        // handle error
                    }
                    @Override
                    public void onNext(Main_category_model user) {
                        // do anything with response
                        try {
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cate_res1" , user.getResult());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getName());
                                CategoryHashMap.put(i, new Main_category_modelData(user.getData().get(i).getId(), user.getData().get(i).getName()));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            spin_cate.setAdapter(dataAdapter);

                        }catch (Exception e){

                        }

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void GetAllProduct(String category_ID) {
        final ProgressDialog progressDialog =new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClicent.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetMarketProduct(category_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MarketPlaceProductModel>() {
                    @Override
                    public void onNext(MarketPlaceProductModel response) {
                        //Handle logic
                        try {
                            MarketplaceArrayList.clear();
                            progressDialog.dismiss();
                            Log.e("result_mr_product", response.getMsg());
                            // Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();

                            if (response.getResult().equalsIgnoreCase("true")){
                                MarketplaceArrayList=response.getData();
                                //  Toast.makeText(DirectoryBusinessActivity.this, ""+response.getMsg(), Toast.LENGTH_SHORT).show();
                                marketPlaceProductAdapter = new MarketAllProductAdapter(MarketplaceArrayList, getActivity());
                                binding.setMyAdapter(marketPlaceProductAdapter);//set databinding adapter
                                marketPlaceProductAdapter.notifyDataSetChanged();

                            }else {

                                Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                            }

                            marketPlaceProductAdapter.notifyDataSetChanged();

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
    public void onResume() {
        super.onResume();
        //get product
        if (Conectivity.isConnected(getActivity())){
            GetAllProduct("");//using rxjava with retrofit
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
