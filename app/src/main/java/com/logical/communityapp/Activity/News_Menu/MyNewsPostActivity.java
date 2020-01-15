package com.logical.communityapp.Activity.News_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.logical.communityapp.adapter.NewsAdapter;
import com.logical.communityapp.model_class.news_model.News_model;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_my_news;

public class MyNewsPostActivity extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView recycler_news;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news_post);
        //initialize must be important
        AndroidNetworking.initialize(getApplicationContext());

        iv_back=findViewById(R.id.iv_back);
        recycler_news=findViewById(R.id.recycler_news);

        //back press imageview
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Conectivity.isConnected(MyNewsPostActivity.this)){

            getPublicNews();
        }else {
            Toast.makeText(MyNewsPostActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void getPublicNews() {
        final ProgressDialog progressDialog = new ProgressDialog(MyNewsPostActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+get_my_news)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(MyNewsPostActivity.this))
                .build()
                .getObjectObservable(News_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News_model>() {
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
                    public void onNext(News_model user) {
                        // do anything with response
                        try {
                            progressDialog.dismiss();
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_my_res1" , user.getResult());

                            if (user.getResult().equalsIgnoreCase("true")){

                                newsAdapter = new NewsAdapter(user.getData(), MyNewsPostActivity.this,"MyNewsPost");
                                recycler_news.setLayoutManager(new LinearLayoutManager(MyNewsPostActivity.this, RecyclerView.VERTICAL, false));
                                recycler_news.setItemAnimator(new DefaultItemAnimator());
                                recycler_news.setAdapter(newsAdapter);
                                newsAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(MyNewsPostActivity.this, ""+user.getMsg(), Toast.LENGTH_SHORT).show();
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
