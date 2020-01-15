package com.logical.communityapp.Activity.News_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.logical.communityapp.model_class.news_model.News_Details_model;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityNewsDetailsBinding;
import com.rxandroidnetworking.RxAndroidNetworking;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.newsImage_URL;
import static com.logical.communityapp.Api_Url.Base_Url.public_news_detail;

public class NewsDetailsActivity extends AppCompatActivity {
    ActivityNewsDetailsBinding activityNewsDetailsBinding;
    String PostId;


    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewsDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_details);
        //setContentView(R.layout.activity_news_details);


        if (getIntent() != null) {//get intent value
            try {
                PostId = getIntent().getStringExtra("PostId");
            } catch (Exception e) {

            }
        }

        //check connectivity internet
        if (Conectivity.isConnected(this)) {
            getNewsDescrtiption();

        } else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        //three dot menu option click
        activityNewsDetailsBinding.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(NewsDetailsActivity.this, activityNewsDetailsBinding.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.report_abuse:
                                //handle menu1 click
                                Toast.makeText(NewsDetailsActivity.this, "Report success", Toast.LENGTH_SHORT).show();

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


    private void getNewsDescrtiption() {
        final ProgressDialog progressDialog = new ProgressDialog(NewsDetailsActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+public_news_detail)
                .addBodyParameter("news_id", PostId)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(NewsDetailsActivity.this))
                .build()
                .getObjectObservable(News_Details_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News_Details_model>() {
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
                    public void onNext(News_Details_model user) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_res1" , user.getResult());

                            if (user.getResult().equalsIgnoreCase("true")){

                                activityNewsDetailsBinding.tvPName.setText(user.getData().getMemberName());
                                activityNewsDetailsBinding.tvPEmail.setText(user.getData().getMemberEmail());
                                activityNewsDetailsBinding.tvDate.setText(user.getData().getDateTime());
                                activityNewsDetailsBinding.tvPostText.setText(user.getData().getTexts());
                                activityNewsDetailsBinding.tvPostDescription.setText(user.getData().getDescription());

                                //set post image
                                if (user.getData().getImage()!=null && !user.getData().getImage().isEmpty()){
                                    activityNewsDetailsBinding.postImage.setVisibility(View.VISIBLE);
                                    Glide.with(NewsDetailsActivity.this)
                                            .load(newsImage_URL+user.getData().getImage())
                                            .placeholder(R.drawable.capture_img)
                                            .into( activityNewsDetailsBinding.postImage);
                                }else {
                                    activityNewsDetailsBinding.postImage.setVisibility(View.GONE);
                                }

                                // set user profile image
                                if (user.getData().getMemberImage()!=null && !user.getData().getMemberImage().isEmpty()){
                                    Glide.with(NewsDetailsActivity.this)
                                            .load(member_pic_BaseUrl+user.getData().getMemberImage())
                                            // .apply(options)
                                            .placeholder(R.drawable.man)
                                            .into(activityNewsDetailsBinding.imgPerson);
                                }

                            }else {
                                Toast.makeText(NewsDetailsActivity.this, ""+user.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
