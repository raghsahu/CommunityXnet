package com.logical.communityapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.communityapp.Activity.Maintenance_Request.MaintenanceDetailsActivity;
import com.logical.communityapp.BR;
import com.logical.communityapp.biding_interface.CustomClickListener;
import com.logical.communityapp.databinding.AllArtisanListAdapterBinding;
import com.logical.communityapp.R;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestData;

import java.util.List;

public class DirectoryListingAdapter extends RecyclerView.Adapter<DirectoryListingAdapter.ViewHolder> implements CustomClickListener {

private List<MaintanceRequestData> dataModelList;
private Context context;

public DirectoryListingAdapter(List<MaintanceRequestData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        AllArtisanListAdapterBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.all_artisan_list_adapter, parent, false);

        return new ViewHolder(binding);
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
    MaintanceRequestData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
    holder.itemRowBinding.setModel(dataModel);
        holder.itemRowBinding.setItemClickListener(this);

    Log.e("all_artisansSize", ""+dataModel.getTitleOfRequest());
        }


@Override
public int getItemCount() {
        return dataModelList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public AllArtisanListAdapterBinding itemRowBinding;

    public ViewHolder(AllArtisanListAdapterBinding itemRowBinding) {
        super(itemRowBinding.getRoot());
        this.itemRowBinding = itemRowBinding;
    }

    public void bind(Object obj) {
        itemRowBinding.setVariable(BR.model, obj);
        itemRowBinding.executePendingBindings();
    }
}

    public void cardClicked(MaintanceRequestData f) {
       // Toast.makeText(context, "You clicked " + f.getTitleOfRequest(),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, MaintenanceDetailsActivity.class);
        intent.putExtra("Post_id", f.getId());
        intent.putExtra("MaintanceRequestData", f);
        context.startActivity(intent);

    }
}
