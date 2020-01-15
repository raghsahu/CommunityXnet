package com.logical.communityapp.Activity.MarketPlace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Activity.Maintenance_Request.Post_NewService_Request;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityUploadProductBinding;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.Main_category_modelData;
import com.logical.communityapp.model_class.maintance_model.Main_currency_model;
import com.rxandroidnetworking.RxAndroidNetworking;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.add_market_place_product;
import static com.logical.communityapp.Api_Url.Base_Url.get_currency_sysmbols;
import static com.logical.communityapp.Api_Url.Base_Url.get_maintenace_category;
import static com.logical.communityapp.Api_Url.Base_Url.get_market_place_category;
import static com.logical.communityapp.Api_Url.Base_Url.post_maintenance_request;
import static com.logical.communityapp.Api_Url.Base_Url.update_market_place_product;
import static com.logical.communityapp.Utils.PathUtils.bitmapToFile;

public class UploadProductActivity extends AppCompatActivity {
    ActivityUploadProductBinding binding;
    HashMap<Integer, Main_category_modelData> CategoryHashMap=new HashMap<Integer, Main_category_modelData>();
    private File img_file;
    private boolean isPermitted;
    private String Category_ID;
     String Update,ProCost,ProName,ProDesc,ProId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_upload_product);
        //setContentView(R.layout.activity_upload_product);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get category  and currency from server
        if (Conectivity.isConnected(UploadProductActivity.this)){
            GetPostCategory();//using rxjava with android networking- own JAVA Object-JSON Parser
            GetPostCurrency();//using rxjava with android networking
        }else {
            Toast.makeText(UploadProductActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        //getIntentvalue
        try {
            if (getIntent()!=null){
                Update=getIntent().getStringExtra("Update");
                ProName=getIntent().getStringExtra("ProName");
                ProDesc=getIntent().getStringExtra("ProDesc");
                ProCost=getIntent().getStringExtra("ProCost");
                ProId=getIntent().getStringExtra("ProId");
            }

            binding.etTitle.setText(ProName);
            binding.etDesc.setText(ProDesc);
            binding.etBudget.setText(ProCost);

            if (Update.equalsIgnoreCase("Update")){
                binding.btnUploadProduct.setText("Update product");
            }

        }catch (Exception e){

        }


        //*************
        //post image show choose camera gallery
        binding.ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission must be granted
                checkRunTimePermission();
            }
        });


        //spinner category select
        binding.spinCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        //product post button click
        binding.btnUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title=binding.etTitle.getText().toString();
                String Description=binding.etDesc.getText().toString();
                String Et_budget=binding.etBudget.getText().toString();
                String Spin_Currency=binding.spinCurrency.getSelectedItem().toString();

                //validate
                if (!Title.isEmpty() && !Description.isEmpty()  && !Et_budget.isEmpty()
                        && !Spin_Currency.isEmpty()){

                    if (!Category_ID.equalsIgnoreCase("")){
                        if (Conectivity.isConnected(UploadProductActivity.this)){
                            if (binding.btnUploadProduct.getText().equals("Update product")){

                             UpdateProduct(Title,Description,Et_budget,Spin_Currency,Category_ID);//using rxjava with android networking
                            }else {
                                PostNewProduct(Title,Description,Et_budget,Spin_Currency,Category_ID);//using rxjava with android networking

                            }
                        }else {
                            Toast.makeText(UploadProductActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(UploadProductActivity.this, "Please select category", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(UploadProductActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        

    }

    private void UpdateProduct(String title, String description, String et_budget, String spin_currency, String category_id) {
        final ProgressDialog progressDialog = new ProgressDialog(UploadProductActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        RxAndroidNetworking.upload(BaseUrl+update_market_place_product)
                .addMultipartParameter("id", ProId)
                .addMultipartParameter("product_name", title)
                .addMultipartParameter("description", description)
                .addMultipartParameter("category_id", category_id)
                .addMultipartParameter("currency", spin_currency)
                .addMultipartParameter("price", et_budget)
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
                                Toast.makeText(UploadProductActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                //  onBackPressed();
                                finish();
//                            Intent intent=new Intent(UploadProductActivity.this, MainActivity.class);
//                            intent.putExtra("Maintain", "Maintain");
//                            startActivity(intent);
//                            finish();

                            }else {
                                Toast.makeText(UploadProductActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){

                        }

                    }
                });



    }

    private void PostNewProduct(String title, String description, String et_budget,
                                String spin_currency, String category_id) {

        final ProgressDialog progressDialog = new ProgressDialog(UploadProductActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        RxAndroidNetworking.upload(BaseUrl+add_market_place_product)
                .addMultipartParameter("member_id", Shared_Preference.getUser_Id(UploadProductActivity.this))
                .addMultipartParameter("product_name", title)
                .addMultipartParameter("description", description)
                .addMultipartParameter("category_id", category_id)
                .addMultipartParameter("currency", spin_currency)
                .addMultipartParameter("price", et_budget)
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
                                Toast.makeText(UploadProductActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                //  onBackPressed();
                                finish();
//                            Intent intent=new Intent(UploadProductActivity.this, MainActivity.class);
//                            intent.putExtra("Maintain", "Maintain");
//                            startActivity(intent);
//                            finish();

                            }else {
                                Toast.makeText(UploadProductActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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
                    binding.ivPost.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    img_file = bitmapToFile(UploadProductActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(UploadProductActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(UploadProductActivity.this);
    }

    private void GetPostCategory() {
        final ProgressDialog progressDialog = new ProgressDialog(UploadProductActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.get(BaseUrl+get_market_place_category)
                //.addPathParameter("userId", "1")
                .build()
                .getObjectObservable(Main_category_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Main_category_model>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                        // do anything onComplete
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        // handle error
                    }
                    @Override
                    public void onNext(Main_category_model user) {
                        progressDialog.dismiss();
                        // do anything with response
                        try {
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cate_res1" , user.getResult());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getName());
                                CategoryHashMap.put(i, new Main_category_modelData(user.getData().get(i).getId(), user.getData().get(i).getName()));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadProductActivity.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            binding.spinCate.setAdapter(dataAdapter);

                        }catch (Exception e){

                        }

                    }
                });
    }

    private void GetPostCurrency() {
        final ProgressDialog progressDialog = new ProgressDialog(UploadProductActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        RxAndroidNetworking.get(BaseUrl+get_currency_sysmbols)
                //.addPathParameter("userId", "1")
                .build()
                .getObjectObservable(Main_currency_model.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Main_currency_model>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                        // do anything onComplete
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        // handle error
                    }
                    @Override
                    public void onNext(Main_currency_model user) {
                        progressDialog.dismiss();
                        // do anything with response

                        try {
                            // Log.e("rx_cate_res" , ""+user.getData().size());
                            Log.e("rx_cur_res" , ""+user.getData().size());
                            List<String> list = new ArrayList<String>();
                            for (int i=0; i<user.getData().size();i++){
                                list.add(user.getData().get(i).getCode());
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadProductActivity.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            binding.spinCurrency.setAdapter(dataAdapter);

                            dataAdapter=(ArrayAdapter)binding.spinCurrency.getAdapter();
                            //ArrayAdapter myAdap = (ArrayAdapter) mySpinner.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition = dataAdapter.getPosition("NGN");
                            binding.spinCurrency.setSelection(spinnerPosition);

                        }catch (Exception e){

                        }
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
                            Toast.makeText(UploadProductActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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


}
