package com.logical.communityapp.Activity.I_Report;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.i_report_model.I_report_post_model;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.post_i_report;

public class I_Report_postActivity extends AppCompatActivity {

    ImageView iv_camara,iv_video,iv_image_show,iv_back;
    VideoView iv_video_show;

    //***********************************************************
    private final String TAG = getClass().getSimpleName();

    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/Community";
    File imgFile;
    private boolean isPermitted;

    EditText et_comment;
    ImageView iv_Send;
    private Uri uriPhoto;
   String path;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__report_post);

        iv_video=findViewById(R.id.iv_video);
        iv_camara=findViewById(R.id.iv_camara);
        iv_image_show=findViewById(R.id.iv_image_show);
        iv_video_show=findViewById(R.id.iv_video_show);
        et_comment=findViewById(R.id.et_comment);
        iv_Send=findViewById(R.id.iv_Send);
        iv_back=findViewById(R.id.iv_back);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();
            }
        });

        iv_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Et_Comment=et_comment.getText().toString();

                //UploadMultipart(Et_Comment); //***retrofit

               Uploadmulti(Et_Comment);//*****upload library



            }
        });

    }


    private void Uploadmulti(String et_comment) {


        //getting the actual path of the image
       // String path = getPath(uriPhoto);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, BaseUrl+ post_i_report)
                   // .addFileToUpload(path, "image") //Adding file
                    .addParameter("texts", et_comment) //Adding text parameter to the request
                    .addParameter("member_id", "20")
                    .addParameter("video", "")
                    .addParameter("type", "Video")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Log.e("upload_fi", exc.getMessage());
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void UploadMultipart(String et_comment) {
        final ProgressDialog progressDialog = new ProgressDialog(I_Report_postActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);
        String user_id=Shared_Preference.getUser_Id(I_Report_postActivity.this);
//        //Create a file object using file path
//        File file = new File(sourceFile.getPath());
//        // Create a request body with file and image media type
//        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
//        // Create MultipartBody.Part using file request-body,file name and part name
//        MultipartBody.Part Img_part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
//        //Create request body with text description and text media type
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        File imgFile1 = new File(path);
        RequestBody   fileBody = RequestBody.create(MediaType.parse("image/*"), imgFile1);
       // RequestBody   fileBody1 = RequestBody.create(MediaType.parse("image/*"), imgFile);

        MultipartBody.Part Img_part = MultipartBody.Part.createFormData("image", imgFile1.getName(),fileBody);
      //  MultipartBody.Part body1 = MultipartBody.Part.createFormData("sign_image", imgFile.getName(),fileBody1);

        RequestBody et_comment1 = RequestBody.create(MediaType.parse("text/plain"), et_comment);
        RequestBody user_id1 = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "Image");
        RequestBody video = RequestBody.create(MediaType.parse("text/plain"), "");


        Call<I_report_post_model> call = apiInterface.post_irepot(user_id1,et_comment1,type,Img_part,video);

        call.enqueue(new Callback<I_report_post_model>() {
            @Override
            public void onResponse(Call<I_report_post_model> call, Response<I_report_post_model> response) {

                try{
                    if (response!=null){
                        Log.e("post_ireport",""+response.body().getResult());

                        if (response.body().getResult().equalsIgnoreCase("true")){
                            Toast.makeText(I_Report_postActivity.this, "Successfull", Toast.LENGTH_SHORT).show();


                        }else {
                            Toast.makeText(I_Report_postActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_ireport", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<I_report_post_model> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_report",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }




    //*******request permission
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
                            Toast.makeText(I_Report_postActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
                // getNumber(getActivity().getContentResolver());
            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                imgFile=  new File(getPath(contentURI));

                Log.e("img_file", imgFile.getPath());

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);
                    Toast.makeText(I_Report_postActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    iv_image_show.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(I_Report_postActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            imgFile=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_image_show.setImageBitmap(thumbnail);
            path= saveImage(thumbnail);
            Toast.makeText(I_Report_postActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private String getPath(Uri path) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(path, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clean up!
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
