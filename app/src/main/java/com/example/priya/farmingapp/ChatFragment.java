package com.example.priya.farmingapp;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment  {
    RecyclerView recyclerView;
    ArrayList<String> InterestName=new ArrayList<>();
    ArrayList<String> InterestContect=new ArrayList<>();
    ArrayList<String> InterestEmail=new ArrayList<>();
    ArrayList<String> InterestAddress=new ArrayList<>();
    ArrayList<String> Description=new ArrayList<>();
    String fetch_interest = "http://dailyupdatework.in/farmer_admin/api/fetch_by.php?";
    ProgressDialog pd;
    String email="";
    public static ChatFragment newInstance() {
        ChatFragment chatFragment=new ChatFragment();
        return chatFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.interestlist);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("farmer_data",MODE_PRIVATE);

        email=sharedPreferences.getString("email","");
//        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(ChatFragment.this).attach(ChatFragment.this).commit();
        interestfetch();
        return view;
    }

    private void interestfetch() {
        pd= new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,fetch_interest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            check = jsonObject.getString("data");

//                            String ch=jsonObject.getString("data");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                InterestName.add(jsonObject1.getString("name"));
                                InterestContect.add(jsonObject1.getString("contact"));
                                InterestEmail.add(jsonObject1.getString("by_id"));
                                InterestAddress.add(jsonObject1.getString("address"));
                                Description.add(jsonObject1.getString("description"));
                            }

                            InterestsRecyclerAdapter interestsRecyclerAdapter=new InterestsRecyclerAdapter(getActivity(),Description,InterestName,InterestContect,InterestEmail,InterestAddress);
                            recyclerView.setAdapter(interestsRecyclerAdapter);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(false);
                            recyclerView.setFocusableInTouchMode(false);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();
//                        Log.d("ErrorResponse",finalRespose);
                    }
                }
        )
        {
            protected Map<String, String> getParams()
            {
                Map<String,String> params= new HashMap<>();

                params.put("by_id",email);



                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

}
