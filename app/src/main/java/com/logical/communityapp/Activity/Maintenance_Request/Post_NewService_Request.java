package com.logical.communityapp.Activity.Maintenance_Request;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Activity.MarketPlace.UploadProductActivity;
import com.logical.communityapp.Fragment.Maintain_fragment;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.Main_category_modelData;
import com.logical.communityapp.model_class.maintance_model.Main_currency_model;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_currency_sysmbols;
import static com.logical.communityapp.Api_Url.Base_Url.get_maintenace_category;
import static com.logical.communityapp.Api_Url.Base_Url.post_maintenance_request;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class Post_NewService_Request extends AppCompatActivity {

    ImageView iv_back,iv_post;
    private File img_file;
    private boolean isPermitted;
    TextView tv_date,tv_header;
    EditText et_desc,et_title,et_budget;
    Button community_maint_post;
    Spinner spin_cate,spin_currency;
    HashMap<Integer, Main_category_modelData> CategoryHashMap=new HashMap<Integer, Main_category_modelData>();
    private String Category_ID="";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__new_service__request);

        //initialize must be important
        AndroidNetworking.initialize(getApplicationContext());

        iv_back=findViewById(R.id.iv_back);
        iv_post=findViewById(R.id.iv_post);
        tv_date=findViewById(R.id.tv_date);
        et_desc=findViewById(R.id.et_desc);
        et_title=findViewById(R.id.et_title);
        et_budget=findViewById(R.id.et_budget);
        tv_header=findViewById(R.id.tv_header);
        community_maint_post=findViewById(R.id.community_maint_post);
        spin_cate=findViewById(R.id.spin_cate);
        spin_currency=findViewById(R.id.spin_currency);

        //getIntent
        try {
          String Services_title=  getIntent().getStringExtra("Services");
            tv_header.setText(Services_title);
        }catch (Exception e){

        }

        //get category  and currency from server
        if (Conectivity.isConnected(Post_NewService_Request.this)){
            GetPostCategory();//using rxjava with android networking- own JAVA Object-JSON Parser
            GetPostCurrency();//using rxjava with android networking
        }else {
            Toast.makeText(Post_NewService_Request.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        //back press imageview
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //post image show choose camera gallery
        iv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission must be granted
                checkRunTimePermission();
            }
        });

        //select date from date picker
        tv_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //date picker dialog
                SelectDate();
                return false;
            }
        });

        //spinner category select
        spin_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

                    if (CategoryHashMap.get(position).getName().equals(selectedItem))
                    {
                       Category_ID=CategoryHashMap.get(position).getId();
                        Log.e("cat_id", Category_ID);
                    }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //community post button click
        community_maint_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title=et_title.getText().toString();
                String Description=et_desc.getText().toString();
                String Tv_Date=tv_date.getText().toString();
                String Et_budget=et_budget.getText().toString();
                String Spin_Currency=spin_currency.getSelectedItem().toString();

                //validate
                if (!Title.isEmpty() && !Description.isEmpty() && !Tv_Date.isEmpty() && !Et_budget.isEmpty()){

                    if (!Category_ID.equalsIgnoreCase("")){
                        if (Conectivity.isConnected(Post_NewService_Request.this)){
                            PostNewMaint(Title,Description,Tv_Date,Et_budget,Spin_Currency,Category_ID);//using rxjava with android networking
                        }else {
                            Toast.makeText(Post_NewService_Request.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(Post_NewService_Request.this, "Please select category", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Post_NewService_Request.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void GetPostCurrency() {
        RxAndroidNetworking.get(BaseUrl+get_currency_sysmbols)
                //.addPathParameter("userId", "1")
                .build()
                .getObjectObservable(Main_currency_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Main_currency_model>() {
                    @Override
                    public void onCompleted() {
                        // do anything onComplete
                    }
                    @Override
                    public void onError(Throwable e) {
                        // handle error
                    }
                    @Override
                    public void onNext(Main_currency_model user) {
                        // do anything with response

                        try {
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cur_res" , ""+user.getData().size());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getCode());
                            }


                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Post_NewService_Request.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            spin_currency.setAdapter(dataAdapter);

                            dataAdapter=(ArrayAdapter)spin_currency.getAdapter();
                            //ArrayAdapter myAdap = (ArrayAdapter) mySpinner.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition = dataAdapter.getPosition("NGN");
                            spin_currency.setSelection(spinnerPosition);

                        }catch (Exception e){

                        }
                    }
                });
    }

    private void GetPostCategory() {
        RxAndroidNetworking.get(BaseUrl+get_maintenace_category)
                //.addPathParameter("userId", "1")
                .build()
                .getObjectObservable(Main_category_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Main_category_model>() {
                    @Override
                    public void onCompleted() {
                        // do anything onComplete
                    }
                    @Override
                    public void onError(Throwable e) {
                        // handle error
                    }
                    @Override
                    public void onNext(Main_category_model user) {
                        // do anything with response
                        try {
                           // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cate_res1" , user.getResult());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getName());
                                CategoryHashMap.put(i, new Main_category_modelData(user.getData().get(i).getId(), user.getData().get(i).getName()));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Post_NewService_Request.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            spin_cate.setAdapter(dataAdapter);

                        }catch (Exception e){

                        }

                    }
                });
    }

    //select date
    private void SelectDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog mDatePicker = new DatePickerDialog(Post_NewService_Request.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy/MM/dd"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                tv_date.setText(sdf.format(myCalendar.getTime()));

                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;
            }
        }, mYear[0], mMonth[0], mDay[0]);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void PostNewMaint(String title, String description, String tv_Date, String et_budget, String spin_Currency, String category_ID) {
        final ProgressDialog progressDialog = new ProgressDialog(Post_NewService_Request.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        RxAndroidNetworking.upload(BaseUrl+post_maintenance_request)
                .addMultipartParameter("member_id",Shared_Preference.getUser_Id(Post_NewService_Request.this))
                .addMultipartParameter("title_of_request", title)
                .addMultipartParameter("description", description)
                .addMultipartParameter("date", tv_Date)
                .addMultipartParameter("cate_id", category_ID)
                .addMultipartParameter("currency", spin_Currency)
                .addMultipartParameter("budget", et_budget)
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


                            String result=response.getString("result");
                            String msg=response.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(Post_NewService_Request.this, "Maintenance request successfully posted", Toast.LENGTH_SHORT).show();

                                finish();
                                Intent intent=new Intent(Post_NewService_Request.this, MainActivity.class);
                                intent.putExtra("Maintain", "Maintain");
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(Post_NewService_Request.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }

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
                    iv_post.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    img_file = bitmapToFile(Post_NewService_Request.this, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(Post_NewService_Request.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(Post_NewService_Request.this);
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
                            Toast.makeText(Post_NewService_Request.this, "Permission required", Toast.LENGTH_SHORT).show();
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
