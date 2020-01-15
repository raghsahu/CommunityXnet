package com.logical.communityapp.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.logical.communityapp.adapter.Community_Tab_adapter;
import com.logical.communityapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Community_Stream_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Community_Stream_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Community_Stream_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayout tabLayout;
    ViewPager viewPager;
    FrameLayout simpleFrameLayout;
    Community_Tab_adapter community_tab_adapter;
    TabItem tabplay,tabcompleted,tabresults;

    private OnFragmentInteractionListener mListener;

    public Community_Stream_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Community_Stream_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Community_Stream_fragment newInstance(String param1, String param2) {
        Community_Stream_fragment fragment = new Community_Stream_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_community__stream_fragment, container, false);
        getActivity().setTitle("Community stream");



//        tabLayout=(TabLayout)root.findViewById(R.id.tabLayout);
//        viewPager=(ViewPager)root.findViewById(R.id.viewPager);
//        simpleFrameLayout = (FrameLayout)root.findViewById(R.id.simpleFrameLayout);
//        tabplay = root.findViewById(R.id.tabplay);
//        tabcompleted = root.findViewById(R.id.tabcompleted);
//        tabresults = root.findViewById(R.id.tabresults);
//
//        community_tab_adapter = new Community_Tab_adapter(getFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(community_tab_adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                if (tab.getPosition() == 1) {
//
////                    tabLayout.setBackgroundColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                            R.color.colorAccent));
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        getWindow().setStatusBarColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                                R.color.colorAccent));
////                    }
//                } else if (tab.getPosition() == 2) {
//
////                    tabLayout.setBackgroundColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                            android.R.color.darker_gray));
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        getWindow().setStatusBarColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                                android.R.color.darker_gray));
////                    }
//                } else {
////                    tabLayout.setBackgroundColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                            R.color.colorPrimary));
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        getWindow().setStatusBarColor(ContextCompat.getColor(LeaguePlaySeeMore.this,
////                                R.color.colorPrimaryDark));
////                    }
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
