package com.logical.communityapp.Activity.News_Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityPostNewsBinding;
import com.rxandroidnetworking.RxAndroidNetworking;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.post_news;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class PostNewsActivity extends AppCompatActivity {
    ActivityPostNewsBinding activityPostNewsBinding;
    Context context;
    private File img_file;
    private boolean isPermitted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityPostNewsBinding= DataBindingUtil.setContentView(this,R.layout.activity_post_news);

       context=PostNewsActivity.this;
        //onback pressed icon
        activityPostNewsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //post image show choose camera gallery
        activityPostNewsBinding.ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission must be granted
                checkRunTimePermission();
            }
        });

        activityPostNewsBinding.newsPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String News_Title=activityPostNewsBinding.etTitle.getText().toString();
               String News_Desc=activityPostNewsBinding.etDesc.getText().toString();
                if (!News_Title.isEmpty() && !News_Desc.isEmpty()){
                    if (Conectivity.isConnected(context)){
                        PostNews(News_Title,News_Desc);
                    }else {
                        Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void PostNews(String news_title, String news_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        RxAndroidNetworking.upload(BaseUrl+post_news)
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(context))
                .addMultipartParameter("texts", news_title)
                .addMultipartParameter("description", news_desc)
                .addMultipartFile("image", img_file)
                .build()
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {
                        // do anything onComplete
                        progressDialog.dismiss();
                        Log.e("rx_responce", "completed");
                    }
                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        progressDialog.dismiss();
                        Log.e("rx_error", e.getMessage());
                    }
                    @Override
                    public void onNext(JSONObject response) {
                        //do anything with response
                        try {
                            progressDialog.dismiss();
                            Log.e("rx_next_res", response.toString());
                            Toast.makeText(context, "News successfully posted", Toast.LENGTH_SHORT).show();

                            onBackPressed();

                        }catch (Exception e){

                        }

                    }
                });



    }

    private void checkRunTimePermission() {

        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            selectImage();

        }
    }

    private void selectImage() {

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
                    activityPostNewsBinding.ivPost.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    img_file = bitmapToFile(context, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(PostNewsActivity.this);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(context, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            }catch (Exception e){

            }

            if (isPermitted){
                selectImage();

            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
