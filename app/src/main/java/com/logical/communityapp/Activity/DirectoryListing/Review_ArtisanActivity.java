package com.logical.communityapp.Activity.DirectoryListing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.logical.communityapp.R;

public class Review_ArtisanActivity extends AppCompatActivity {

    ImageView iv_rating,iv_back;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__artisan);
        context=Review_ArtisanActivity.this;
        iv_rating=findViewById(R.id.iv_rating);
        iv_back=findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        iv_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rating_Dialog();

            }
        });


    }

    private void Rating_Dialog() {
        final Dialog Ratingdialog = new Dialog(context);
        Ratingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Ratingdialog.setCancelable(true);
        Ratingdialog.setContentView(R.layout.artisan_rating_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btn_submit=Ratingdialog.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ratingdialog.dismiss();

            }
        });


        try {
            Ratingdialog.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
