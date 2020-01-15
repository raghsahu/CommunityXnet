package com.logical.communityapp.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.logical.communityapp.adapter.SliderAdapter;
import com.logical.communityapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Welcome_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private int dotsCount;
    private ImageView[] dotes;
    private TextView tv_skip;


    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);


       // CheckPermission();
        viewPager = findViewById(R.id.slider_pager);
        linearLayout = findViewById(R.id.linear_layout);
        tv_skip = findViewById(R.id.tv_skip);
        printHashKey();

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //dotesIndicater(0);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(pageChangeListener);
        dotesIndicater();

    }

    private void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.trivia.lambatriviaapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private void CheckPermission() {

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[7])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[8])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[9])
                            != PackageManager.PERMISSION_GRANTED)
            {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            }

        }catch (Exception e){

        }



    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < dotsCount; i++) {
                dotes[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));
            }

            dotes[position].setImageResource(R.drawable.circle_active);


            if (position + 1 == dotsCount) {
                tv_skip.setText("Get Start");

            } else {
                tv_skip.setText("Skip");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void dotesIndicater() {
        dotsCount = sliderAdapter.getCount();
        dotes = new ImageView[dotsCount];
        linearLayout.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dotes[i] = new ImageView(this);
            dotes[i].setImageResource(R.drawable.circle_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dotes[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            linearLayout.addView(dotes[i], params);

        }
        dotes[0].setImageResource(R.drawable.circle_active);


    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        System.out.println(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[1] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[2] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[3] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[4] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[5] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[6] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[7] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[8] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[9] == PackageManager.PERMISSION_GRANTED);


    }

}
