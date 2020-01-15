package com.logical.communityapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.logical.communityapp.Activity.Events_Menu.AddEventActivity;
import com.logical.communityapp.Activity.Events_Menu.Event_List_Activity;
import com.logical.communityapp.Activity.I_Report.I_Report_postNewActivity;
import com.logical.communityapp.Activity.Maintenance_Request.Post_NewService_Request;
import com.logical.communityapp.Activity.MarketPlace.MyMarketPlace;
import com.logical.communityapp.Activity.MarketPlace.UploadProductActivity;
import com.logical.communityapp.Activity.News_Menu.PostNewsActivity;
import com.logical.communityapp.R;
import com.logical.communityapp.databinding.PostFragmentBinding;

/**
 * Created by Raghvendra Sahu on 27/12/2019.
 */
public class Post_fragment extends Fragment {

    PostFragmentBinding binding;

    public Post_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.post_fragment, container, false);
        View root = binding.getRoot();

        getActivity().setTitle("Post");

        binding.cardAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        binding.cardPostMaintService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Post_NewService_Request.class);
                intent.putExtra("Services", "Post New Service");
                startActivity(intent);
            }
        });

        binding.cardIReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), I_Report_postNewActivity.class);
                startActivity(intent);
            }
        });

        binding.cardUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UploadProductActivity.class);
                startActivity(intent);
            }
        });

        binding.cardPostNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PostNewsActivity.class);
                startActivity(intent);
            }
        });



        return  root;
    }

}
