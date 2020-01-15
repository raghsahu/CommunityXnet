package com.logical.communityapp.adapter.community_adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.logical.communityapp.Activity.Community.CommunityDetailsActivity;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
//import com.logical.communityapp.databinding.AllCommunityAdapterBinding;
import com.logical.communityapp.Utils.Conectivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.join_community;

public class AllCommunityAdapter extends RecyclerView.Adapter<AllCommunityAdapter.ViewHolder> {
    private List<AllCommunityModel> allCommunityModels;
    private Context context;
    //private Session session;
    private String user_id;
    String Community;
    int mStarPosition;

    public AllCommunityAdapter(List<AllCommunityModel> allCommunityModels1, Context context, String Community) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
        this.Community = Community;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_community_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
           AllCommunityModel allCommunityModel=allCommunityModels.get(position);

            holder.tv_commu_name.setText(allCommunityModel.name);

            try {
                if (Community.equalsIgnoreCase("MyJoin")){

                    holder.btn_commu_join_now.setText("Joined");
                }else {
                    if (allCommunityModel.join_status.equalsIgnoreCase("0")){
                        holder.btn_commu_join_now.setText("Join");
                    }else {
                        holder.btn_commu_join_now.setText("Joined");
                    }
                }

            }catch (Exception e){

            }

            try {
                if (allCommunityModel.default_status.equalsIgnoreCase("0")){
                    holder.iv_star.setVisibility(View.GONE);
                }else {
                    holder.iv_star.setVisibility(View.VISIBLE);
                    mStarPosition=position;
                }
            }catch (Exception e){

            }




            holder.card_community_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommunityDetailsActivity.class);
                    intent.putExtra("Community_Id", allCommunityModels.get(position).id);
                    intent.putExtra("Join_status", allCommunityModels.get(position).join_status);
                    context.startActivity(intent);
                }
            });

            holder.btn_commu_join_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String CommuId=allCommunityModels.get(position).id;
                    if (holder.btn_commu_join_now.getText().equals("Joined")){
                        Toast.makeText(context, "Already joined", Toast.LENGTH_SHORT).show();
                    }else {
                        if (Conectivity.isConnected(context)){
                            JoinCommunity(CommuId,holder.btn_commu_join_now);

                        }
                        else {
                            Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });




            if (!allCommunityModel.icon.equalsIgnoreCase(null)){
                Picasso.with(context).load(community_icon_BaseUrl+allCommunityModel.icon)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.star)
                        .error(R.drawable.star)
                        .into(holder.iv_sample1);
            }

        }
    }

    private void JoinCommunity(String commuId, final Button btn_commu_join_now) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+join_community;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                 .addBodyParameter("community_id",commuId )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(context, "Joined successfully", Toast.LENGTH_SHORT).show();
                                btn_commu_join_now.setText("Joined");
                               // Intent intent = new Intent(context, CommunityJoin_NowActivity.class);
                               // context.startActivity(intent);
                            } else {
                                String message = jsonObject.getString("msg");
                                 Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();


                    }
                });


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
//        public AllCommunityAdapterBinding allCommunityAdapterBinding;
//
//        public ViewHolder(AllCommunityAdapterBinding itemRowBinding) {
//            super(itemRowBinding.getRoot());
//            this.allCommunityAdapterBinding = itemRowBinding;
//        }

        public TextView tv_commu_name;
        public ImageView iv_sample1,iv_star;
        public LinearLayout ll_item;
        CardView card_community_details;
        Button btn_commu_join_now;


        public ViewHolder(View parent) {
            super(parent);
            card_community_details = parent.findViewById(R.id.card_community_details);
            btn_commu_join_now = parent.findViewById(R.id.btn_commu_join_now);
            iv_sample1 = parent.findViewById(R.id.iv_sample1);
            tv_commu_name = parent.findViewById(R.id.tv_commu_name);
            iv_star = parent.findViewById(R.id.iv_star);


        }

//        public void bind(AllCommunityModel allCommunityModel) {
//            allCommunityAdapterBinding.setVariable(BR._all, allCommunityModel);
//            allCommunityAdapterBinding.executePendingBindings();
//        }



    }


//    @Override
//    public void cardClicked(AllCommunityModel f) {
//        Toast.makeText(context, "You clicked " + f.name,
//                Toast.LENGTH_LONG).show();
//    }
}
