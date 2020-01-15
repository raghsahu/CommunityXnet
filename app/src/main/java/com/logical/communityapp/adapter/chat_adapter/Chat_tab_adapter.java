package com.logical.communityapp.adapter.chat_adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.logical.communityapp.Activity.All_Chat.All_Chat_Activity;
import com.logical.communityapp.Fragment.All_Chat_fragment;

public class Chat_tab_adapter extends FragmentPagerAdapter {

    Context myContext;
    int totalTabs;

    public Chat_tab_adapter(FragmentManager fm, int totalTabs, All_Chat_Activity allChatActivity) {
        super(fm);
        myContext = allChatActivity;
        this.totalTabs = totalTabs;
    }



    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                All_Chat_fragment playFragment = new All_Chat_fragment();
                return playFragment;
            case 1:
                All_Chat_fragment buyFragment = new All_Chat_fragment();
                return buyFragment;
            case 2:
                All_Chat_fragment sellFragment = new All_Chat_fragment();
                return sellFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

}
