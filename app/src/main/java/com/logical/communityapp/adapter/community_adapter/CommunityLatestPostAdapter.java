package com.logical.communityapp.adapter.community_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.Community.LatestPostDetails;
import com.logical.communityapp.model_class.community_model.CommunityLatestPostModelData;
import com.logical.communityapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

/**
 * Created by Raghvendra Sahu on 20/12/2019.
 */
public class CommunityLatestPostAdapter extends RecyclerView.Adapter<CommunityLatestPostAdapter.ViewHolder> {
    private List<CommunityLatestPostModelData> latestPostModels;
    private CommunityLatestPostModelData latestPostModel;
    private Context context;
    //private Session session;
    private String user_id;

    public CommunityLatestPostAdapter(List<CommunityLatestPostModelData> latestPostModels1, Context context) {
        this.latestPostModels = latestPostModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.latestpostadapter, parent, false);
        /*session = new Session(context);
        user_id = session.getUser().user_id;*/
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (latestPostModels.size() > 0) {
            latestPostModel = latestPostModels.get(position);
            holder.tv_date.setText(latestPostModel.getDate());
          //  holder.tv_desc.setText(latestPostModel.getDescription());
            holder.tv_headline.setText(latestPostModel.getDescription());



            if (latestPostModels.get(position).getMemberImage()!=null){

                Glide.with(context)
                        .load(member_pic_BaseUrl+latestPostModels.get(position).getMemberImage())
                        .centerCrop()
                        .placeholder(R.drawable.man)
                        .into(holder.member_icon);

            }

            holder.ll_latest_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, LatestPostDetails.class);
                    intent.putExtra("LatestId", latestPostModels.get(position).getId());
                    intent.putExtra("LatestDetails", latestPostModels.get(position));
                    context.startActivity(intent);

                }
            });


            holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.latest_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_report:
                                    //handle menu1 click
                                    Toast.makeText(context, "report success", Toast.LENGTH_SHORT).show();
                                    return true;



                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return latestPostModels.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date, tv_headline,tv_desc,textViewOptions;
        public CircleImageView member_icon;
        public LinearLayout ll_latest_description;


        public ViewHolder(View parent) {
            super(parent);
            ll_latest_description = parent.findViewById(R.id.ll_latest_description);
            tv_date = parent.findViewById(R.id.tv_date);
            tv_headline = parent.findViewById(R.id.tv_headline);
            tv_desc = parent.findViewById(R.id.tv_desc);
            textViewOptions = parent.findViewById(R.id.textViewOptions);
            member_icon = parent.findViewById(R.id.member_icon);


        }
    }
}
