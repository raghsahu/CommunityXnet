package com.logical.communityapp.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.logical.communityapp.Fragment.I_Report_fragment;

public class Community_Tab_adapter extends FragmentPagerAdapter {

    Context myContext;
    int totalTabs;

    public Community_Tab_adapter(FragmentManager fm, int totalTabs) {
        super(fm);
        // myContext = leaguePlaySeeMore;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                I_Report_fragment playFragment = new I_Report_fragment();
                return playFragment;
            case 1:
//                CompletedFragment completedFragment = new CompletedFragment();
//                return completedFragment;
            case 2:
//                ReasultFragment reasultFragment = new ReasultFragment();
//                return reasultFragment;
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
