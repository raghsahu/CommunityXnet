package com.logical.communityapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.adapter.NotificationAdapter;
import com.logical.communityapp.databinding.NotiFragmentBinding;

public class All_Chat_fragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View root =inflater.inflate(R.layout.all_chat_fragment, container, false);
        getActivity().setTitle("Chat");



        if (Conectivity.isConnected(getActivity())){
           // getAllNotification();

        }else {
            Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
        }



        return root;

    }
}
