package com.logical.communityapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.logical.communityapp.Activity.All_Chat.All_Chat_Activity;
import com.logical.communityapp.Activity.Events_Menu.Event_List_Activity;
import com.logical.communityapp.Activity.Login_Signup.SplashActivity;
import com.logical.communityapp.Activity.Profile_Related.ProfileActivity;
import com.logical.communityapp.BuildConfig;
import com.logical.communityapp.Fragment.Community_Stream_fragment;
import com.logical.communityapp.Fragment.MarketPlace_fragment;
import com.logical.communityapp.Fragment.News_fragment;
import com.logical.communityapp.Fragment.Directory_List_fragment;
import com.logical.communityapp.Fragment.Home_fragment;
import com.logical.communityapp.Fragment.I_Report_fragment;
import com.logical.communityapp.Fragment.Maintain_fragment;
import com.logical.communityapp.Fragment.Notification_fragment;
import com.logical.communityapp.Fragment.Pay_Hoa_fragment;
import com.logical.communityapp.Fragment.Post_fragment;
import com.logical.communityapp.Fragment.SupportFragment;
import com.logical.communityapp.Fragment.Vendor_fragment;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout nav_header;
    SessionManager sessionManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment fragment_home=new Home_fragment();
                    FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
                    ft_home.replace(R.id.frame_content, fragment_home);
                    ft_home.addToBackStack(null);
                    ft_home.commit();
                    return true;

                case R.id.navigation_post:
                    Fragment fragment_post=new Post_fragment();
                    FragmentTransaction ft_post = getSupportFragmentManager().beginTransaction();
                    ft_post.replace(R.id.frame_content, fragment_post);
                    ft_post.addToBackStack(null);
                    ft_post.commit();

                    return true;
                case R.id.navigation_notifications:

                    Fragment fragment_notification=new Notification_fragment();
                    FragmentTransaction ft_noti = getSupportFragmentManager().beginTransaction();
                    ft_noti.replace(R.id.frame_content, fragment_notification);
                    ft_noti.addToBackStack(null);
                    ft_noti.commit();

                    return true;

                case R.id.navigation_chat:

//                    Fragment fragment_chat=new All_Chat_fragment();
//                    FragmentTransaction ft_chat = getSupportFragmentManager().beginTransaction();
//                    ft_chat.replace(R.id.frame_content, fragment_chat);
//                    ft_chat.addToBackStack(null);
//                    ft_chat.commit();

                    Intent intent=new Intent(MainActivity.this, All_Chat_Activity.class);
                    startActivity(intent);


                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this);
        AndroidNetworking.initialize(getApplicationContext());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_image);
        //navigationView.getMenu().getItem(5).setActionView(R.layout.menu_image);

        try {
            if (getIntent()!=null){
                String Maintain=getIntent().getStringExtra("Maintain");

                if (Maintain.equalsIgnoreCase("Maintain")){

                    FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.frame_content, new Maintain_fragment());
                    tx.commit();

                }else {
                    FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.frame_content, new Home_fragment());
                    tx.commit();

                }
           }else {

                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame_content, new Home_fragment());
                tx.commit();

            }

        }catch (Exception e){
            Log.e("mainten_error", e.toString());

            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.frame_content, new Home_fragment());
            tx.commit();
        }

        View headerview = navigationView.getHeaderView(0);
        nav_header = headerview.findViewById(R.id.ll_nav_profile);
        CircleImageView nav_imageView = headerview.findViewById(R.id.nav_imageView);
        TextView nav_tv_name = headerview.findViewById(R.id.nav_tv_name);
        TextView textView_email = headerview.findViewById(R.id.textView_email);

        textView_email.setText(Shared_Preference.getEmail(MainActivity.this));
        nav_tv_name.setText(Shared_Preference.getName(MainActivity.this));

        if (!Shared_Preference.getImage(MainActivity.this)
                .equalsIgnoreCase("")){

            Glide.with(this)
                    .load(member_pic_BaseUrl+Shared_Preference.getImage(MainActivity.this))
                   // .apply(options)
                    .placeholder(R.drawable.man)
                    .into(nav_imageView);
        }


        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment_home=new Home_fragment();
            FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
            ft_home.replace(R.id.frame_content, fragment_home);
            ft_home.addToBackStack(null);
            ft_home.commit();

        } else if (id == R.id.nav_pay_hoa) {
            Fragment fragment_hoa=new Pay_Hoa_fragment();
            FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
            ft_home.replace(R.id.frame_content, fragment_hoa);
            ft_home.addToBackStack(null);
            ft_home.commit();

        }

        else if (id == R.id.nav_events) {
//            Fragment fragment_maintain=new Select_community_fragment();
//            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
//            ft_vendor.replace(R.id.frame_content, fragment_maintain);
//            ft_vendor.addToBackStack(null);
//            ft_vendor.commit();

            Intent intent=new Intent(MainActivity.this, Event_List_Activity.class);
           // intent.putExtra("Community_Id", allCommunityModels.get(position).id);
            startActivity(intent);


        } else if (id == R.id.nav_maint_request) {
            Fragment fragment_maintain=new Maintain_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_maintain);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }
//        else if (id == R.id.nav_vault) {
//
//        }
        else if (id == R.id.nav_community_deals) {
            Fragment fragment_home=new Community_Stream_fragment();
            FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
            ft_home.replace(R.id.frame_content, fragment_home);
            ft_home.addToBackStack(null);
            ft_home.commit();

        }  else if (id == R.id.nav_ireport) {

            Fragment fragment_maintain=new I_Report_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_maintain);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }  else if (id == R.id.nav_vendor) {

            Fragment fragment_vendor=new Vendor_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_vendor);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }else if (id == R.id.nav_direct_list) {
            Fragment fragment_direct_list=new Directory_List_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_direct_list);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }else if (id == R.id.nav_marketplace) {
            Fragment fragment_direct_list=new MarketPlace_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_direct_list);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }

        else if (id == R.id.nav_news) {

            Fragment fragment_direct_list=new News_fragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_direct_list);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();

        }else if (id == R.id.nav_share) {
            shareApp();

        }else if (id == R.id.nav_support) {
            Fragment fragment_direct_list=new SupportFragment();
            FragmentTransaction ft_vendor = getSupportFragmentManager().beginTransaction();
            ft_vendor.replace(R.id.frame_content, fragment_direct_list);
            ft_vendor.addToBackStack(null);
            ft_vendor.commit();
        }
        else if (id == R.id.nav_logout) {
            Logout_user();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CommunityXnet");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }

    }

    private void Logout_user() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.app_name)
                .setMessage("Are you sure, you want to logout this app");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                    sessionManager.logoutUser();

                sessionManager.setLogin(false);
                sessionManager.saveToken("");
                Shared_Preference.setUser_Id(MainActivity.this, "");
                Shared_Preference.setName(MainActivity.this, "");
                Shared_Preference.setEmail(MainActivity.this, "");
                Shared_Preference.setImage(MainActivity.this, "");

                Intent intent=new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();

            }

        });
        final AlertDialog alert = dialog.create();
        alert.show();

    }

    //************************************




}
