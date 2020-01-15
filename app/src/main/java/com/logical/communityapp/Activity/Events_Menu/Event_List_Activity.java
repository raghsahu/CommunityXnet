package com.logical.communityapp.Activity.Events_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.adapter.EventListAdapter;
import com.logical.communityapp.model_class.EventListModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_events;

public class Event_List_Activity extends AppCompatActivity {
    LinearLayout add_event,ll_filter_event;
    String Community_Id;
    ImageView iv_back;
    RecyclerView recycler_event_list;
    ArrayList<EventListModel>eventListModels=new ArrayList<>();
    EventListAdapter eventListAdapter;
    SearchView searchView;
    private Dialog Hoadialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__list_);
        hideSoftKeyboard();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        iv_back=findViewById(R.id.iv_back);
        recycler_event_list=findViewById(R.id.recycler_event_list);
        searchView=findViewById(R.id.search_hint);

        add_event=findViewById(R.id.add_event);
        ll_filter_event=findViewById(R.id.ll_filter_event);

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Event_List_Activity.this, AddEventActivity.class);
                intent.putExtra("Community_Id", Community_Id);
                startActivity(intent);

            }
        });

        ll_filter_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // OpenCalanderFilter();
                OpenfilterDialog();
            }
        });


        if (getIntent()!=null){

           Community_Id=getIntent().getStringExtra("Community_Id");
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.clearFocus();
        searchView.setFocusable(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });


        if (Conectivity.isConnected(Event_List_Activity.this)){
            GetEventList("");

        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void OpenfilterDialog() {
        Hoadialog = new Dialog(Event_List_Activity.this);
        Hoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Hoadialog.setCancelable(true);
        Hoadialog.setContentView(R.layout.filter_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout ll_next=Hoadialog.findViewById(R.id.ll_next);
        final TextView tv_date=Hoadialog.findViewById(R.id.tv_date);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                final DatePickerDialog mDatePicker = new DatePickerDialog(Event_List_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        tv_date.setText(sdf.format(myCalendar.getTime()));
                        Toast.makeText(Event_List_Activity.this, " "+sdf.format(myCalendar.getTime()), Toast.LENGTH_SHORT).show();

                        mDay[0] = selectedday;
                        mMonth[0] = selectedmonth;
                        mYear[0] = selectedyear;

                    }
                }, mYear[0], mMonth[0], mDay[0]);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            }
        });

        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (Conectivity.isConnected(Event_List_Activity.this)){
                        Hoadialog.dismiss();
                        GetEventList(tv_date.getText().toString());

                    }else {
                        Toast.makeText(Event_List_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }




            }
        });


        try {
            if (!Event_List_Activity.this.isFinishing()){
                Hoadialog.show();
            }
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }


    }

    private void OpenCalanderFilter() {
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        final DatePickerDialog mDatePicker = new DatePickerDialog(Event_List_Activity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MM/yyyy"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
               // homework_date.setText(sdf.format(myCalendar.getTime()));
                Toast.makeText(Event_List_Activity.this, " "+sdf.format(myCalendar.getTime()), Toast.LENGTH_SHORT).show();

                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;


            }
        }, mYear[0], mMonth[0], mDay[0]);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();

        //*********************************
//        mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE,
//                "OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            String myFormat = "dd/MM/yyyy";
//                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
//                            // homework_date.setText(sdf.format(myCalendar.getTime()));
//                            Toast.makeText(Event_List_Activity.this, ""+sdf.format(myCalendar.getTime()), Toast.LENGTH_SHORT).show();
//
//                            if (Conectivity.isConnected(Event_List_Activity.this)){
//                                //GetEventList(sdf.format(myCalendar.getTime()));
//
//                            }else {
//                                Toast.makeText(Event_List_Activity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    }
//                });

    }

    private void filter(String newText) {
        // ArrayList<SearchProductList> temp = new ArrayList();
        ArrayList <EventListModel> Contact_li= new ArrayList<EventListModel>();
        for (EventListModel smodel : eventListModels) {
            //use .toLowerCase() for better matches
            if (smodel.name.toLowerCase().startsWith(newText.toLowerCase())) {
                Contact_li.add(smodel);
            }
        }
        //update recyclerview
        eventListAdapter.updateList(Contact_li);
    }

    private void GetEventList(String date) {

        final ProgressDialog progressDialog = new ProgressDialog(Event_List_Activity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_events;
        AndroidNetworking.post(url)
               // .addBodyParameter("community_id", Community_Id)
                .addBodyParameter("date", date)
                .addBodyParameter("member_id",Shared_Preference.getUser_Id(Event_List_Activity.this) )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {
                            eventListModels.clear();
                            Log.e("get_event_res", jsonObject.toString());

                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    EventListModel eventListModel=new EventListModel();

                                    eventListModel.id = dataObject.getString("id");
                                    eventListModel.member_id = dataObject.getString("member_id");
                                    eventListModel.community_id = dataObject.getString("community_id");
                                    eventListModel.name = dataObject.getString("name");
                                    eventListModel.location = dataObject.getString("location");
                                    eventListModel.venue = dataObject.getString("venue");
                                    eventListModel.date = dataObject.getString("date");
                                    eventListModel.time = dataObject.getString("time");
                                    eventListModel.type = dataObject.getString("type");
                                    eventListModel.about = dataObject.getString("about");
                                    eventListModel.image = dataObject.getString("image");
                                    eventListModel.joined_status = dataObject.getString("joined_status");

                                    eventListModels.add(0,eventListModel);

                                }

                            } else {
                                String message = jsonObject.getString("msg");
                                Toast.makeText(Event_List_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                            eventListAdapter = new EventListAdapter(eventListModels, Event_List_Activity.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(Event_List_Activity.this);
                            recycler_event_list.setLayoutManager(mLayoutManger);
                            recycler_event_list.setLayoutManager(new LinearLayoutManager(Event_List_Activity.this, RecyclerView.VERTICAL, false));
                            recycler_event_list.setItemAnimator(new DefaultItemAnimator());

                            recycler_event_list.setAdapter(eventListAdapter);
                            eventListAdapter.notifyDataSetChanged();

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

    public void hideSoftKeyboard() {

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
