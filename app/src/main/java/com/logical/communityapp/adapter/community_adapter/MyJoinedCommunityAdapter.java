package com.logical.communityapp.adapter.community_adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.Profile_Related.SeeListJoinedCommunity;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.remove_my_joined_community;
import static com.logical.communityapp.Api_Url.Base_Url.set_default_community;

/**
 * Created by Raghvendra Sahu on 12/11/2019.
 */
public class MyJoinedCommunityAdapter extends RecyclerView.Adapter<MyJoinedCommunityAdapter.ViewHolder> {
    private List<AllCommunityModel> allCommunityModels;
    private Context context;
    //private Session session;
    private String user_id;
     int mStarPosition;

    public MyJoinedCommunityAdapter(List<AllCommunityModel> allCommunityModels1, Context context) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myself_commu_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final AllCommunityModel allCommunityModel=allCommunityModels.get(position);

            holder.tv_commu_name.setText(allCommunityModel.name);

            if (!allCommunityModel.icon.equalsIgnoreCase(null)){
                Glide.with(context)
                        .load(community_icon_BaseUrl+allCommunityModel.icon)
                        .centerCrop()
                        .placeholder(R.drawable.sunny)
                       // .error(R.drawable.sunny)
                        .placeholder(R.drawable.man)
                        .into(holder.iv_sample1);
            }




            if (allCommunityModel.default_status.equalsIgnoreCase("0")){
                holder.iv_star.setVisibility(View.GONE);
            }else {
                holder.iv_star.setVisibility(View.VISIBLE);
                mStarPosition=position;
            }

            holder.card_community_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                           // .setTitle(R.string.app_name)
                            .setMessage("Make a default this community");

                    dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                        MakeDefault(allCommunityModel.id,allCommunityModel,position,holder.iv_star);

                        }

                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();

                }
            });


            holder.btn_commu_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LeaveCommunityGroup(allCommunityModel.id,allCommunityModels.get(position),position);

                }
            });

        }
    }

    private void LeaveCommunityGroup(String id, final AllCommunityModel allCommunityModel, final int position) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+remove_my_joined_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .addBodyParameter("community_id",id )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                          progressDialog.dismiss();
                        try {

                            Log.e("res_my", jsonObject.toString());
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")) {

                                allCommunityModels.remove(position);
                                allCommunityModels.remove(allCommunityModel);
                                notifyDataSetChanged();

                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                         progressDialog.dismiss();

                    }
                });


    }

    private void MakeDefault(String id, AllCommunityModel allCommunityModel, final int position, final ImageView iv_star) {

//        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
////        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
////        progressDialog.show();

        String url=BaseUrl+set_default_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .addBodyParameter("community_id",id )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                      //  progressDialog.dismiss();
                        try {

                            Log.e("res_my", jsonObject.toString());
                             String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")) {
//                            if (mStarPosition==position){
//                                iv_star.setVisibility(View.VISIBLE);
//                            }else {
//                                iv_star.setVisibility(View.GONE);
//                            }
//                                iv_star.setVisibility(View.VISIBLE);

                                Intent intent=new Intent(context, SeeListJoinedCommunity.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();

                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                       // progressDialog.dismiss();

                    }
                });


    }


    @Override
    public int getItemCount() {
        return allCommunityModels.size();
        //return rattingReviewList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_commu_name;
        public ImageView iv_sample1,iv_star;
        public LinearLayout ll_item;
        CardView card_community_details;
        Button btn_commu_join_now,btn_commu_remove;


        public ViewHolder(View parent) {
            super(parent);
            card_community_details = parent.findViewById(R.id.card_community_details);
            //btn_commu_join_now = parent.findViewById(R.id.btn_commu_join_now);
            iv_sample1 = parent.findViewById(R.id.iv_sample1);
            tv_commu_name = parent.findViewById(R.id.tv_commu_name);
            iv_star = parent.findViewById(R.id.iv_star);
            btn_commu_remove = parent.findViewById(R.id.btn_commu_remove);


        }




    }
}
