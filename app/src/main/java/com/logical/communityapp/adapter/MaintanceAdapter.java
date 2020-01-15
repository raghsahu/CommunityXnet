package com.logical.communityapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.All_Chat.Chat_Activity;
import com.logical.communityapp.Activity.Maintenance_Request.MaintenanceDetailsActivity;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestData;
import com.logical.communityapp.R;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.complete_maintenance_request;
import static com.logical.communityapp.Api_Url.Base_Url.delete_maintenance_request;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

/**
 * Created by Raghvendra Sahu on 09/12/2019.
 */
public class MaintanceAdapter extends RecyclerView.Adapter<MaintanceAdapter.ViewHolder> {
    private List<MaintanceRequestData> allCommunityModels;
    private Context context;
    private String user_id;
    String RequestPost;

    public MaintanceAdapter(List<MaintanceRequestData> allCommunityModels1, Context context, String RequestPost) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
        this.RequestPost = RequestPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_maintenance_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final MaintanceRequestData allCommunityModel=allCommunityModels.get(position);
            holder.tv_name.setText(allCommunityModel.getMemberName());
            holder.tv_date.setText(allCommunityModel.getDate());
            holder.tv_desc.setText(allCommunityModel.getTitleOfRequest());

            if (allCommunityModel.getMemberImage()!=null){
                Glide.with(context)
                        .load(member_pic_BaseUrl+allCommunityModel.getMemberImage())
                        // .apply(options)
                        .placeholder(R.drawable.man)
                        .into(holder.iv_maint);
            }

            if (RequestPost.equalsIgnoreCase("MySelfPost")){
                holder.ll_del_part.setVisibility(View.VISIBLE);
                holder.tv_close_job.setText(allCommunityModel.getStatus());

            }else {
                holder.ll_del_part.setVisibility(View.VISIBLE);
                holder.tv_close_job.setVisibility(View.GONE);
                holder.iv_delete.setVisibility(View.GONE);
                holder.tv_change_status.setVisibility(View.GONE);
            }

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostId=allCommunityModels.get(position).getId();
                    DeleteMyPostNow(PostId,allCommunityModels.get(position),position);//rxjava with android networking use default responce

                }
            });

            holder.iv_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, Chat_Activity.class);
                    context.startActivity(intent);
                }
            });

        holder.tv_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allCommunityModels.get(position).getStatus().equalsIgnoreCase("Completed")){
                        Toast.makeText(context, "Already completed", Toast.LENGTH_SHORT).show();
                    }else {

                    String PostId=allCommunityModels.get(position).getId();
                    CloseMyPostNow(PostId,allCommunityModels.get(position),position,holder.tv_close_job);//rxjava with android networking use default responce

                    }
                }
            });

            holder.ll_request_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("post_img_maint", allCommunityModels.get(position).getImage());

                    Intent intent = new Intent(context, MaintenanceDetailsActivity.class);
                    intent.putExtra("Post_id", allCommunityModels.get(position).getId());
                    intent.putExtra("MaintanceRequestData", allCommunityModels.get(position));
                    context.startActivity(intent);
                }
            });

        }
    }

    private void CloseMyPostNow(String postId, MaintanceRequestData maintanceRequestData, int position, TextView tv_close_job) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+complete_maintenance_request)
                .addBodyParameter("id", postId)
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
                               allCommunityModels.get(position).setStatus("Completed");
                                tv_close_job.setText("Completed");

                            }else {
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                         notifyDataSetChanged();

                        }catch (Exception e){

                        }

                    }
                });


    }

    private void DeleteMyPostNow(String postId, MaintanceRequestData maintanceRequestData, int position) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+delete_maintenance_request)
                .addBodyParameter("id", postId)
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
                                allCommunityModels.remove(position);
                                allCommunityModels.remove(maintanceRequestData);
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
        return allCommunityModels.size();
        //return rattingReviewList.size();

    }

    public void updateList(ArrayList<MaintanceRequestData> contact_li) {
        allCommunityModels = contact_li;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name,tv_date,tv_desc,tv_close_job,tv_change_status;
        public ImageView iv_maint,iv_delete,iv_chat;
        LinearLayout ll_del_part,ll_request_details;
        CardView card_main;
        RelativeLayout rl_delete;

        public ViewHolder(View parent) {
            super(parent);
            card_main = parent.findViewById(R.id.card_main);
            iv_maint = parent.findViewById(R.id.iv_maint);
            tv_name = parent.findViewById(R.id.tv_name);
            tv_date = parent.findViewById(R.id.tv_date);
            tv_desc = parent.findViewById(R.id.tv_desc);
            ll_del_part = parent.findViewById(R.id.ll_del_part);
            rl_delete = parent.findViewById(R.id.rl_delete);
            tv_close_job = parent.findViewById(R.id.tv_close_job);
            iv_delete = parent.findViewById(R.id.iv_delete);
            tv_change_status = parent.findViewById(R.id.tv_change_status);
            ll_request_details = parent.findViewById(R.id.ll_request_details);
            iv_chat = parent.findViewById(R.id.iv_chat);

        }

    }
}
