package com.logical.communityapp.Activity.HOA_Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.logical.communityapp.R;

public class HOA_Dashboard extends AppCompatActivity {

    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa__dashboard);

        iv_back=findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
