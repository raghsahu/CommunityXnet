package com.logical.communityapp.Activity.Events_Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityAddEventBinding;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.create_events;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding activityAddEventBinding;
    String Community_Id;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/Community";
    File imgFile;
    String path;
    boolean isPermitted;

    private Calendar calendar;
    private TimePicker timePicker1;
    private TextView time,cancel,ok;
    private String format = "";

    private int mYear, mMonth, mDay, mHour, mMinute;

    //ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddEventBinding= DataBindingUtil.setContentView(this,R.layout.activity_add_event);

        //setContentView(R.layout.activity_add_event);
        //iv_back=findViewById(R.id.iv_back);
        calendar = Calendar.getInstance();

        try {
            if (getIntent()!=null){
                Community_Id=getIntent().getStringExtra("Community_Id");
            }

        }catch (Exception e){

        }

        activityAddEventBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityAddEventBinding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();

            }
        });

        activityAddEventBinding.etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                SelectDate();
                return false;
            }
        });

        activityAddEventBinding.etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //SelectTime();
                settime();
                return false;
            }
        });


        activityAddEventBinding.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Event_Name=activityAddEventBinding.etEventName.getText().toString();
                String Event_About=activityAddEventBinding.etAbout.getText().toString();
                String Event_Location=activityAddEventBinding.etLocation.getText().toString();
                String Event_Venue=activityAddEventBinding.etVenue.getText().toString();
                String Event_Date=activityAddEventBinding.etDate.getText().toString();
                String Event_Time=activityAddEventBinding.etTime.getText().toString();

                if (!Event_Name.isEmpty() && !Event_About.isEmpty() &&
                        !Event_Date.isEmpty() && !Event_Time.isEmpty()){

                    if (Conectivity.isConnected(AddEventActivity.this)){
                        CreateNewEvent(Event_Name,Event_About,Event_Location,Event_Venue,Event_Date,Event_Time);

                    }else {
                        Toast.makeText(AddEventActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddEventActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void settime() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        activityAddEventBinding.etTime.setText(hourOfDay + ":" + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void SelectTime() {

        final Dialog dialog = new Dialog(AddEventActivity.this);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        /*showTime(hour, min);*/

        /* dialog.setTitle("Dialog Title");*/

        dialog.setContentView(R.layout.dialog_view);

        cancel=(TextView)dialog.findViewById(R.id.cancel);
        ok=dialog.findViewById(R.id.set_button);
        timePicker1=dialog.findViewById(R.id.timePicker1);

        /* public void showTime(int hour, int min){*/
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final int finalHour = hour;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //Toast.makeText(AddEventActivity.this,"Timer set sucessfully installed",Toast.LENGTH_LONG).show();
                activityAddEventBinding.etTime.setText(new StringBuilder().append(finalHour).append(" : ").append(min)
                        .append(" ").append(format));

            }
        });

        dialog.show();

    }

    private void SelectDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog mDatePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy/MM/dd"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                activityAddEventBinding.etDate.setText(sdf.format(myCalendar.getTime()));

                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;
            }
        }, mYear[0], mMonth[0], mDay[0]);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

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
        imagePick();

//        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Select photo from gallery",
//                "Capture photo from camera" };
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                choosePhotoFromGallary();
//                                break;
//                            case 1:
//                                takePhotoFromCamera();
//                                break;
//                        }
//                    }
//                });
//        pictureDialog.show();
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
                    activityAddEventBinding.ivImage.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = bitmapToFile(AddEventActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(AddEventActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(AddEventActivity.this);
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
                    Toast.makeText(AddEventActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    activityAddEventBinding.ivImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddEventActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            imgFile=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            activityAddEventBinding.ivImage.setImageBitmap(thumbnail);
            path= saveImage(thumbnail);
            Toast.makeText(AddEventActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    private void CreateNewEvent(String event_name, String event_about, String event_location,
                                String event_venue, String event_date, String event_Time) {

        final ProgressDialog progressDialog = new ProgressDialog(AddEventActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+create_events;
        AndroidNetworking.upload(url)
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(AddEventActivity.this))
               // .addMultipartParameter("community_id",Community_Id)
                .addMultipartParameter("name", event_name )
                .addMultipartParameter("location", event_location )
                .addMultipartParameter("venue",event_venue  )
                .addMultipartParameter("date", event_date )
                .addMultipartParameter("time", event_Time )
                .addMultipartParameter("about", event_about )
                .addMultipartParameter("type", "public" )
                .addMultipartFile("image", imgFile )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("event_details", jsonObject.toString());
                             String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                 Toast.makeText(AddEventActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String id = jsonObject1.getString("id");
                                String member_id = jsonObject1.getString("member_id");
                                String community_id = jsonObject1.getString("community_id");
                                String name = jsonObject1.getString("name");
                                String location = jsonObject1.getString("location");

                                Intent intent=new Intent(AddEventActivity.this, Event_List_Activity.class);
                                intent.putExtra("Community_Id", community_id);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(AddEventActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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

    private String getPath(Uri path) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(path, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
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
                            Toast.makeText(AddEventActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
    }



}
