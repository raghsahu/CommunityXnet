package com.logical.communityapp.adapter.community_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.communityapp.Activity.Events_Menu.Event_List_Activity;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;

/**
 * Created by Raghvendra Sahu on 13/11/2019.
 */
public class SelectCommunityAdapter extends RecyclerView.Adapter<SelectCommunityAdapter.ViewHolder> {
    private List<AllCommunityModel> allCommunityModels;
    private Context context;
    private String user_id;

    public SelectCommunityAdapter(List<AllCommunityModel> allCommunityModels1, Context context) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_select_commu_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final AllCommunityModel allCommunityModel=allCommunityModels.get(position);
            holder.tv_commu_name.setText(allCommunityModel.name);

            if (allCommunityModel.join_status.equalsIgnoreCase("0")){
               // holder.btn_commu_join_now.setText("Join");
            }else {
               // holder.btn_commu_join_now.setText("Joined");
            }

            if (!allCommunityModel.icon.equalsIgnoreCase(null)){
                Picasso.with(context).load(community_icon_BaseUrl+allCommunityModel.icon)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.star)
                        .error(R.drawable.star)
                        .into(holder.iv_sample1);
            }

            holder.card_community_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, Event_List_Activity.class);
                    intent.putExtra("Community_Id", allCommunityModels.get(position).id);
                    context.startActivity(intent);

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return allCommunityModels.size();
        //return rattingReviewList.size();

    }

    public void updateList(ArrayList<AllCommunityModel> contact_li) {
        allCommunityModels = contact_li;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_commu_name;
        public ImageView iv_sample1;
        public LinearLayout ll_item;
        CardView card_community_details;


        public ViewHolder(View parent) {
            super(parent);
            card_community_details = parent.findViewById(R.id.card_community_details);
            iv_sample1 = parent.findViewById(R.id.iv_sample1);
            tv_commu_name = parent.findViewById(R.id.tv_commu_name);


        }



    }

}
