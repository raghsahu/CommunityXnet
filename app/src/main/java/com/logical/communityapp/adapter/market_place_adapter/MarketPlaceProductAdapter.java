package com.logical.communityapp.adapter.market_place_adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.communityapp.Activity.MarketPlace.MarketProductDetailsActivity;
import com.logical.communityapp.Activity.MarketPlace.UploadProductActivity;
import com.logical.communityapp.BR;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.biding_interface.MarketPlaceClickListener;
import com.logical.communityapp.databinding.MarketProductListAdapterBinding;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductData;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONObject;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.delete_market_place_product;
import static com.logical.communityapp.Api_Url.Base_Url.mark_in_out_stock_product;

/**
 * Created by Raghvendra Sahu on 01/01/2020.
 */
public class MarketPlaceProductAdapter extends RecyclerView.Adapter<MarketPlaceProductAdapter.ViewHolder> implements MarketPlaceClickListener {

    private List<MarketPlaceProductData> dataModelList;
     Context context;
    String RequestPost;

    public MarketPlaceProductAdapter(List<MarketPlaceProductData> dataModelList, Context ctx, String requestPost) {
        this.dataModelList = dataModelList;
        context = ctx;
        this.RequestPost = requestPost;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MarketProductListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.market_product_list_adapter, parent, false);

        return new ViewHolder(binding);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarketPlaceProductData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        holder.itemRowBinding.setItemClickListener(this);

        Log.e("all_artisansSize", ""+dataModel.getProductName());

        if (RequestPost.equalsIgnoreCase("MySelfPost")){
            holder.itemRowBinding.llDelPart.setVisibility(View.VISIBLE);
            holder.itemRowBinding.switchBtn.setVisibility(View.VISIBLE);

        }

        if (dataModelList.get(position).getStockStatus().equalsIgnoreCase("In Stock")){
            holder.itemRowBinding.switchBtn.setChecked(true);
        }

        holder.itemRowBinding.switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    String SwitchText="In Stock";
                    Stock_in_out_Status(SwitchText,dataModelList.get(position).getId(),position,holder.itemRowBinding.tvStatus);
                } else {
                    // The toggle is disabled
                    String SwitchText="Out of Stock";
                    Stock_in_out_Status(SwitchText,dataModelList.get(position).getId(),position,holder.itemRowBinding.tvStatus);
                }
            }
        });


        holder.itemRowBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (Conectivity.isConnected(context)){

                   DeleteMyProduct(dataModelList.get(position).getId(),dataModelList.get(position),position);
               }else{
                   Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
               }

            }
        });

        holder.itemRowBinding.tvUpdatePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Conectivity.isConnected(context)){

                    Intent intent=new Intent(context, UploadProductActivity.class);
                    intent.putExtra("Update", "Update");
                    intent.putExtra("ProId", dataModelList.get(position).getId());
                    intent.putExtra("ProName", dataModelList.get(position).getProductName());
                    intent.putExtra("ProDesc", dataModelList.get(position).getDescription());
                    intent.putExtra("ProCost", dataModelList.get(position).getPrice());
                    context.startActivity(intent);

                }else{
                    Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void Stock_in_out_Status(String switchText, String id, final int position, TextView tvStatus) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+mark_in_out_stock_product)
                .addBodyParameter("id", id)
                .addBodyParameter("stock_status", switchText)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
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
                    public void onNext(JSONObject jsonObject) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_update" , jsonObject.toString());

                            String result=jsonObject.getString("result");
                            String msg=jsonObject.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                dataModelList.get(position).setStockStatus(switchText);
                               // dataModelList.remove(marketPlaceProductData);
                               // notifyDataSetChanged();
                                tvStatus.setText(dataModelList.get(position).getStockStatus());
                            }else {
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });

    }

    private void DeleteMyProduct(String id, MarketPlaceProductData marketPlaceProductData, int position) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+delete_market_place_product)
                .addBodyParameter("id", id)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
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
                    public void onNext(JSONObject jsonObject) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_res1" , jsonObject.toString());

                            String result=jsonObject.getString("result");
                            String msg=jsonObject.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                dataModelList.remove(position);
                                dataModelList.remove(marketPlaceProductData);
                                notifyDataSetChanged();
                            }else {
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });
    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MarketProductListAdapterBinding itemRowBinding;

        public ViewHolder(MarketProductListAdapterBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public void cardClicked(MarketPlaceProductData f) {
      // Toast.makeText(context, "You clicked " + f.getProductName(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, MarketProductDetailsActivity.class);
        intent.putExtra("Product_id", f.getId());
       // intent.putExtra("MaintanceRequestData", f);
        context.startActivity(intent);

    }
}
