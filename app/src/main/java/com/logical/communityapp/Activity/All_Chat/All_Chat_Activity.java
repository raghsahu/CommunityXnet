package com.logical.communityapp.Activity.All_Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.logical.communityapp.R;
import com.logical.communityapp.adapter.chat_adapter.Chat_tab_adapter;

public class All_Chat_Activity extends AppCompatActivity {

    ImageView iv_back;
    TabLayout tablayout;
    private ViewPager viewPager;
    Chat_tab_adapter chat_tab_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        iv_back=findViewById(R.id.iv_back);
        tablayout=findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        chat_tab_adapter = new Chat_tab_adapter(this.getSupportFragmentManager(), tablayout.getTabCount(),this);
        viewPager.setAdapter(chat_tab_adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));


        tablayout.setupWithViewPager(viewPager);
        tablayout.addTab(tablayout.newTab().setText("All"));
        tablayout.addTab(tablayout.newTab().setText("Buying"));
        tablayout.addTab(tablayout.newTab().setText("Selling"));

        //*******************************************
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * on swipe select the respective tab
             * */
            @Override
            public void onPageSelected(int position) {
                //actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });

        //*******************************************
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                 viewPager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
               // String TipsId = TipsTablist.get(position).getId();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
