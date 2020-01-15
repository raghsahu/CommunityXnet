package com.logical.communityapp.Activity.Profile_Related;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.city_state_country.CityData;
import com.logical.communityapp.model_class.city_state_country.CityDetailsModel;
import com.logical.communityapp.model_class.city_state_country.CountryData;
import com.logical.communityapp.model_class.city_state_country.CountryDeailsModel;
import com.logical.communityapp.model_class.profile_model.ProfileDeailsModel;
import com.logical.communityapp.model_class.city_state_country.StateData;
import com.logical.communityapp.model_class.city_state_country.StateDeailsModel;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityEditProfileBinding;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.change_member_password;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.update_member_profile;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding activityEditProfileBinding;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/Community";

    ArrayList<String>spin_country=new ArrayList<>();
    ArrayList<String>spin_state=new ArrayList<>();
    ArrayList<String>spin_city=new ArrayList<>();
    public HashMap<Integer, CountryData> CountryHashMap = new HashMap<Integer, CountryData>();
    public HashMap<Integer, StateData> StateHashMap = new HashMap<Integer, StateData>();
    public HashMap<Integer, CityData> CityHashMap = new HashMap<>();
    String CountryId,StateId,CityId,path;
    SessionManager sessionManager;
     File imgFile;
    private boolean isPermitted;
    private Dialog Quitdialog;
    String Gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        // setContentView(R.layout.activity_edit_profile);
        sessionManager=new SessionManager(this);

        if (Conectivity.isConnected(this)){//***check internet conectivity
            getEditProfile();
            getCountry();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        activityEditProfileBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//onback press
                onBackPressed();
            }
        });
        activityEditProfileBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//image profile
                checkRunTimePermission();

            }
        });

        activityEditProfileBinding.spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < CountryHashMap.size(); i++)
                {
                    if (CountryHashMap.get(i).getName().equals( activityEditProfileBinding.spinCountry.getItemAtPosition(position)))
                    {
                      CountryId=CountryHashMap.get(i).getId();

                       getStateList(CountryId);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        activityEditProfileBinding.spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < StateHashMap.size(); i++)
                {
                    if (StateHashMap.get(i).getName().equals( activityEditProfileBinding.spinState.getItemAtPosition(position)))
                    {
                        StateId=StateHashMap.get(i).getId();

                        getCityList(StateId);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        activityEditProfileBinding.spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < CityHashMap.size(); i++)
                {
                    if (CityHashMap.get(i).getName().equals( activityEditProfileBinding.spinCity.getItemAtPosition(position)))
                    {
                        CityId=CityHashMap.get(i).getId();

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //*select gender
        activityEditProfileBinding.radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityEditProfileBinding.radioMale.setChecked(true);
                activityEditProfileBinding.radioFemale.setChecked(false);
                Gender=activityEditProfileBinding.radioMale.getText().toString();

            }
        });

        activityEditProfileBinding.radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityEditProfileBinding.radioMale.setChecked(false);
                activityEditProfileBinding.radioFemale.setChecked(true);
                 Gender=activityEditProfileBinding.radioFemale.getText().toString();

            }
        });


  //**************************submit button click*****
        activityEditProfileBinding.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Et_FirstName=activityEditProfileBinding.etFirstName.getText().toString();
                String Et_LastName=activityEditProfileBinding.etLastName.getText().toString();
                String Et_Mobile=activityEditProfileBinding.etMobile.getText().toString();
                String Et_Address=activityEditProfileBinding.etAddress.getText().toString();
                String Et_Email=activityEditProfileBinding.etEmail.getText().toString();
                String Et_Lga=activityEditProfileBinding.etLga.getText().toString();
                String Et_Occupation=activityEditProfileBinding.etOccupation.getText().toString();

                if (Conectivity.isConnected(EditProfileActivity.this)){
                    UpdateProfile(Et_FirstName,Et_LastName,Et_Mobile,Et_Address,Et_Email,Et_Lga,Et_Occupation,Gender);
                }else {
                    Toast.makeText(EditProfileActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        activityEditProfileBinding.tvChangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwDialog();

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

    private void changePwDialog() {

        Quitdialog = new Dialog(EditProfileActivity.this);
        Quitdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Quitdialog.setCancelable(true);
        Quitdialog.setContentView(R.layout.change_pw_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btn_quit_yes=Quitdialog.findViewById(R.id.btn_update);
        final EditText et_password=Quitdialog.findViewById(R.id.et_password);
        final EditText et_new_password=Quitdialog.findViewById(R.id.et_new_password);
        final EditText et_conf_password=Quitdialog.findViewById(R.id.et_conf_password);


        btn_quit_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Old_pw=et_password.getText().toString();
                String New_pw=et_new_password.getText().toString();
                String Confirm_pw=et_conf_password.getText().toString();

                if (Conectivity.isConnected(EditProfileActivity.this)){
                    UpdatePw(Old_pw,New_pw,Confirm_pw);
                }else {
                    Toast.makeText(EditProfileActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            if (!EditProfileActivity.this.isFinishing()){
                Quitdialog.show();
            }
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }

    private void UpdateProfile(String et_firstName, String et_lastName, String et_mobile,
                               String et_address, String et_email, String et_lga,
                               String et_Occupation, String gender) {

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+update_member_profile;
        AndroidNetworking.upload(url)
                .addMultipartParameter("id",Shared_Preference.getUser_Id(EditProfileActivity.this) )
                .addMultipartParameter("first_name",et_firstName )
                .addMultipartParameter("last_name",et_lastName )
                .addMultipartParameter("email",et_email )
                .addMultipartParameter("street_address",et_address )
                .addMultipartParameter("phone_number",et_mobile )
                .addMultipartParameter("lga",et_lga )
                .addMultipartParameter("city_id",CityId )
                .addMultipartParameter("state_id",StateId )
                .addMultipartParameter("country_id",CountryId )
                .addMultipartParameter("gender",gender )
                .addMultipartParameter("occupation",et_Occupation )
                .addMultipartFile("image",imgFile )

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
                            Log.e("update_message",""+message.toString());
                            Log.e("update_jsonObject",""+jsonObject.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                String id = dataObject.getString("id");
                                String member_id = dataObject.getString("member_id");
                                String first_name = dataObject.getString("first_name");
                                String last_name = dataObject.getString("last_name");
                                String email = dataObject.getString("email");
                                String image = dataObject.getString("image");
                                String phone_number = dataObject.getString("phone_number");
                                String street_address = dataObject.getString("street_address");
                                String lga = dataObject.getString("lga");
                                String city_id = dataObject.getString("city_id");
                                String state_id = dataObject.getString("state_id");
                                String email_status = dataObject.getString("email_status");

                                Shared_Preference.setUser_Id(EditProfileActivity.this, id);
                                Shared_Preference.setName(EditProfileActivity.this, first_name);
                                Shared_Preference.setEmail(EditProfileActivity.this, email);
                                Shared_Preference.setImage(EditProfileActivity.this, image);

                                Intent intent=new Intent(EditProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(EditProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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

    //*********************************
    private void getCityList(String stateId) {

//        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<CityDetailsModel> call = apiInterface.GetAllCity(stateId);

        call.enqueue(new Callback<CityDetailsModel>() {
            @Override
            public void onResponse(Call<CityDetailsModel> call, Response<CityDetailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("e_et_city",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            //   activityEditProfileBinding.etFirstName.setText(response.body().getData().getFirstName());


                            for (int i=0; i<response.body().getData().size();i++){

                                spin_city.add(i,response.body().getData().get(i).getName());
                                CityHashMap.put(i , response.body().getData().get(i));

                            }

                            Log.e("spin_citysize", ""+spin_city.size());
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (EditProfileActivity.this, android.R.layout.simple_spinner_item, spin_city); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityEditProfileBinding.spinCity.setAdapter(spinnerArrayAdapter);


                        }else {
                            // Toast.makeText(EditProfileActivity.this, ""+response.body().getData(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_et_city", e.getMessage());
                }
               // progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CityDetailsModel> call, Throwable t) {
               // progressDialog.dismiss();
                Log.e("error_lcccn",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getStateList(String countryId) {

//        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<StateDeailsModel> call = apiInterface.GetAllState(countryId);

        call.enqueue(new Callback<StateDeailsModel>() {
            @Override
            public void onResponse(Call<StateDeailsModel> call, Response<StateDeailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("e_et_state",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            //   activityEditProfileBinding.etFirstName.setText(response.body().getData().getFirstName());


                            for (int i=0; i<response.body().getData().size();i++){

                                spin_state.add(i,response.body().getData().get(i).getName());
                                StateHashMap.put(i , response.body().getData().get(i));

                            }

                            Log.e("spin_statesize", ""+spin_state.size());
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (EditProfileActivity.this, android.R.layout.simple_spinner_item, spin_state); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityEditProfileBinding.spinState.setAdapter(spinnerArrayAdapter);


                        }else {
                            // Toast.makeText(EditProfileActivity.this, ""+response.body().getData(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_et_country", e.getMessage());
                }
                // progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StateDeailsModel> call, Throwable t) {
                // progressDialog.dismiss();
                Log.e("error_lcccn",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCountry() {
//        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<CountryDeailsModel> call = apiInterface.GetAllCountry();

        call.enqueue(new Callback<CountryDeailsModel>() {
            @Override
            public void onResponse(Call<CountryDeailsModel> call, Response<CountryDeailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("e_et_country",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                         //   activityEditProfileBinding.etFirstName.setText(response.body().getData().getFirstName());


                            for (int i=0; i<response.body().getData().size();i++){

                            spin_country.add(i,response.body().getData().get(i).getName());
                            CountryHashMap.put(i , response.body().getData().get(i));

                            }

                            Log.e("spin_countsize", ""+spin_country.size());
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (EditProfileActivity.this, android.R.layout.simple_spinner_item, spin_country); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityEditProfileBinding.spinCountry.setAdapter(spinnerArrayAdapter);


                        }else {
                           // Toast.makeText(EditProfileActivity.this, ""+response.body().getData(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_et_country", e.getMessage());
                }
              //  progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CountryDeailsModel> call, Throwable t) {
             //   progressDialog.dismiss();
                Log.e("error_lcccn",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void selectImage() {
        //showPictureDialog();

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
                    activityEditProfileBinding.profileImage.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = bitmapToFile(EditProfileActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(EditProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(EditProfileActivity.this);
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
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    private void getEditProfile() {

        Log.e("user_idlog", Shared_Preference.getUser_Id(EditProfileActivity.this));

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<ProfileDeailsModel> call = apiInterface.GetProfileImage(Shared_Preference.getUser_Id(EditProfileActivity.this));

        call.enqueue(new Callback<ProfileDeailsModel>() {
            @Override
            public void onResponse(Call<ProfileDeailsModel> call, Response<ProfileDeailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("error_et_profile",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            activityEditProfileBinding.etFirstName.setText(response.body().getData().getFirstName());
                            activityEditProfileBinding.etLastName.setText(response.body().getData().getLastName());
                            activityEditProfileBinding.etMobile.setText(response.body().getData().getPhoneNumber());
                            activityEditProfileBinding.etEmail.setText(response.body().getData().getEmail());
                            activityEditProfileBinding.etAddress.setText(response.body().getData().getStreetAddress());
                            activityEditProfileBinding.etLga.setText(response.body().getData().getLga());

                            Glide.with(EditProfileActivity.this)
                                    .load(member_pic_BaseUrl+response.body().getData().getImage())
                                    // .apply(options)
                                    .placeholder(R.drawable.man)
                                    .into(activityEditProfileBinding.profileImage);

                        }else {
                            Toast.makeText(EditProfileActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_login", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileDeailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_Dr_login",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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
                    Toast.makeText(EditProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    activityEditProfileBinding.profileImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            imgFile=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            activityEditProfileBinding.profileImage.setImageBitmap(thumbnail);
            path= saveImage(thumbnail);
            Toast.makeText(EditProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
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



    private void UpdatePw(String old_pw, String new_pw, String confirm_pw){
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+change_member_password;
        AndroidNetworking.post(url)
                .addBodyParameter("old_password",old_pw )
                .addBodyParameter("new_password",new_pw )
                .addBodyParameter("confirm_password",confirm_pw )
                .addBodyParameter("id",Shared_Preference.getUser_Id(EditProfileActivity.this) )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        Quitdialog.dismiss();
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Log.e("update_pro",""+result.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                String id = dataObject.getString("id");
//                                String member_id = dataObject.getString("member_id");
//                                String first_name = dataObject.getString("first_name");
//                                String last_name = dataObject.getString("last_name");
//                                String email = dataObject.getString("email");
//                                String image = dataObject.getString("image");
//                                String phone_number = dataObject.getString("phone_number");
//                                String street_address = dataObject.getString("street_address");
//                                String lga = dataObject.getString("lga");
//                                String city_id = dataObject.getString("city_id");
//                                String state_id = dataObject.getString("state_id");
//                                String email_status = dataObject.getString("email_status");

                                // Shared_Preference.setUser_Id(EditProfileActivity.this, id);
//
                                Quitdialog.dismiss();


                            } else {

                                Toast.makeText(EditProfileActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                            Toast.makeText(EditProfileActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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



}
