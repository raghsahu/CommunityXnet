package com.logical.communityapp.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.Community.SeeAllCommunityActivity;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityLoginBiding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.login;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBiding loginActivityBiding;
    Context context;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        loginActivityBiding=DataBindingUtil.setContentView(this,R.layout.activity_login);
        context=LoginActivity.this;
        sessionManager=new SessionManager(LoginActivity.this);

        loginActivityBiding.llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, Sign_up_Activity.class);
                startActivity(intent);
                //finish();
            }
        });

        loginActivityBiding.tvForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        loginActivityBiding.llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email_id=loginActivityBiding.etEmail.getText().toString();
                String Password=loginActivityBiding.etUserPass.getText().toString();

                if (!Email_id.isEmpty() && !Password.isEmpty()){

                    if (Patterns.EMAIL_ADDRESS.matcher(Email_id).matches()){

                        if (sessionManager.getTokenId()!=null && !sessionManager.getTokenId().isEmpty() &&
                            !sessionManager.getTokenId().equalsIgnoreCase("")){

                            if (Conectivity.isConnected(context)){
                                CallLoginApi(Email_id,Password,sessionManager.getTokenId());
                            }else {
                                Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(LoginActivity.this, "Firebase token not get yet", Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        Toast.makeText(LoginActivity.this, "Please enter valid Email id", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    if (Email_id.isEmpty()){
                        loginActivityBiding.etEmail.setError("Please enter email");
                    }else if (Password.isEmpty()){
                        loginActivityBiding.etUserPass.setError("Please enter password");
                    }
                }

            }
        });



    }

    private void CallLoginApi(String email_id, String password, String tokenId) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+login;
        AndroidNetworking.post(url)
                .addBodyParameter("email",email_id )
                .addBodyParameter("password",password )
               .addBodyParameter("fcm_id",tokenId )

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

                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();

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
                                String com_join_status = dataObject.getString("com_join_status");

                                sessionManager.setLogin(true);
                                Shared_Preference.setUser_Id(LoginActivity.this, id);
                                Shared_Preference.setName(LoginActivity.this, first_name);
                                Shared_Preference.setEmail(LoginActivity.this, email);
                                Shared_Preference.setImage(LoginActivity.this, image);

                                if (com_join_status.equalsIgnoreCase("0")){
                                    Intent intent=new Intent(context, SeeAllCommunityActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent=new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {

                                Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
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
