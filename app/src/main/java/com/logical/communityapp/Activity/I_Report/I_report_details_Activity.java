package com.logical.communityapp.Activity.I_Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.logical.communityapp.adapter.community_adapter.CommentI_ReportAdapter;
import com.logical.communityapp.Api_Url.APIClient;
import com.logical.communityapp.Api_Url.Api_Call;
import com.logical.communityapp.model_class.i_report_model.I_reportDetailsModel;
import com.logical.communityapp.model_class.i_report_model.I_reportDetailsModelData;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;
import com.logical.communityapp.databinding.ActivityIReportDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.I_Report_Image_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.I_Report_Video_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.delete_i_report_comment;
import static com.logical.communityapp.Api_Url.Base_Url.i_report_comment;
import static com.logical.communityapp.Api_Url.Base_Url.member_pic_BaseUrl;

public class I_report_details_Activity extends AppCompatActivity implements CommentI_ReportAdapter.AdapterCallback {

   // ImageView iv_back;
   ActivityIReportDetailsBinding activityIReportDetailsBinding;
   String i_report_id,i_report_type;
   CommentI_ReportAdapter commentI_reportAdapter;
    I_reportDetailsModelData I_reportDetailsModelData;
    private String user_id;
    private LinkedHashSet<String> Chat_id_linked;
    private String Multichat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIReportDetailsBinding= DataBindingUtil.
                setContentView(this,R.layout.activity_i_report_details_);
       // setContentView(R.layout.activity_i_report_details_);


        activityIReportDetailsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        if (getIntent()!=null){
           i_report_id=getIntent().getStringExtra("i_report_id");
            i_report_type=getIntent().getStringExtra("i_report_type");
        }

        if (Conectivity.isConnected(I_report_details_Activity.this)){
           user_id= Shared_Preference.getUser_Id(this);
            getIreport_Details(user_id,i_report_id);

        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        activityIReportDetailsBinding.llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id= Shared_Preference.getUser_Id(I_report_details_Activity.this);
                String Et_comments=activityIReportDetailsBinding.etComment.getText().toString();

                if (Conectivity.isConnected(I_report_details_Activity.this)){
                  Post_Comment(user_id,Et_comments,i_report_id);

                }else {
                    Toast.makeText(I_report_details_Activity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


        activityIReportDetailsBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                String user_id= Shared_Preference.getUser_Id(I_report_details_Activity.this);


                    String result = null;
                    if(Chat_id_linked!=null && !Chat_id_linked.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> it = Chat_id_linked.iterator();
                        if(it.hasNext()) {
                            sb.append(it.next());
                        }
                        while(it.hasNext()) {
                            sb.append(",").append(it.next());
                        }
                        result = sb.toString();
                    }
                    Multichat_id=result;

                    Log.e("Multichat_id",Multichat_id);
                  //  Toast.makeText(I_report_details_Activity.this, "mm "+Multichat_id, Toast.LENGTH_SHORT).show();




                if (Conectivity.isConnected(I_report_details_Activity.this)){
                 DeleteMy_Chat(Multichat_id);

                }else {
                    Toast.makeText(I_report_details_Activity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


      //  ********************************

        activityIReportDetailsBinding.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (I_reportDetailsModelData.getType()
                        .equalsIgnoreCase("Video")){

                    if (I_reportDetailsModelData.getVideo()!=null &&
                            !I_reportDetailsModelData.getVideo().equalsIgnoreCase("")){

                        activityIReportDetailsBinding.postImage.setVisibility(View.GONE);
                        activityIReportDetailsBinding.playImage.setVisibility(View.GONE);
                        activityIReportDetailsBinding.videoView.setVisibility(View.VISIBLE);

                        Log.e("video_url", I_Report_Video_BaseUrl+I_reportDetailsModelData.getVideo());
                        MediaController vidControl = new MediaController(I_report_details_Activity.this);
                        String vidAddress = I_Report_Video_BaseUrl+I_reportDetailsModelData.getVideo();
                        Uri vidUri = Uri.parse(vidAddress);
                        activityIReportDetailsBinding.videoView.setVideoURI(vidUri);
                        activityIReportDetailsBinding.videoView.start();
                        vidControl.setAnchorView(activityIReportDetailsBinding.videoView);
                        activityIReportDetailsBinding.videoView.setMediaController(vidControl);

                    }else {
                        Toast.makeText(I_report_details_Activity.this, "No video found", Toast.LENGTH_SHORT).show();
                    }

                }else {

//                    Intent intent=new Intent(context, I_report_details_Activity.class);
////                    intent.putExtra("i_report_id", allCommunityModels.get(position).getReport_id());
////                    context.startActivity(intent);
                }

            }
        });

    }

    private void DeleteMy_Chat(String multichat_id) {
//        final ProgressDialog progressDialog = new ProgressDialog(I_report_details_Activity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        String url=BaseUrl+delete_i_report_comment;
        AndroidNetworking.post(url)
                .addBodyParameter("comment_id",multichat_id )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                      //  progressDialog.dismiss();
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(I_report_details_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                                getIreport_Details(user_id,i_report_id);
                                commentI_reportAdapter.notifyDataSetChanged();


                            } else {

                                Toast.makeText(I_report_details_Activity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("i_post_catch", e.toString());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("i_post_error", anError.getMessage());
                      //  progressDialog.dismiss();


                    }
                });

    }

