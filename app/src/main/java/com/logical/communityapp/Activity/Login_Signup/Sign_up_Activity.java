package com.logical.communityapp.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.Community.SeeAllCommunityActivity;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Activity.Profile_Related.EditProfileActivity;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivitySignupBiding;
import com.logical.communityapp.model_class.city_state_country.CityData;
import com.logical.communityapp.model_class.city_state_country.CityDetailsModel;
import com.logical.communityapp.model_class.city_state_country.CountryData;
import com.logical.communityapp.model_class.city_state_country.CountryDeailsModel;
import com.logical.communityapp.model_class.city_state_country.StateData;
import com.logical.communityapp.model_class.city_state_country.StateDeailsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.member_register;

public class Sign_up_Activity extends AppCompatActivity {

    ActivitySignupBiding signupBiding;
   // LinearLayout ll_login;
    Context context;
    SessionManager sessionManager;
    ArrayList<String> spin_country=new ArrayList<>();
    ArrayList<String>spin_state=new ArrayList<>();
    ArrayList<String>spin_city=new ArrayList<>();
    public HashMap<Integer, CountryData> CountryHashMap = new HashMap<Integer, CountryData>();
    public HashMap<Integer, StateData> StateHashMap = new HashMap<Integer, StateData>();
    public HashMap<Integer, CityData> CityHashMap = new HashMap<>();
    String CountryId,StateId,CityId,path;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_sign_up_);
        signupBiding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up_);
        context=Sign_up_Activity.this;
        sessionManager=new SessionManager(this);
       // ll_login=findViewById(R.id.ll_login);


        if (Conectivity.isConnected(this)){//***check internet conectivity
            getCountry();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        signupBiding.etPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_pass) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        signupBiding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_email) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });


        signupBiding.llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sign_up_Activity.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        signupBiding.spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < CountryHashMap.size(); i++)
                {
                    if (CountryHashMap.get(i).getName().equals( signupBiding.spinCountry.getItemAtPosition(position)))
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


        signupBiding.spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < StateHashMap.size(); i++)
                {
                    if (StateHashMap.get(i).getName().equals( signupBiding.spinState.getItemAtPosition(position)))
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

        signupBiding.spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < CityHashMap.size(); i++)
                {
                    if (CityHashMap.get(i).getName().equals( signupBiding.spinCity.getItemAtPosition(position)))
                    {
                        CityId=CityHashMap.get(i).getId();

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });



        signupBiding.llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String First_name=signupBiding.etFirstName.getText().toString();
                String Last_name=signupBiding.etLastName.getText().toString();
                String Mobile_number=signupBiding.etMobile.getText().toString();
                String Email_id=signupBiding.etEmail.getText().toString();
                String Password=signupBiding.etPass.getText().toString();
                
                if (!First_name.isEmpty() && !Last_name.isEmpty() && !Mobile_number.isEmpty()
                    && !Email_id.isEmpty() && !Password.isEmpty()){

                    if (Patterns.EMAIL_ADDRESS.matcher(Email_id).matches()){
                        if (Mobile_number.length()==10){
                            if (Password.length()>5){

                                if (signupBiding.chTerms.isChecked()){

                                    if (sessionManager.getTokenId()!=null && !sessionManager.getTokenId().isEmpty() &&
                                            !sessionManager.getTokenId().equalsIgnoreCase("")){

                                        if (Conectivity.isConnected(context)){
                                            CallSignUpApi(First_name,Last_name,Mobile_number,Email_id,Password,sessionManager.getTokenId());
                                        }else {
                                            Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(context, "Firebase token not get yet", Toast.LENGTH_SHORT).show();
                                    }


                                }else {
                                    Toast.makeText(context, "Please accept T&C", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(context, "Password must be 6 digit", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(Sign_up_Activity.this, "Please enter valid Email id", Toast.LENGTH_SHORT).show();
                    }


                    
                }else {
                    Toast.makeText(context, "All field required", Toast.LENGTH_SHORT).show();
                }
          
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
                                    (Sign_up_Activity.this, android.R.layout.simple_spinner_item, spin_city); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            signupBiding.spinCity.setAdapter(spinnerArrayAdapter);


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
                                    (Sign_up_Activity.this, android.R.layout.simple_spinner_item, spin_state); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            signupBiding.spinState.setAdapter(spinnerArrayAdapter);


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
                                    (Sign_up_Activity.this, android.R.layout.simple_spinner_item, spin_country); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            signupBiding.spinCountry.setAdapter(spinnerArrayAdapter);


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

    private void CallSignUpApi(String first_name, String last_name, String mobile_number, String email_id,
                               String password, String tokenId) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+member_register;
        AndroidNetworking.post(url)
                .addBodyParameter("first_name",first_name )
                .addBodyParameter("last_name",last_name )
                .addBodyParameter("email",email_id )
                .addBodyParameter("password",password )
                .addBodyParameter("phone_number",mobile_number )
                .addBodyParameter("city_id",CityId )
                .addBodyParameter("state_id",StateId )
                .addBodyParameter("country_id",CountryId )
                .addBodyParameter("fcm_id",tokenId )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            Log.e("reg_new_user", jsonObject.toString());
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Log.e("reg_new_user",message);
                                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show();

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                String id = dataObject.getString("id");
                                String member_id = dataObject.getString("member_id");
                                String first_name = dataObject.getString("first_name");
                                String last_name = dataObject.getString("last_name");
                                String email = dataObject.getString("email");
                                String image = dataObject.getString("image");
                                String phone_number = dataObject.getString("phone_number");
                                String street_address = dataObject.getString("street_address");
                               // String lga = dataObject.getString("lga");
                                String city_id = dataObject.getString("city_id");
                                String state_id = dataObject.getString("state_id");
                                String email_status = dataObject.getString("email_status");
                              //  String com_join_status = dataObject.getString("com_join_status");

                                sessionManager.setLogin(true);
                                Shared_Preference.setUser_Id(Sign_up_Activity.this, id);
                                Shared_Preference.setName(Sign_up_Activity.this, first_name);
                                Shared_Preference.setEmail(Sign_up_Activity.this, email);
                                Shared_Preference.setImage(Sign_up_Activity.this, image);

                              //  if (com_join_status.equalsIgnoreCase("0")){
                                    Intent intent=new Intent(context, SignupWaitingApproval.class);
                                    startActivity(intent);
                                    finish();
//                                }else {
//                                    Intent intent=new Intent(context, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
                                
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
