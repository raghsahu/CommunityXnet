package com.logical.communityapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.logical.communityapp.Activity.Events_Menu.Event_List_details_Activity;
import com.logical.communityapp.R;

public class Events_fragment extends Fragment {

    LinearLayout ll_view_event_details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_events_list, container, false);
        getActivity().setTitle("Events");

        ll_view_event_details=root.findViewById(R.id.ll_view_event_details);
        ll_view_event_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), Event_List_details_Activity.class);
                startActivity(intent);

            }
        });

        return root;
    }

}
