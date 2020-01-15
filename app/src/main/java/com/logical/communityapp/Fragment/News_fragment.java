package com.logical.communityapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.logical.communityapp.Activity.News_Menu.MyNewsPostActivity;
import com.logical.communityapp.Activity.News_Menu.PostNewsActivity;
import com.logical.communityapp.adapter.NewsAdapter;
import com.logical.communityapp.model_class.news_model.News_model;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_public_news;

public class News_fragment extends Fragment {

    RecyclerView recycler_news;
    NewsAdapter newsAdapter;
    TextView tv_my_post;

    public News_fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);

        //View v = (View) menu.findItem(R.id.action_add).getActionView();
        MenuItem iv_add = menu.findItem(R.id.action_add);

        iv_add.setVisible(true);

        iv_add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent=new Intent(getActivity(), PostNewsActivity.class);
                startActivity(intent);
                //getActivity().finish();

                return true;
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_news_fragment, container, false);
        //initialize must be important
        AndroidNetworking.initialize(getActivity());
        getActivity().setTitle("News");
        setHasOptionsMenu(true);

        recycler_news=root.findViewById(R.id.recycler_news);
        tv_my_post=root.findViewById(R.id.tv_my_post);

        if (Conectivity.isConnected(getActivity())){

            getPublicNews();
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        tv_my_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), MyNewsPostActivity.class);
                startActivity(intent);

            }
        });


    return root;
    }

    private void getPublicNews() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.post(BaseUrl+get_public_news)
                //.addBodyParameter("member_id", Shared_Preference.getUser_Id(getActivity()))
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

                                newsAdapter = new NewsAdapter(user.getData(), getActivity(),"PublicNews");
                                recycler_news.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                recycler_news.setItemAnimator(new DefaultItemAnimator());
                                recycler_news.setAdapter(newsAdapter);
                                newsAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(getActivity(), ""+user.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){

                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Conectivity.isConnected(getActivity())){

            getPublicNews();
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
