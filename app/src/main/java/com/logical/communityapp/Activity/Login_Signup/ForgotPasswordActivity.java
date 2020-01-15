package com.logical.communityapp.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityForgotBiding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.forgot_password;
import static com.logical.communityapp.Api_Url.Base_Url.login;

public class ForgotPasswordActivity extends AppCompatActivity  {

    ActivityForgotBiding activityForgotBiding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotBiding= DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        context=ForgotPasswordActivity.this;
       // setContentView(R.layout.activity_forgot_password);

        activityForgotBiding.llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_id=  activityForgotBiding.etEmail.getText().toString();

                if (Conectivity.isConnected(context)){

                    ForgotApi(email_id);
                }else {
                    Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void ForgotApi(String email_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+forgot_password;
        AndroidNetworking.post(url)
                .addBodyParameter("email",email_id )
               // .addBodyParameter("password",password )

                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(context, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                            } else {

                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
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
