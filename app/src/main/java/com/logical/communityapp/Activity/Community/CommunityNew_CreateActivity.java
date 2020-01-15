package com.logical.communityapp.Activity.Community;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.model_class.city_state_country.CityData;
import com.logical.communityapp.model_class.city_state_country.CityDetailsModel;
import com.logical.communityapp.model_class.city_state_country.CountryData;
import com.logical.communityapp.model_class.city_state_country.CountryDeailsModel;
import com.logical.communityapp.model_class.city_state_country.StateData;
import com.logical.communityapp.model_class.city_state_country.StateDeailsModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityCreateCommunityBinding;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.create_community;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class CommunityNew_CreateActivity extends AppCompatActivity {

    ActivityCreateCommunityBinding activityCreateCommunityBinding;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/Community";

    ArrayList<String> spin_country=new ArrayList<>();
    ArrayList<String>spin_state=new ArrayList<>();
    ArrayList<String>spin_city=new ArrayList<>();
    public HashMap<Integer, CountryData> CountryHashMap = new HashMap<Integer, CountryData>();
    public HashMap<Integer, StateData> StateHashMap = new HashMap<Integer, StateData>();
    public HashMap<Integer, CityData> CityHashMap = new HashMap<>();
    String CountryId,StateId,CityId;
    String Live_Check="No";
    String Owner_Check="No";
    String Work_Check="No";
    File imgFile;
    String path;
    ImageView iv_back;
    boolean isPermitted;
    List<AllCommunityModel>allCommunityModelsList=new ArrayList<>();
    ArrayList<String>commu_name=new ArrayList<>();
    AllCommunityModel allCommunityModels;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateCommunityBinding= DataBindingUtil.setContentView(this,R.layout.activity_community_new__create);
       // setContentView(R.layout.activity_community_new__create);


        if (Conectivity.isConnected(this)){
            getCountry();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        try {
            if (getIntent()!=null){

                allCommunityModelsList = getIntent().getParcelableArrayListExtra("AllCommunityModel");

                Log.e("Allcommu_size", ""+allCommunityModelsList.size());
                for (int i=0; i<allCommunityModelsList.size();i++){
                    commu_name.add(allCommunityModelsList.get(i).name);
                }

                Log.e("commu_size", ""+commu_name.size());
                adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, commu_name);
                activityCreateCommunityBinding.etCommuName.setThreshold(1);//will start working from first character
                activityCreateCommunityBinding.etCommuName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                activityCreateCommunityBinding.etCommuName.setTextColor(Color.BLACK);

            }
        }catch (Exception e){

        }



        activityCreateCommunityBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //*********spinner country, state, city
        activityCreateCommunityBinding.spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
//                    if(sectionList.size() !=0)
//                    {
//                        ChooseSection.clear();
//
//                        spin_section.setAdapter(null);
//                        sectionListAdapter.notifyDataSetChanged();
//
//                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < CountryHashMap.size(); i++)
                {
                    if (CountryHashMap.get(i).getName().equals( activityCreateCommunityBinding.spinCountry.getItemAtPosition(position)))
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


        activityCreateCommunityBinding.spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
//                    if(sectionList.size() !=0)
//                    {
//                        ChooseSection.clear();
//
//                        spin_section.setAdapter(null);
//                        sectionListAdapter.notifyDataSetChanged();
//
//                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < StateHashMap.size(); i++)
                {
                    if (StateHashMap.get(i).getName().equals( activityCreateCommunityBinding.spinState.getItemAtPosition(position)))
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

        activityCreateCommunityBinding.spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
//                    if(sectionList.size() !=0)
//                    {
//                        ChooseSection.clear();
//
//                        spin_section.setAdapter(null);
//                        sectionListAdapter.notifyDataSetChanged();
//
//                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < CityHashMap.size(); i++)
                {
                    if (CityHashMap.get(i).getName().equals( activityCreateCommunityBinding.spinCity.getItemAtPosition(position)))
                    {
                        CityId=CityHashMap.get(i).getId();


                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        activityCreateCommunityBinding.ivCommu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();


            }
        });
        //**************************submit button click
        activityCreateCommunityBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Et_CommuName=activityCreateCommunityBinding.etCommuName.getText().toString();
                String Et_Desc=activityCreateCommunityBinding.etCommuDesc.getText().toString();
                String Et_Address=activityCreateCommunityBinding.etAddres.getText().toString();
               // String Et_LGA=activityCreateCommunityBinding.etLga.getText().toString();

                if (activityCreateCommunityBinding.checkLiveCommu.isChecked()){
                    Live_Check="Yes";
                }else {
                    Live_Check="No";
                }

                if (activityCreateCommunityBinding.checkOwnerCommu.isChecked()){
                    Owner_Check="Yes";
                }else {
                    Owner_Check="No";
                }

                if (activityCreateCommunityBinding.checkWorkCommu.isChecked()){
                    Work_Check="Yes";
                }else {
                    Work_Check="No";
                }

                if (Conectivity.isConnected(CommunityNew_CreateActivity.this)){
                    CreateNewCommunity(Et_CommuName,Et_Desc,Et_Address);
                }else {
                    Toast.makeText(CommunityNew_CreateActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
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
       // requestMultiplePermissions();
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
                    activityCreateCommunityBinding.ivCommu.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = bitmapToFile(CommunityNew_CreateActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(CommunityNew_CreateActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(CommunityNew_CreateActivity.this);
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

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
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
                    Toast.makeText(CommunityNew_CreateActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    activityCreateCommunityBinding.ivCommu.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CommunityNew_CreateActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            imgFile=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            activityCreateCommunityBinding.ivCommu.setImageBitmap(thumbnail);
            path= saveImage(thumbnail);
            Toast.makeText(CommunityNew_CreateActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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


    private void CreateNewCommunity(String et_commuName, String et_desc,String et_address) {

        final ProgressDialog progressDialog = new ProgressDialog(CommunityNew_CreateActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+create_community;
        AndroidNetworking.upload(url)
                .addMultipartParameter("member_id",Shared_Preference.getUser_Id(CommunityNew_CreateActivity.this) )
                .addMultipartParameter("name",et_commuName )
                .addMultipartParameter("description",et_desc )
                .addMultipartParameter("address",et_address )
              //  .addMultipartParameter("lga",et_lga )
                .addMultipartParameter("city_id",CityId )
                .addMultipartParameter("state_id",StateId )
                .addMultipartParameter("country_id",CountryId )
                .addMultipartParameter("do_you_live_in_com",Live_Check )
                .addMultipartParameter("do_you_work_in_com",Work_Check )
                .addMultipartParameter("are_you_property_owner", Owner_Check )
                .addMultipartFile("icon",imgFile )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            Log.e("create_commu",""+result.toString());

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(CommunityNew_CreateActivity.this, "Create Successful", Toast.LENGTH_SHORT).show();

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                String id = dataObject.getString("id");
                                String member_id = dataObject.getString("member_id");
                                String community_id = dataObject.getString("community_id");
                                String name = dataObject.getString("name");
                                String icon = dataObject.getString("icon");
                                String description = dataObject.getString("description");
                                String address = dataObject.getString("address");
                               // String lga = dataObject.getString("lga");
                                String city_id = dataObject.getString("city_id");
                                String state_id = dataObject.getString("state_id");
                                String country_id = dataObject.getString("country_id");
                                String do_you_live_in_com = dataObject.getString("do_you_live_in_com");
                                String are_you_property_owner = dataObject.getString("are_you_property_owner");
                                String do_you_work_in_com = dataObject.getString("do_you_work_in_com");
                                String status = dataObject.getString("status");
                                String created_date = dataObject.getString("created_date");

                                // Shared_Preference.setUser_Id(EditProfileActivity.this, id);
                                Intent intent=new Intent(CommunityNew_CreateActivity.this, WaitingApprovalActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(CommunityNew_CreateActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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

    //*****************************************************
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
                                    (CommunityNew_CreateActivity.this, android.R.layout.simple_spinner_item, spin_city); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityCreateCommunityBinding.spinCity.setAdapter(spinnerArrayAdapter);


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
                                    (CommunityNew_CreateActivity.this, android.R.layout.simple_spinner_item, spin_state); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityCreateCommunityBinding.spinState.setAdapter(spinnerArrayAdapter);


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
                                    (CommunityNew_CreateActivity.this, android.R.layout.simple_spinner_item, spin_country); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activityCreateCommunityBinding.spinCountry.setAdapter(spinnerArrayAdapter);



                            spinnerArrayAdapter=(ArrayAdapter)activityCreateCommunityBinding.spinCountry.getAdapter();
                            //ArrayAdapter myAdap = (ArrayAdapter) mySpinner.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition = spinnerArrayAdapter.getPosition("Nigeria");
                            activityCreateCommunityBinding.spinCountry.setSelection(spinnerPosition);

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
                            Toast.makeText(CommunityNew_CreateActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
