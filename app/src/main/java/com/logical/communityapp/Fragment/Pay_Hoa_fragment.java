package com.logical.communityapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.HOA_Dashboard.HOA_Dashboard;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.hoa_login;
import static com.logical.communityapp.Api_Url.Base_Url.hoa_request;

public class Pay_Hoa_fragment extends Fragment {

    Button btn_send_request;
    TextView tv_login_hoa;
    private Dialog Hoadialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View root = inflater.inflate(R.layout.pay_hoa_fragment, container, false);
        getActivity().setTitle("Join Hoa");

        btn_send_request=root.findViewById(R.id.btn_send_request);
        tv_login_hoa=root.findViewById(R.id.tv_login_hoa);

        btn_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendRequestForJoinHoa();
               // Toast.makeText(getActivity(), "Send request successfully", Toast.LENGTH_SHORT).show();
            }
        });

        tv_login_hoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            HoaLoginDialog();

            }
        });


        return root;

    }

    private void SendRequestForJoinHoa() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+hoa_request;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(getActivity()))

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("hoa_request", jsonObject.toString());
                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(getActivity(), "Send request successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                String message = jsonObject.getString("msg");
                                //Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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

    private void HoaLoginDialog() {

        Hoadialog = new Dialog(getActivity());
        Hoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Hoadialog.setCancelable(true);
        Hoadialog.setContentView(R.layout.hoa_login_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout ll_next=Hoadialog.findViewById(R.id.ll_next);
        EditText et_username=Hoadialog.findViewById(R.id.et_username);
        final EditText et_userPass=Hoadialog.findViewById(R.id.et_userPass);

        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_username.getText().toString().isEmpty() &&
                        !et_userPass.getText().toString().isEmpty()){

                    if (Conectivity.isConnected(getActivity())){
                        HoaLoginApi(et_username.getText().toString(),et_userPass.getText().toString());
                    }else {
                        Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Please enter user name & password", Toast.LENGTH_SHORT).show();
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

    private void HoaLoginApi(String user_name, String pass) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+hoa_login;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(getContext()))
                .addBodyParameter("user_name",user_name )
                .addBodyParameter("password",pass)
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("hoa_login", jsonObject.toString());

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Hoadialog.dismiss();
                               Toast.makeText(getActivity(), "HOA Login Successful", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(getActivity(), HOA_Dashboard.class);
                                  startActivity(intent);
                               //  getActivity().finish();
                            } else {

                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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

}
