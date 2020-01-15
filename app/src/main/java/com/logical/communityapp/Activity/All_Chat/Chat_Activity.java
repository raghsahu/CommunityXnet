package com.logical.communityapp.Activity.All_Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.logical.communityapp.R;

public class Chat_Activity extends AppCompatActivity {

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

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
