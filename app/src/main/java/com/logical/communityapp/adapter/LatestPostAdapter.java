package com.logical.communityapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.communityapp.model_class.LatestPostModel;
import com.logical.communityapp.R;

import java.util.List;

public class LatestPostAdapter extends RecyclerView.Adapter<LatestPostAdapter.ViewHolder> {
    private List<LatestPostModel> latestPostModels;
    private LatestPostModel latestPostModel;
    private Context context;
    //private Session session;
    private String user_id;

    public LatestPostAdapter(List<LatestPostModel> latestPostModels1, Context context) {
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
            holder.tv_date.setText(latestPostModel.date);
            holder.tv_desc.setText(latestPostModel.description);
            holder.tv_headline.setText(latestPostModel.headline);


            /*if (!rattingReviewData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(rattingReviewData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/

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

                                case R.id.action_comment:
                                    //handle menu1 click
                                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

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
        //return rattingReviewList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date, tv_headline,tv_desc,textViewOptions;
        public ImageView iv_delete;
        public LinearLayout ll_item;


        public ViewHolder(View parent) {
            super(parent);
          //  ll_item = parent.findViewById(R.id.ll_item);
            tv_date = parent.findViewById(R.id.tv_date);
            tv_headline = parent.findViewById(R.id.tv_headline);
            tv_desc = parent.findViewById(R.id.tv_desc);
            textViewOptions = parent.findViewById(R.id.textViewOptions);


        }
    }
}
