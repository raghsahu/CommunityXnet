package com.logical.communityapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.Activity.Events_Menu.Event_List_details_Activity;
import com.logical.communityapp.model_class.EventListModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.community_icon_BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.join_event;

/**
 * Created by Raghvendra Sahu on 13/11/2019.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<EventListModel> allCommunityModels;
    private Context context;
    private String user_id;

    public EventListAdapter(ArrayList<EventListModel> allCommunityModels1, Context context) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final EventListModel allCommunityModel=allCommunityModels.get(position);
            holder.tv_event_name.setText(allCommunityModel.name);
            holder.tv_date.setText(allCommunityModel.date);
            holder.tv_venue.setText(allCommunityModel.venue);

            if (!allCommunityModel.image.equalsIgnoreCase(null)){
                Picasso.with(context).load(community_icon_BaseUrl+allCommunityModel.image)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.star)
                        .error(R.drawable.star)
                        .into(holder.event_img);
            }

            if (allCommunityModels.get(position).joined_status.equalsIgnoreCase("0")){
                holder.btn_join.setText("Attend");

            }else {
                holder.btn_join.setText("Attending");
            }
//            holder.card_event.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(context, Event_List_details_Activity.class);
//                   // intent.putExtra("Community_Id", allCommunityModels.get(position).id);
//                    context.startActivity(intent);
//
//                }
//            });

        holder.view_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Event_List_details_Activity.class);
                   intent.putExtra("Event_Id", allCommunityModels.get(position).id);
                   intent.putExtra("Joined_status", allCommunityModels.get(position).joined_status);
                    context.startActivity(intent);

                }
            });

            holder.btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String EventId=allCommunityModels.get(position).id;
                    Join_EventNow(EventId,holder.btn_join);

                }
            });

        }
    }

    private void Join_EventNow(String eventId, final Button btn_join) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+join_event;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(context))
                .addBodyParameter("event_id",eventId )
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

                            if (message.equalsIgnoreCase("Successfully joined.")){
                                btn_join.setText("Attending");
                            }else {
                                btn_join.setText("Attend");
                            }
                            if (result.equalsIgnoreCase("true")) {
                                //activityEventListDetailsBinding.btnJoinEvent.setText("Cancel");
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                // JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                // String id = jsonObject1.getString("id");
                            } else {
                                //  String msg = jsonObject.getString("msg");
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


    @Override
    public int getItemCount() {
        return allCommunityModels.size();
        //return rattingReviewList.size();

    }

    public void updateList(ArrayList<EventListModel> contact_li) {
        allCommunityModels = contact_li;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_event_name,tv_venue,tv_date;
        public ImageView event_img;
        CardView card_event;
        Button view_event,btn_join;

        public ViewHolder(View parent) {
            super(parent);
            card_event = parent.findViewById(R.id.card_event);
            event_img = parent.findViewById(R.id.event_img);
            tv_event_name = parent.findViewById(R.id.tv_event_name);
            tv_venue = parent.findViewById(R.id.tv_venue);
            tv_date = parent.findViewById(R.id.tv_date);
            view_event = parent.findViewById(R.id.view_event);
            btn_join = parent.findViewById(R.id.btn_join);

        }

    }
}
