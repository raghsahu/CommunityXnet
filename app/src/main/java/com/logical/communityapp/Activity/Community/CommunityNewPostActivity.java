package com.logical.communityapp.Activity.Community;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.create_posts;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class CommunityNewPostActivity extends AppCompatActivity {

    ImageView iv_post,iv_back;
    private File imgFile;
    Button Btn_community_news_post;
    EditText et_desc;
    String Community_Id,Join_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_new_post);

        iv_post=findViewById(R.id.iv_post);
        Btn_community_news_post=findViewById(R.id.community_news_post);
        iv_back=findViewById(R.id.iv_back);
        et_desc=findViewById(R.id.et_desc);


        if (getIntent()!=null){
           Community_Id=getIntent().getStringExtra("Community_Id");
            Join_status=getIntent().getStringExtra("Join_status");
        }

        iv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePick();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Btn_community_news_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Et_Desc=et_desc.getText().toString();

                if (!Et_Desc.isEmpty() || imgFile!=null){
                    if (Conectivity.isConnected(CommunityNewPostActivity.this)){
                        Post_Create(Et_Desc);
                    }else {
                        Toast.makeText(CommunityNewPostActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CommunityNewPostActivity.this, "Please enter image or description", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void Post_Create(String et_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(CommunityNewPostActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+create_posts;
        AndroidNetworking.upload(url)
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(CommunityNewPostActivity.this))
                .addMultipartParameter("community_id",Community_Id )
                .addMultipartParameter("description",et_desc )
                .addMultipartFile("image",imgFile )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("Community_post", jsonObject.toString());
                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(CommunityNewPostActivity.this, "Post successfully", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(CommunityNewPostActivity.this,CommunityDetailsActivity.class);
                                intent.putExtra("Community_Id", Community_Id);
                                intent.putExtra("Join_status", Join_status);
                                startActivity(intent);
                                finish();

                            } else {

                                String message = jsonObject.getString("msg");
                                Toast.makeText(CommunityNewPostActivity.this, ""+message, Toast.LENGTH_LONG).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();


                    }
                });


    }

    private void imagePick() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                   iv_post.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = bitmapToFile(CommunityNewPostActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(CommunityNewPostActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(CommunityNewPostActivity.this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
