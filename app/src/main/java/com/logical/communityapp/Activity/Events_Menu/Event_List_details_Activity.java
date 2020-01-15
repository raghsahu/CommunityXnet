package com.logical.communityapp.Activity.Events_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityEventListDetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_event_details;
import static com.logical.communityapp.Api_Url.Base_Url.join_event;

public class Event_List_details_Activity extends AppCompatActivity {

    ActivityEventListDetailsBinding activityEventListDetailsBinding;
    ImageView iv_back;
    String Event_Id,Joined_status;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEventListDetailsBinding= DataBindingUtil.
                setContentView(this,R.layout.activity_event__list_details_);
        sessionManager=new SessionManager(this);

        //setContentView(R.layout.activity_event__list_details_);

      //  iv_back=findViewById(R.id.iv_back);
        try {
            if (getIntent()!=null){
                Event_Id=getIntent().getStringExtra("Event_Id");
                Joined_status=getIntent().getStringExtra("Joined_status");

                if (Joined_status.equalsIgnoreCase("0")){
                activityEventListDetailsBinding.btnJoinEvent.setText("Attend");
                }else {
                    activityEventListDetailsBinding.btnJoinEvent.setText("Attending");
                }
            }
        }catch (Exception e){

        }


        activityEventListDetailsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        if (Conectivity.isConnected(Event_List_details_Activity.this)){
            getEventDetails();

        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

        activityEventListDetailsBinding.btnJoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Conectivity.isConnected(Event_List_details_Activity.this)){
                    Join_EventNow();

                }else {
                    Toast.makeText(Event_List_details_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void Join_EventNow() {
        final ProgressDialog progressDialog = new ProgressDialog(Event_List_details_Activity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+join_event;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(Event_List_details_Activity.this))
                 .addBodyParameter("event_id",Event_Id )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("event_join", jsonObject.toString());

                             String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");
                            String joined_status = jsonObject.getString("joined_status");

                            if (joined_status.equalsIgnoreCase("0")){
                                activityEventListDetailsBinding.btnJoinEvent.setText("Join");
                            }else {
                                activityEventListDetailsBinding.btnJoinEvent.setText("Cancel");
                            }

                            if (result.equalsIgnoreCase("true")) {
                                //activityEventListDetailsBinding.btnJoinEvent.setText("Cancel");
                                 Toast.makeText(Event_List_details_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                               // JSONObject jsonObject1=jsonObject.getJSONObject("data");
                               // String id = jsonObject1.getString("id");

                            } else {
                              //  String msg = jsonObject.getString("msg");
                               Toast.makeText(Event_List_details_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
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

    private void getEventDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(Event_List_details_Activity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_event_details;
        AndroidNetworking.post(url)
                .addBodyParameter("id", Event_Id)
                 .addBodyParameter("member_id",Shared_Preference.getUser_Id(Event_List_details_Activity.this) )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            Log.e("event_details", jsonObject.toString());
                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String id = jsonObject1.getString("id");
                                String member_id = jsonObject1.getString("member_id");
                                String community_id = jsonObject1.getString("community_id");
                                String name = jsonObject1.getString("name");
                                String location = jsonObject1.getString("location");
                                String venue = jsonObject1.getString("venue");
                                String date = jsonObject1.getString("date");
                                String time = jsonObject1.getString("time");
                                String type = jsonObject1.getString("type");
                                String about = jsonObject1.getString("about");
                                String image = jsonObject1.getString("image");
                                String total_member = jsonObject1.getString("total_member");
                                String joined_status = jsonObject1.getString("joined_status");

                                activityEventListDetailsBinding.tvEventName.setText(name);
                                activityEventListDetailsBinding.tvEventLocation.setText(venue+" "+location);
                                activityEventListDetailsBinding.tvEventDateTime.setText(date+" "+time);
                                activityEventListDetailsBinding.tvAbout.setText(about);
                                activityEventListDetailsBinding.tvTotalMember.setText(total_member.toString());

                                Picasso.with(Event_List_details_Activity.this).load(community_icon_BaseUrl+image)
                                        .fit()
                                        .centerCrop()
                                        .placeholder(R.drawable.star)
                                        .error(R.drawable.star)
                                        .into(activityEventListDetailsBinding.eventIcon);

                                if (joined_status.equalsIgnoreCase("0")){
                                    activityEventListDetailsBinding.btnJoinEvent.setText("Join");
                                }else {
                                    activityEventListDetailsBinding.btnJoinEvent.setText("Cancel");
                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
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
}
