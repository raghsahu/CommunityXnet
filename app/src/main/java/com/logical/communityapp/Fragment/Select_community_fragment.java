package com.logical.communityapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.logical.communityapp.adapter.community_adapter.SelectCommunityAdapter;
import com.logical.communityapp.model_class.community_model.AllCommunityModel;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;
import com.logical.communityapp.Utils.Conectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.logical.communityapp.Api_Url.Base_Url.BaseUrl;
import static com.logical.communityapp.Api_Url.Base_Url.get_communities;


public class Select_community_fragment extends Fragment {
    RecyclerView recycler_all_community;
    SelectCommunityAdapter mAdapter;
    TextView tv_create_community;
    ArrayList<AllCommunityModel> allCommunityModels=new ArrayList<>();
    SearchView searchView;

    public Select_community_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_select_community_fragment, container, false);
        getActivity().setTitle("Select community");
        hideSoftKeyboard();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recycler_all_community=root.findViewById(R.id.recycler_all_community);
        searchView=root.findViewById(R.id.search_hint);

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

        if (Conectivity.isConnected(getActivity())){
            getAllCommunity();
        }else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }



        return root;
    }

    public void hideSoftKeyboard() {

        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    //*********************************************************
    private void filter(String newText) {
        // ArrayList<SearchProductList> temp = new ArrayList();
        ArrayList <AllCommunityModel> Contact_li= new ArrayList<AllCommunityModel>();
        for (AllCommunityModel smodel : allCommunityModels) {
            //use .toLowerCase() for better matches
            if (smodel.name.toLowerCase().startsWith(newText.toLowerCase())) {
                Contact_li.add(smodel);
            }
        }
        //update recyclerview
        mAdapter.updateList(Contact_li);

    }

    private void getAllCommunity() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        String url=BaseUrl+get_communities;
        AndroidNetworking.post(url)
                .addBodyParameter("member_id", Shared_Preference.getUser_Id(getActivity()))
                // .addBodyParameter("password",password )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        try {

                            // String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject dataObject=jsonArray.getJSONObject(i);

                                    AllCommunityModel allCommunityModel=new AllCommunityModel();

                                    allCommunityModel.id = dataObject.getString("id");
                                    allCommunityModel.member_id = dataObject.getString("member_id");
                                    allCommunityModel.community_id = dataObject.getString("community_id");
                                    allCommunityModel.name = dataObject.getString("name");
                                    allCommunityModel.icon = dataObject.getString("icon");
                                    allCommunityModel.description = dataObject.getString("description");
                                    allCommunityModel.address = dataObject.getString("address");
                                    allCommunityModel.lga = dataObject.getString("lga");
                                    allCommunityModel.city_id = dataObject.getString("city_id");
                                    allCommunityModel.state_id = dataObject.getString("state_id");
                                    allCommunityModel.country_id = dataObject.getString("country_id");
                                    allCommunityModel.do_you_live_in_com = dataObject.getString("do_you_live_in_com");
                                    allCommunityModel.are_you_property_owner = dataObject.getString("are_you_property_owner");
                                    allCommunityModel.do_you_work_in_com = dataObject.getString("do_you_work_in_com");
                                    allCommunityModel.status = dataObject.getString("status");
                                    allCommunityModel.created_date = dataObject.getString("created_date");
                                    allCommunityModel.country = dataObject.getString("country");
                                    allCommunityModel.states_name = dataObject.getString("states_name");
                                    allCommunityModel.city = dataObject.getString("city");
                                    allCommunityModel.join_status = dataObject.getString("join_status");


                                    allCommunityModels.add(allCommunityModel);

                                }


                            } else {

                                // Toast.makeText(context, "Login fail, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new SelectCommunityAdapter(allCommunityModels, getActivity());
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity());
                            recycler_all_community.setLayoutManager(mLayoutManger);
                            recycler_all_community.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            recycler_all_community.setItemAnimator(new DefaultItemAnimator());
                            recycler_all_community.setAdapter(mAdapter);
                            recycler_all_community.setFocusable(false);
                            mAdapter.notifyDataSetChanged();

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
