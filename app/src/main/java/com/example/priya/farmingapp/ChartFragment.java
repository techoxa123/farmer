package com.example.priya.farmingapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {
    ArrayList<String> CropName=new ArrayList<>();
    ArrayList<String> CropPrice=new ArrayList<>();

    RecyclerView recyclerView;
    AppCompatButton done;
    EditText price,description;
    ImageView upload;
    public static String login_url="http://dailyupdatework.in/farmer_admin/api/add_crop.php?";
    public static String Crop_List="http://dailyupdatework.in/farmer_admin/api/crop_fetch.php?";
    public static String Spinner_List="http://dailyupdatework.in/farmer_admin/api/category.php?";

    String crop="",getemail="",check="",Price="",Description="",Crop="";
    Spinner spinner;
    final ArrayList<String> SPINNERLIST = new ArrayList<>();
ProgressDialog pd;


    public static ChartFragment newInstance() {
        ChartFragment chartfragment = new ChartFragment();
        return chartfragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chart, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.cropname);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("farmer_data",MODE_PRIVATE);
        getemail=sharedPreferences.getString("email","");
        Log.e("Email",getemail);
        Categorylist();
        upload=(ImageView)view. findViewById(R.id.add);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.activity_crop__detail);
                spinner=(Spinner) dialog.findViewById(R.id.spinner);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        crop= SPINNERLIST.get(position);
                        Toast.makeText(getContext(), SPINNERLIST.get(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            price=(EditText)dialog.findViewById(R.id.priceedit) ;
            description=(EditText)dialog.findViewById(R.id.description);
                done=(AppCompatButton)dialog.findViewById(R.id.upload);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         Price=price.getText().toString();
                         Description=description.getText().toString();
                         Crop=crop;
                        UploadCrop();
                        dialog.dismiss();
//                        Toast.makeText(getContext(), "No Match Found", Toast.LENGTH_SHORT).show();

                    }
                });


                dialog.show();
            }
        });
        ListAdd();

        return view;
    }

    private void Categorylist() {
//        pd= new ProgressDialog(getContext());
//        pd.setMessage("Loading");
//        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,Spinner_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            check = jsonObject.getString("data");

//                            String ch=jsonObject.getString("data");
                            JSONArray jsonArray=jsonObject.getJSONArray("success");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                SPINNERLIST.add(jsonObject1.getString("category"));


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        pd.dismiss();
//                        Log.d("ErrorResponse",finalRespose);
                    }
                }
        )
        {
            protected Map<String, String> getParams()
            {
                Map<String,String> params= new HashMap<>();





                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void ListAdd() {
        pd= new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,Crop_List,
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

                                    CropName.add(jsonObject1.getString("crop_name"));
                                    CropPrice.add(jsonObject1.getString("crop_price"));

                            }
                            CropnameRecyclerAdapter cropnameRecyclerAdapter=new CropnameRecyclerAdapter(getActivity(), CropName,CropPrice);
                            recyclerView.setAdapter(cropnameRecyclerAdapter);
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

                params.put("email",getemail);



                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void UploadCrop() {
        pd= new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            check = jsonObject.getString("data");
                            check=jsonObject.getString("success");




                            if (check.equalsIgnoreCase("1"))
                            {

                                Intent i=new Intent(getContext(),FarmerData.class);
                                Toast.makeText(getContext(), "Upload done", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Some error created...", Toast.LENGTH_SHORT).show();
                            }


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
                        //Log.d("ErrorResponse",finalRespose);
                    }
                }
        )
        {
            protected Map<String, String> getParams()
            {
                Map<String,String> params= new HashMap<>();

                params.put("email",getemail);
                params.put("crop_name",Crop);
                params.put("crop_price",Price);
                params.put("description",Description);
//
//                params.put("password",PASSWORD);
//



                return params;
                //&fathers_business=atul&
// fathers_mobile=8956235689&mothers_name=gtra&mothers_mobile=7845124578&mothers_business=sco&
// mama_s_name=asz&mamas_phone=9865325689&mamas_address=jaipur&mama_city=sodala&state=rajasthan&images=atul
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

}
