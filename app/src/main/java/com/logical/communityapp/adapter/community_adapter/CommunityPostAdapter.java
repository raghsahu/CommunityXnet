package com.logical.communityapp.adapter.community_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.communityapp.model_class.community_model.Post_CommunityModel;
import com.logical.communityapp.R;

import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.CommuImage_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.ViewHolder> {
    private List<Post_CommunityModel> post_communityModels;
    private Context context;
    //private Session session;
    private String user_id;

    public CommunityPostAdapter(List<Post_CommunityModel> post_communityModels1, Context context) {
        this.post_communityModels = post_communityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (post_communityModels.size() > 0) {
            holder.tv_name.setText(post_communityModels.get(position).first_name+" "+
                    post_communityModels.get(position).last_name);
            holder.tv_date.setText(post_communityModels.get(position).date);
            holder.tv_disc.setText(post_communityModels.get(position).description);

            if (post_communityModels.get(position).image!=null
                    && !post_communityModels.get(position).image.isEmpty()){

                Glide.with(context)
                        .load(CommuImage_BaseUrl+post_communityModels.get(position).image)
                        .centerCrop()
                        .placeholder(R.drawable.sunny)
                        .into(holder.iv_post_image);

            }else {
                holder.iv_post_image.setVisibility(View.GONE);
            }

            if (!post_communityModels.get(position).member_image.equalsIgnoreCase(null)){

                Glide.with(context)
                        .load(member_pic_BaseUrl+post_communityModels.get(position).image)
                        .centerCrop()
                        .placeholder(R.drawable.man)
                        .into(holder.img_person);

            }

        }
    }

    @Override
    public int getItemCount() {
        return post_communityModels.size();
        //return rattingReviewList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_date,tv_disc;
        public ImageView iv_post_image,img_person;


        public ViewHolder(View parent) {
            super(parent);
            tv_name = parent.findViewById(R.id.tv_name);
            tv_disc = parent.findViewById(R.id.tv_disc);
            tv_date = parent.findViewById(R.id.tv_date);
            iv_post_image = parent.findViewById(R.id.iv_post_image);
            img_person = parent.findViewById(R.id.img_person);


        }
    }
}
