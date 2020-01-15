package com.logical.communityapp.adapter.community_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.model_class.i_report_model.CommentData;
import com.logical.communityapp.R;

import java.util.LinkedHashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

/**
 * Created by Raghvendra Sahu on 06/12/2019.
 */
public class CommentI_ReportAdapter extends RecyclerView.Adapter<CommentI_ReportAdapter.ViewHolder> {
    private List<CommentData> allCommunityModels;
    private Context context;
    private String user_id;
    LinkedHashSet<String>chat_id_linked=new LinkedHashSet<>();
    AdapterCallback mAdapterCallback;


    public CommentI_ReportAdapter(List<CommentData> allCommunityModels1, Context context) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_report_comment_item, parent, false);

        return new ViewHolder(itemView);
    }

    public interface AdapterCallback {
        void onMethodCallback(LinkedHashSet<String> cat_id);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

        if (allCommunityModels.size() > 0) {
            final CommentData allCommunityModel=allCommunityModels.get(position);
            holder.comment_name.setText(allCommunityModel.getMemberName());
            holder.tv_date_comment.setText(allCommunityModel.getDate());
            holder.tv_comment.setText(allCommunityModel.getComment());

            if (!allCommunityModel.getMemberImage().equalsIgnoreCase(null)){
                Glide.with(context)
                        .load(member_pic_BaseUrl+allCommunityModel.getMemberImage())
                        // .apply(options)
                        .placeholder(R.drawable.man)
                        .into(holder.ivUserImage);
            }


            holder.card_view_chat.setOnLongClickListener(new View.OnLongClickListener() {

                boolean chat_clicked=true;
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onLongClick(View v) {


                    if (allCommunityModels.get(position).getMemberId().
                            equalsIgnoreCase(Shared_Preference.getUser_Id(context))){

                        String chat_pos=allCommunityModels.get(position).getId();

                        if (!chat_clicked){
                           // holder.card_view_chat.setCardBackgroundColor(R.color.white);
                            chat_id_linked.remove(chat_pos);
                            holder.card_view_chat.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            chat_clicked=true;
                        }else {
                            holder.card_view_chat.setCardBackgroundColor(R.color.silver);
                            chat_id_linked.add(chat_pos);
                            chat_clicked=false;
                        }

                        try {
                            mAdapterCallback.onMethodCallback(chat_id_linked);
                        } catch (ClassCastException exception) {
                            // do something
                            Log.e("tab_sel_error", exception.toString());
                        }

                        Log.e("chat_pos_id",""+chat_id_linked);

                    }


                    return false;
                }
            });


        }
    }



    @Override
    public int getItemCount() {
        return allCommunityModels.size();
        //return rattingReviewList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment_name,tv_date_comment,tv_comment;
        public CircleImageView ivUserImage;
        CardView card_view_chat;


        public ViewHolder(View parent) {
            super(parent);
            ivUserImage = parent.findViewById(R.id.ivUserImage);
            comment_name = parent.findViewById(R.id.comment_name);
            tv_date_comment = parent.findViewById(R.id.tv_date_comment);
            tv_comment = parent.findViewById(R.id.tv_comment);
            card_view_chat = parent.findViewById(R.id.card_view_chat);


        }

    }
}
