package com.logical.communityapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.communityapp.Activity.News_Menu.NewsDetailsActivity;
import com.logical.communityapp.model_class.news_model.NewsModelData;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONObject;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.delete_my_news;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.newsImage_URL;
import static com.logical.communityapp.Api_Url.Base_Url.report_abouse;

/**
 * Created by Raghvendra Sahu on 13/12/2019.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsModelData> allCommunityModels;
    private Context context;
    private String user_id;
    String NewsType;

    public NewsAdapter(List<NewsModelData> allCommunityModels1, Context context, String newsType) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
        this.NewsType = newsType;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final NewsModelData allCommunityModel=allCommunityModels.get(position);
            holder.tv_name.setText(allCommunityModel.getMemberName());
            holder.tv_date.setText(allCommunityModel.getDateTime());
            holder.tv_news_title.setText(""+allCommunityModel.getTexts());

            if (NewsType.equalsIgnoreCase("MyNewsPost")){
                holder.ll_clickpart.setVisibility(View.VISIBLE);
                holder.rl_delete.setVisibility(View.VISIBLE);
                holder.textViewOptions.setVisibility(View.GONE);
            }

            //set profile image
            if (allCommunityModel.getMemberImage()!=null && !allCommunityModel.getMemberImage().isEmpty()){
                Glide.with(context)
                        .load(member_pic_BaseUrl+allCommunityModel.getMemberImage())
                        // .apply(options)
                        .placeholder(R.drawable.man)
                        .into(holder.img_person);
            }

            //set post image if available
            if (allCommunityModel.getImage()!=null && !allCommunityModel.getImage().isEmpty()){
                Glide.with(context)
                        .load(newsImage_URL+allCommunityModel.getImage())
                        // .apply(options)
                        .placeholder(R.drawable.capture_img)
                        .into(holder.post_image);
            }else {
                holder.post_image.setVisibility(View.GONE);
            }

            holder.rl_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostId=allCommunityModels.get(position).getNewsId();
                    DeleteMyPostNow(PostId,allCommunityModels.get(position),position);//rxjava with android networking use default responce

                }
            });

         holder.ll_details_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostId=allCommunityModels.get(position).getNewsId();
                    Log.e("news_id", PostId);
                    Intent intent=new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("PostId", PostId);
                    context.startActivity(intent);

                }
            });

            //***********************
            holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.report_abuse:
                                    //handle menu1 click
                                    if (Conectivity.isConnected(context)){

                                        ReportAbuse(allCommunityModels.get(position).getNewsId());
                                    }else {
                                        Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                                    }


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

    private void ReportAbuse(String newsId) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+report_abouse)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .addBodyParameter("event_id", newsId)
                .addBodyParameter("type", "News")
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
                            Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();

                            if (result.equalsIgnoreCase("true")){

                            }else {
                              //  Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });

    }

    private void DeleteMyPostNow(String postId, NewsModelData maintanceRequestData, int position) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+delete_my_news)
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

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name,tv_date,tv_news_title,textViewOptions;
        public ImageView img_person,post_image;
        LinearLayout ll_details_news,ll_clickpart;
        RelativeLayout rl_delete;

        public ViewHolder(View parent) {
            super(parent);
            img_person = parent.findViewById(R.id.img_person);
            post_image = parent.findViewById(R.id.post_image);
            tv_name = parent.findViewById(R.id.tv_name);
            tv_date = parent.findViewById(R.id.tv_date);
            tv_news_title = parent.findViewById(R.id.tv_news_title);
           rl_delete = parent.findViewById(R.id.rl_delete);
            ll_details_news = parent.findViewById(R.id.ll_details_news);
            ll_clickpart = parent.findViewById(R.id.ll_clickpart);
            textViewOptions = parent.findViewById(R.id.textViewOptions);

        }

    }
}
