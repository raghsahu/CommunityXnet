package com.logical.communityapp.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.logical.communityapp.R;
import com.logical.communityapp.databinding.NotificationItemBinding;
import com.logical.communityapp.model_class.notification_model.NotificationModelData;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {

    private List<NotificationModelData> dataModelList;
    Context context;


    public NotificationAdapter(List<NotificationModelData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificationItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.notification_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModelData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        //holder.itemRowBinding.setItemClickListener(this);

        Log.e("all_artisansSize", ""+dataModel.getMessage());





    }



    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NotificationItemBinding itemRowBinding;

        public ViewHolder(NotificationItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(com.logical.communityapp.BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }

//    public void cardClicked(MarketPlaceProductData f) {
//        // Toast.makeText(context, "You clicked " + f.getProductName(), Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(context, MarketProductDetailsActivity.class);
//        intent.putExtra("Product_id", f.getId());
//        // intent.putExtra("MaintanceRequestData", f);
//        context.startActivity(intent);
//
//    }
}
