package com.logical.communityapp.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.logical.communityapp.Activity.Maintenance_Request.My_Service_RequestActivity;
import com.logical.communityapp.Activity.Maintenance_Request.Post_NewService_Request;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.adapter.MaintanceAdapter;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.maintance_model.Main_category_model;
import com.logical.communityapp.model_class.maintance_model.Main_category_modelData;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestData;
import com.logical.communityapp.model_class.maintance_model.MaintanceRequestModel;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.rxandroidnetworking.RxAndroidNetworking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_maintenace_category;


public class Maintain_fragment extends Fragment {

    RecyclerView recycler_maintance;
    MaintanceAdapter maintanceAdapter;
    TextView tv_my_request;
    FloatingActionButton fiter_maintanence;
    private Dialog Hoadialog;
   Spinner  spin_cate;
    List<MaintanceRequestData> maintanceRequestDataArrayList=new ArrayList<>();

    HashMap<Integer, Main_category_modelData> CategoryHashMap=new HashMap<Integer, Main_category_modelData>();
    private String Category_ID;

    public Maintain_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_maintain_fragment, container, false);
        getActivity().setTitle("Maintenance request");
        setHasOptionsMenu(true);
        //initialize must be important
       // AndroidNetworking.initialize(getApplicationContext());
        tv_my_request=root.findViewById(R.id.tv_my_request);
        recycler_maintance=root.findViewById(R.id.recycler_maintance);
        fiter_maintanence=root.findViewById(R.id.fiter_maintanence);

        if (Conectivity.isConnected(getActivity())){
            getMaintanceRequest("");//get maintanance all request post

        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }


    tv_my_request.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //see myself request post
            Intent intent=new Intent(getActivity(), My_Service_RequestActivity.class);
            startActivity(intent);

        }
    });

        fiter_maintanence.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OpenfilterDialog();

        }
    });



        return root;
    }

    private void OpenfilterDialog() {

        Hoadialog = new Dialog(getActivity());
        Hoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Hoadialog.setCancelable(true);
        Hoadialog.setContentView(R.layout.filter_mainten_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout ll_next=Hoadialog.findViewById(R.id.ll_next);
        spin_cate=Hoadialog.findViewById(R.id.spin_cate);

        GetPostCategory();

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


        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Conectivity.isConnected(getActivity())){
                    Hoadialog.dismiss();
                    getMaintanceRequest(Category_ID);

                }else {
                    Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
                }




            }
        });


        try {
            if (!getActivity().isFinishing()){
                Hoadialog.show();
            }
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            spin_cate.setAdapter(dataAdapter);

                        }catch (Exception e){

                        }

                    }
                });

    }

    @SuppressLint("CheckResult")
    private void getMaintanceRequest(String category_ID) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<MaintanceRequestModel> call = apiInterface.GetMaintanceRequest(category_ID, Shared_Preference.getUser_Id(getActivity()));

        call.enqueue(new Callback<MaintanceRequestModel>() {
            @Override
            public void onResponse(Call<MaintanceRequestModel> call, Response<MaintanceRequestModel> response) {

                try{
                    maintanceRequestDataArrayList.clear();
                    if (response!=null){
                        Log.e("error_Dr_login",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){

                            maintanceRequestDataArrayList=(response.body().getData());
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            maintanceAdapter = new MaintanceAdapter(maintanceRequestDataArrayList, getActivity(),"OtherPost");
                            recycler_maintance.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recycler_maintance.setItemAnimator(new DefaultItemAnimator());
                            recycler_maintance.setAdapter(maintanceAdapter);

                        }else {

                            Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                        }

                        maintanceAdapter.notifyDataSetChanged();

                    }
                }catch (Exception e){
                    Log.e("error_Dr_login", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MaintanceRequestModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_Dr_login",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

        getMaintanceRequest("");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);
        //View v = (View) menu.findItem(R.id.action_add).getActionView();
        MenuItem iv_add = menu.findItem(R.id.action_i_post);

        iv_add.setVisible(true);

        iv_add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //new request post
                Intent intent=new Intent(getActivity(), Post_NewService_Request.class);
                intent.putExtra("Services", "Post New Service");
                startActivity(intent);

                return true;
            }
        });
    }
}