    private void Post_Comment(final String user_id, String et_comments, final String i_report_id) {

//        final ProgressDialog progressDialog = new ProgressDialog(I_report_details_Activity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        String url=BaseUrl+i_report_comment;
        AndroidNetworking.post(url)
                .addBodyParameter("comment",et_comments )
                .addBodyParameter("report_id",i_report_id )
                .addBodyParameter("member_id",Shared_Preference.getUser_Id(I_report_details_Activity.this) )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                      //  progressDialog.dismiss();
                        try {

                          String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(I_report_details_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                                activityIReportDetailsBinding.etComment.setText("");
                                getIreport_Details(user_id,i_report_id);
                                commentI_reportAdapter.notifyDataSetChanged();


                            } else {

                                Toast.makeText(I_report_details_Activity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("i_post_catch", e.toString());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("i_post_error", anError.getMessage());
                      //  progressDialog.dismiss();


                    }
                });

    }

    private void getIreport_Details(String user_id, String i_report_id) {
        final ProgressDialog progressDialog =
                new ProgressDialog(I_report_details_Activity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<I_reportDetailsModel> call = apiInterface.GetI_Report_Details(user_id,i_report_id);

        call.enqueue(new Callback<I_reportDetailsModel>() {
            @Override
            public void onResponse(Call<I_reportDetailsModel> call, Response<I_reportDetailsModel> response) {

                try{
                    if (response!=null){
                        Log.e("res_i_report",""+response.body().getResult());

                        if (!response.body().getResult().equalsIgnoreCase("false")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                            I_reportDetailsModelData=new I_reportDetailsModelData();
                            I_reportDetailsModelData=response.body().getData();

                    activityIReportDetailsBinding.tvPName.setText(response.body().getData().getMemberName());
                    activityIReportDetailsBinding.tvPEmail.setText(response.body().getData().getMemberEmail());
                    activityIReportDetailsBinding.tvTime.setText(response.body().getData().getDateTime());
                    activityIReportDetailsBinding.tvPostText.setText(response.body().getData().getTexts());

                            if (!response.body().getData().getMemberImage().equalsIgnoreCase(null)){

                                Glide.with(I_report_details_Activity.this)
                                        .load(member_pic_BaseUrl+response.body().getData().getMemberImage())
                                        // .apply(options)
                                        .centerCrop()
                                       .placeholder(R.drawable.man)
                                        .into(activityIReportDetailsBinding.imgPerson);

                            }

                            if (response.body().getData().getType().equalsIgnoreCase("Image")){
                                if (response.body().getData().getImage()!=null &&
                                        !response.body().getData().getImage().equalsIgnoreCase("")){
                                    activityIReportDetailsBinding.postImage.setVisibility(View.VISIBLE);


                                    Glide.with(I_report_details_Activity.this)
                                            .load(I_Report_Image_BaseUrl+response.body().getData().getImage())
                                            // .apply(options)
                                            .centerCrop()
                                            // .placeholder(R.drawable.man)
                                            .into(activityIReportDetailsBinding.postImage);

                                }

                            }else if (response.body().getData().getType().equalsIgnoreCase("Video")){

                                if (response.body().getData().getVideo()!=null &&
                                        !response.body().getData().getVideo().equalsIgnoreCase("")) {
                                    activityIReportDetailsBinding.playImage.setVisibility(View.VISIBLE);
                                    activityIReportDetailsBinding.postImage.setVisibility(View.VISIBLE);

                                    final String vid_url=I_Report_Video_BaseUrl+response.body().getData().getVideo();

                                    //************find thumbnails image from video url**
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.isMemoryCacheable();
                                    Glide.with(I_report_details_Activity.this).setDefaultRequestOptions(requestOptions)
                                            .load(vid_url)
                                            .into(activityIReportDetailsBinding.postImage);
                                }

                            }else {
                                activityIReportDetailsBinding.videoView.setVisibility(View.GONE);
                                activityIReportDetailsBinding.postImage.setVisibility(View.GONE);
                                activityIReportDetailsBinding.playImage.setVisibility(View.GONE);
                            }


                            commentI_reportAdapter = new CommentI_ReportAdapter(response.body().getData().getComments(), I_report_details_Activity.this);
                            activityIReportDetailsBinding.recyclerComment.setLayoutManager(new LinearLayoutManager(I_report_details_Activity.this, RecyclerView.VERTICAL, false));
                            activityIReportDetailsBinding.recyclerComment.setAdapter(commentI_reportAdapter);
                            commentI_reportAdapter.notifyDataSetChanged();
                            activityIReportDetailsBinding.recyclerComment.setFocusable(true);

                        }else {
                            Toast.makeText(I_report_details_Activity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_i_report", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<I_reportDetailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error_i_report1",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onMethodCallback(LinkedHashSet<String> chat_id_linked) {

        Chat_id_linked=chat_id_linked;
        Log.e("Cat_Tab_id", ""+Chat_id_linked);

        if (Chat_id_linked!=null && !Chat_id_linked.isEmpty() ){
            activityIReportDetailsBinding.ivDelete.setVisibility(View.VISIBLE);

        }else {
            activityIReportDetailsBinding.ivDelete.setVisibility(View.INVISIBLE);
        }
    }
}
