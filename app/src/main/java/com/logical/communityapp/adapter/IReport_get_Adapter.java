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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.logical.communityapp.Activity.I_Report.I_report_details_Activity;
import com.logical.communityapp.model_class.i_report_model.I_ReportModel_Details;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.I_Report_Image_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.I_Report_Video_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.delete_i_report;
import static com.logical.communityapp.Api_Url.Base_Url.i_report_like;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

/**
 * Created by Raghvendra Sahu on 23/11/2019.
 */
public class IReport_get_Adapter  extends RecyclerView.Adapter<IReport_get_Adapter.ViewHolder> {
    private List<I_ReportModel_Details> allCommunityModels;
    private Context context;
    //private Session session;
    private String user_id;
    private String report;

    public IReport_get_Adapter(List<I_ReportModel_Details> allCommunityModels1, Context context, String Report) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
        this.report = Report;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_p_name,tv_p_email,tv_date,tv_post_text,badge_count_like,badge_count_comment;
        public ImageView img_person,post_image,play_image,iv_like;
        VideoView video_view;
        public LinearLayout ll_post;
        RelativeLayout rel_like,rel_del;
        CardView card_community_details;


        public ViewHolder(View parent) {
            super(parent);
            img_person = parent.findViewById(R.id.img_person);
            tv_p_name = parent.findViewById(R.id.tv_p_name);
            tv_p_email = parent.findViewById(R.id.tv_p_email);
            tv_date = parent.findViewById(R.id.tv_date);
            tv_post_text = parent.findViewById(R.id.tv_post_text);
            badge_count_like = parent.findViewById(R.id.badge_count_like);
            badge_count_comment = parent.findViewById(R.id.badge_count_comment);
            post_image = parent.findViewById(R.id.post_image);
            play_image = parent.findViewById(R.id.play_image);
            iv_like = parent.findViewById(R.id.iv_like);
            video_view = parent.findViewById(R.id.video_view);
            ll_post = parent.findViewById(R.id.ll_post);
            rel_like = parent.findViewById(R.id.rel_like);
            rel_del = parent.findViewById(R.id.rel_del);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_report_list_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            I_ReportModel_Details allCommunityModel=allCommunityModels.get(position);

            holder.tv_p_name.setText(allCommunityModel.getMemberName());
            holder.tv_p_email.setText(allCommunityModel.getMemberEmail());
            holder.tv_date.setText(allCommunityModel.getDateTime());
            holder.tv_post_text.setText(allCommunityModel.getTexts());
            holder.badge_count_comment.setText(allCommunityModel.getTotalComments());
            holder.badge_count_like.setText(allCommunityModel.getTotalLike());

            if (report.equalsIgnoreCase("AllReport")){
                holder.rel_like.setVisibility(View.VISIBLE);
                holder.rel_del.setVisibility(View.GONE);
            }
            if (report.equalsIgnoreCase("My_IReport")){
                holder.rel_like.setVisibility(View.GONE);
                holder.rel_del.setVisibility(View.VISIBLE);
            }


            if (!allCommunityModel.getMemberImage().equalsIgnoreCase(null)){
                Glide.with(context)
                        .load(member_pic_BaseUrl+allCommunityModel.getMemberImage())
                        // .apply(options)
                        .placeholder(R.drawable.man)
                        .into(holder.img_person);
            }

            if (allCommunityModel.getType().equalsIgnoreCase("Image")){
                if (allCommunityModel.getImage()!=null &&
                        !allCommunityModel.getImage().equalsIgnoreCase("")){
                    holder.post_image.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(I_Report_Image_BaseUrl+allCommunityModel.getImage())
                            // .apply(options)
                            .centerCrop()
                           // .placeholder(R.drawable.man)
                            .into(holder.post_image);


                }

            }else if (allCommunityModel.getType().equalsIgnoreCase("Video")){

                if (allCommunityModel.getVideo()!=null &&
                        !allCommunityModel.getVideo().equalsIgnoreCase("")) {
                    holder.play_image.setVisibility(View.VISIBLE);
                    holder.post_image.setVisibility(View.VISIBLE);

                    final String vid_url=I_Report_Video_BaseUrl+allCommunityModel.getVideo();

                    //************find thumbnails image from video url**
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.isMemoryCacheable();
                    Glide.with(context).setDefaultRequestOptions(requestOptions)
                            .load(vid_url)
                            .into(holder.post_image);
                }

            }else {
                holder.video_view.setVisibility(View.GONE);
                holder.post_image.setVisibility(View.GONE);
                holder.play_image.setVisibility(View.GONE);
            }

        }

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String i_post_id=allCommunityModels.get(position).getReport_id();
                String url=BaseUrl+i_report_like;

                LikePost(url,i_post_id,holder.badge_count_like,allCommunityModels.get(position));
            }
        });

        holder.ll_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, I_report_details_Activity.class);
                intent.putExtra("i_report_id", allCommunityModels.get(position).getReport_id());
                intent.putExtra("i_report_type", allCommunityModels.get(position).getType());
                context.startActivity(intent);

            }
        });

        holder.rel_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PostId=allCommunityModels.get(position).getReport_id();
                MyIReportDelete(PostId,allCommunityModels.get(position),position);//rxjava with android networking use default responce


            }
        });

        //here play
//        holder.post_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (allCommunityModels.get(position).getType()
//                        .equalsIgnoreCase("Video")){
//
//                    if (allCommunityModels.get(position).getVideo()!=null &&
//                            !allCommunityModels.get(position).getVideo().equalsIgnoreCase("")){
//
//                        holder.post_image.setVisibility(View.GONE);
//                        holder.play_image.setVisibility(View.GONE);
//                        holder.video_view.setVisibility(View.VISIBLE);
//
//                        Log.e("video_url", I_Report_Video_BaseUrl+allCommunityModels.get(position).getVideo());
//                        MediaController vidControl = new MediaController(context);
//                        String vidAddress = I_Report_Video_BaseUrl+allCommunityModels.get(position).getVideo();
//                        Uri vidUri = Uri.parse(vidAddress);
//                        holder.video_view.setVideoURI(vidUri);
//                        holder.video_view.start();
//                        vidControl.setAnchorView(holder.video_view);
//                        holder.video_view.setMediaController(vidControl);
//
//                    }else {
//                        Toast.makeText(context, "No video found", Toast.LENGTH_SHORT).show();
//                    }
//
//                }else {
//                    Intent intent=new Intent(context, I_report_details_Activity.class);
//                    intent.putExtra("i_report_id", allCommunityModels.get(position).getReport_id());
//                    context.startActivity(intent);
//                }
//
//            }
//        });

    }

    private void MyIReportDelete(String postId, I_ReportModel_Details i_reportModel_details, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+delete_i_report)
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
                                allCommunityModels.remove(i_reportModel_details);
                                notifyDataSetChanged();
                            }else {
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });

    }

    private void LikePost(String url, String i_post_id, final TextView badge_count_like, final I_ReportModel_Details i_reportModel_details) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        //String url=BaseUrl+get_events;
        AndroidNetworking.post(url)
                .addBodyParameter("report_id", i_post_id)
                .addBodyParameter("member_id",Shared_Preference.getUser_Id(context) )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");
                            String total_like = jsonObject.getString("total_like");

                            if (result.equalsIgnoreCase("true")) {
                                i_reportModel_details.setTotalLike(total_like);
                                badge_count_like.setText(i_reportModel_details.getTotalLike());

                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();


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

    }




}
