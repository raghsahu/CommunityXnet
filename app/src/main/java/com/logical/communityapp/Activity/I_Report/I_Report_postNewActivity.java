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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Utilities;
import com.logical.communityapp.videocompression.MediaController;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.post_i_report;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class I_Report_postNewActivity extends AppCompatActivity {

    ImageView iv_camara,iv_image_show,iv_Send,iv_video,iv_back;
    private int GALLERY = 1, CAMERA = 2,SELECT_VIDEO=3;
    private static final String IMAGE_DIRECTORY = "/Community";
    String path;
    private boolean isPermitted;
    EditText et_comment;
    String VideoPath;
    private String type;
    private String Et_comment;
    String filemanagerstring;
    File destination, Videofile,img_file,CompressVideoFile;
    String imageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__report_post);

        iv_camara=findViewById(R.id.iv_camara);
        iv_image_show=findViewById(R.id.iv_image_show);
        iv_Send=findViewById(R.id.iv_Send);
        et_comment=findViewById(R.id.et_comment);
        iv_video=findViewById(R.id.iv_video);
        iv_back=findViewById(R.id.iv_back);

        iv_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               type="Image";
             checkRunTimePermission(type);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });

        iv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 type="Video";
            checkRunTimePermission(type);
            }
        });

        iv_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Et_comment=et_comment.getText().toString();

                if (CompressVideoFile==null && img_file==null){

                    if (Et_comment.isEmpty()){
                        Toast.makeText(I_Report_postNewActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                    }else {
                        Post_i_reportMulti_Text();//*****remove video and image parameter

                    }

                }else {
                    if (type.equalsIgnoreCase("Video")){
                        Post_i_reportMulti();//***android networking
                        //new UploadExecuteTask().execute();//**async task
                    }else {
                        Post_i_reportMulti_Img();//*****remove video parameter
                    }

                }




            }
        });

    }

    private void Post_i_reportMulti_Text() {
        final ProgressDialog progressDialog = new ProgressDialog(I_Report_postNewActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+post_i_report;
        //String url=BaseUrl+create_posts;
        AndroidNetworking.post(url)
                .addBodyParameter("texts",Et_comment)
                // .addMultipartFile("video", Videofile)
                .addBodyParameter("type","" )
               // .addMultipartFile("image", img_file)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(I_Report_postNewActivity.this) )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Log.e("update_pro",""+result.toString());
                            Log.e("update_res",""+jsonObject.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(I_Report_postNewActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                onBackPressed();

                            } else {

                                Toast.makeText(I_Report_postNewActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("update_ex",""+e.toString());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("update_fail",""+anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void Post_i_reportMulti_Img() {
        final ProgressDialog progressDialog = new ProgressDialog(I_Report_postNewActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+post_i_report;
        //String url=BaseUrl+create_posts;
        AndroidNetworking.upload(url)
                .addMultipartParameter("texts",Et_comment)
               // .addMultipartFile("video", Videofile)
                .addMultipartParameter("type",type )
                .addMultipartFile("image", img_file)
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(I_Report_postNewActivity.this) )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Log.e("update_pro",""+result.toString());
                            Log.e("update_res",""+jsonObject.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(I_Report_postNewActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                onBackPressed();

                            } else {

                                Toast.makeText(I_Report_postNewActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("update_ex",""+e.toString());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("update_fail",""+anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void Post_i_reportMulti() {

        final ProgressDialog progressDialog = new ProgressDialog(I_Report_postNewActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        String url=BaseUrl+post_i_report;
        //Uploading code
        AndroidNetworking.upload(url)
                .addMultipartParameter("texts",Et_comment)
                .addMultipartFile("video", CompressVideoFile)
                .addMultipartParameter("type",type )
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(I_Report_postNewActivity.this) )
                //.setPriority(Priority.MEDIUM)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Log.e("update_pro",""+result.toString());
                            Log.e("update_res",""+jsonObject.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(I_Report_postNewActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                onBackPressed();

                            } else {

                                Toast.makeText(I_Report_postNewActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("update_ex",""+e.toString());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("update_fail",""+anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void checkRunTimePermission(String type) {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            if (type.equalsIgnoreCase("Image")){
                selectImage();
            }else {
                selectVideo();
            }
       
        }
    }

    private void selectVideo() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_VIDEO);
    }

    private void selectImage() {
       // showPictureDialog();

        imagePick();
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
                    iv_image_show.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    img_file = bitmapToFile(I_Report_postNewActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(I_Report_postNewActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(I_Report_postNewActivity.this);
    }

    private void showPictureDialog(){
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

//         imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
//        img_file = new File(imageFilePath);
//        Uri imageFileUri = Uri.fromFile(img_file); // convert path to Uri
//
//        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
//        startActivityForResult(it, CAMERA);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
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
                img_file=  new File(getPath(contentURI));

                Log.e("img_file", img_file.getPath());

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                   // path = saveImage(bitmap);
                    //Toast.makeText(EditProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    iv_image_show.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(EditProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            img_file=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_image_show.setImageBitmap(thumbnail);


            // Decode it for real
//            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//            bmpFactoryOptions.inJustDecodeBounds = false;
//
//            //imageFilePath image path which you pass with intent
//            Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);

            // Display it
            //iv_image_show.setImageBitmap(bmp);


        }
        else if (requestCode == SELECT_VIDEO)
        {
            Uri selectedVideoUri = data.getData();
            // OI FILE Manager
            filemanagerstring = selectedVideoUri.getPath();

            // MEDIA GALLERY
            VideoPath = getPath(selectedVideoUri);

            Videofile = new File(getPath(selectedVideoUri));
            Log.e("Videofile ", ""+Videofile);

            //***compress video
            new VideoCompressor().execute();

            if (VideoPath != null) {
                Log.e("video_path", VideoPath);
               // Toast.makeText(this, "video path- "+VideoPath, Toast.LENGTH_SHORT).show();
               // text_video_path.setText( selectedVideoPath);

                getVideoTrumbnails(VideoPath);
            }else {
                Log.e("video_path ", "null");
            }
    }
    }

    private void getVideoTrumbnails(String videoPath) {

        String filePath = videoPath; //change the location of your file!

        Bitmap bmThumbnail;

//MICRO_KIND, size: 96 x 96 thumbnail
//        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
//        iv_image_show.setImageBitmap(bmThumbnail);

// MINI_KIND, size: 512 x 384 thumbnail
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
        iv_image_show.setImageBitmap(bmThumbnail);
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
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
                            Toast.makeText(I_Report_postNewActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
                if (type.equalsIgnoreCase("Image")){
                    selectImage();
                }else {
                    selectVideo();
                }

                // getNumber(getActivity().getContentResolver());
            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class UploadExecuteTask extends AsyncTask<Void, Long, String> {

        String result = "";
        @Override
        protected String doInBackground(Void... params) {
            try {

                Log.d("video_path",""+VideoPath);
                org.apache.http.entity.mime.MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                String id = Shared_Preference.getUser_Id(I_Report_postNewActivity.this);
                if(VideoPath !=null) {
                    entity.addPart("video", new FileBody(new File(VideoPath)));
                }if(destination !=null)
                {
                    entity.addPart("image", new FileBody(destination));
                }

                entity.addPart("type", new StringBody(type));
                entity.addPart("texts", new StringBody(Et_comment));
                entity.addPart("member_id", new StringBody(id));

                result = Utilities.postEntityAndFindJson("http://logicalsofttech.com/communityx/Api/post_i_report", entity);

                return result;
                // }


            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
                Log.e("some_error", e.toString());
            }
            return null;
        }

    @Override
    protected void onPostExecute(String result) {
        // Toast.makeText(MainLocation.this, "res is "+result, Toast.LENGTH_LONG).show();

        if (result != null) {

            Log.e("result_Image", result);
            try {
                JSONObject object = new JSONObject(result);
                String responce = object.getString("result");
                // String img = object.getString("img");
                if (responce.equals("true")) {

                    Toast.makeText(I_Report_postNewActivity.this, "Success", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(I_Report_postNewActivity.this, "Some Problem", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
               // dialog.dismiss();
                Toast.makeText(I_Report_postNewActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        } else {
           // dialog.dismiss();
              Toast.makeText(I_Report_postNewActivity.this, "No Response From Server", Toast.LENGTH_LONG).show();
        }


    }
    }

    @Override
    public void onBackPressed() {
      super.onBackPressed();

//        Intent intent=new Intent(I_Report_postNewActivity.this,MainActivity.class);
//       // intent.putExtra("OnBack_I_Report", "I_Report");
//        startActivity(intent);
//        finish();
    }

    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
       ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
             progressDialog = new ProgressDialog(I_Report_postNewActivity.this,R.style.MyGravity);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(VideoPath);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                progressDialog.dismiss();
                Log.e("Compression", "Compression successfully!");
                Log.e("Compressed File Path", "" + MediaController.cachedFile.getPath());

                CompressVideoFile=new File(MediaController.cachedFile.getPath());

            }else {
                progressDialog.dismiss();
            }

        }
    }
}
