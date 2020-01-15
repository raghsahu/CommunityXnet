package com.logical.communityapp.adapter.market_place_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.communityapp.Activity.MarketPlace.MarketProductDetailsActivity;
import com.logical.communityapp.R;
import com.logical.communityapp.biding_interface.MarketPlaceClickListener;
import com.logical.communityapp.databinding.MarketProductListAllBinding;
import com.logical.communityapp.model_class.market_place_model.MarketPlaceProductData;


import java.util.List;


public class MarketAllProductAdapter extends RecyclerView.Adapter<MarketAllProductAdapter.ViewHolder> implements MarketPlaceClickListener {

    private List<MarketPlaceProductData> dataModelList;
    Context context;


    public MarketAllProductAdapter(List<MarketPlaceProductData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MarketProductListAllBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.market_product_list_all, parent, false);

        return new ViewHolder(binding);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarketPlaceProductData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        holder.itemRowBinding.setItemClickListener(this);

      // Log.e("all_artisansSize", ""+dataModel.getProductName());


    }



    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MarketProductListAllBinding itemRowBinding;

        public ViewHolder(MarketProductListAllBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(com.logical.communityapp.BR.model, obj);
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
