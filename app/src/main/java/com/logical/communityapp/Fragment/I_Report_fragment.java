package com.logical.communityapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.logical.communityapp.Activity.I_Report.I_Report_postNewActivity;
import com.logical.communityapp.Activity.I_Report.My_I_ReportPostActivity;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.adapter.IReport_get_Adapter;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.i_report_model.I_reportModel;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class I_Report_fragment extends Fragment {
    RecyclerView recycler_i_report_list;
    IReport_get_Adapter iReport_get_adapter;
    TextView tv_my_post;

    public I_Report_fragment() {
        // Required empty public constructor
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

             //   Intent intent=new Intent(getActivity(), I_Report_postActivity.class);
                Intent intent=new Intent(getActivity(), I_Report_postNewActivity.class);
                startActivity(intent);
                //getActivity().finish();

                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root= inflater.inflate(R.layout.fragment_i__report_fragment, container, false);
        getActivity().setTitle("I-Report");
        setHasOptionsMenu(true);

        recycler_i_report_list=root.findViewById(R.id.recycler_i_report_list);
        tv_my_post=root.findViewById(R.id.tv_my_post);

        if (Conectivity.isConnected(getActivity())){
           getI_Report();
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        tv_my_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), My_I_ReportPostActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    private void getI_Report() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<I_reportModel> call = apiInterface.GetI_Report(Shared_Preference.getUser_Id(getActivity()));

        call.enqueue(new Callback<I_reportModel>() {
            @Override
            public void onResponse(Call<I_reportModel> call, Response<I_reportModel> response) {

                try{
                    if (response!=null){
                        Log.e("res_i_report",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();

                            iReport_get_adapter = new IReport_get_Adapter(response.body().getData(), getActivity(),"AllReport");
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recycler_i_report_list.setLayoutManager(mLayoutManger);
                            recycler_i_report_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recycler_i_report_list.setAdapter(iReport_get_adapter);
                            iReport_get_adapter.notifyDataSetChanged();

                        }else {
                            Toast.makeText(getActivity(), ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_i_report", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<I_reportModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_i_report1",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getI_Report();
        Log.e("resume", "resume");
    }
}
