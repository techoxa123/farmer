package com.example.priya.farmingapp;

import android.app.ProgressDialog;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SubCategory extends AppCompatActivity {
    ActionBar actionBar;
    ImageView imageView;
    RecyclerView rv;
    TextView t1,t2;
    ProgressDialog pd;
    String request_url="http://dailyupdatework.in/farmer_admin/api/category_fetch.php?";
   ArrayList<String>nameArray=new ArrayList<>();
   ArrayList<String>imageArray=new ArrayList<>();
    ArrayList<String>s_no=new ArrayList<>();
    ArrayList<String>category=new ArrayList<>();
   String categoryy="";
  /**  ImageView imageView;
    String image="";**/
   // String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(SubCategory.this);
        setContentView(R.layout.activity_second);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Farmer's Contact");
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //imageView=(ImageView)findViewById(R.id.image);
        t1=(TextView)findViewById(R.id.name);
        rv=(RecyclerView) findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        //name=getIntent().getStringExtra("name");
     //  t1.setText("name: "+name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        RVAdapter rvAdapter = new RVAdapter(getApplicationContext(),nameArray,imageArray,s_no,category);
        rv.setAdapter(rvAdapter);
        pd=new ProgressDialog(this);
        getSerVerData();
        //image=getIntent().getStringExtra("image");
         categoryy=getIntent().getStringExtra("name");
       /*** t2=(TextView)findViewById(R.id.tex2);
        image=getIntent().getStringExtra("image");
        name=getIntent().getStringExtra("name");
        Picasso.with(this).load(image ).into(imageView);
        t1.setText("name: "+name);**/



    }

    private void getSerVerData() {
        pd= new ProgressDialog(SubCategory.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(SubCategory.this);
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                            String check=jsonObject.getString("success");
//                            if(check.equalsIgnoreCase("0")){
//                                Toast.makeText(Navigation.this, "No Data", Toast.LENGTH_SHORT).show();
//                            }else {
//                            check = jsonObject.getString("data");

                                if (categoryy.equalsIgnoreCase(jsonObject1.getString("crop_name"))) {

                                    nameArray.add(jsonObject1.getString("farmer_name"));
//                                    imageArray.add(jsonObject1.getString("farmer_photo"));
                                    s_no.add(jsonObject1.getString("s_no"));
                                    category.add(jsonObject1.getString("crop_name"));
                                }

                            }

//                            for(int i=0;i<jsonObject1.length();i++)
//                            {
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//
//
//                                DataModel model=new DataModel(jsonObject1.getString("category"));
//                                dataModels.add(model);
//
//                            }
                            RVAdapter rvAdapter=new RVAdapter(getApplicationContext(),nameArray,imageArray,s_no,category);
                            rv.setAdapter(rvAdapter);



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


                params.put("category",categoryy);
                Log.e("category",categoryy);
//
//                params.put("img",img);
//                Log.e("img",img);

//                params.put("pass",pass);
//                Log.e("pass",pass);
//
//                params.put("c_pass",cpass);
//                Log.e("c_pass",cpass);
//
//                params.put("contact",mobile);
//                Log.e("contact",mobile);
//                params.put("address",add);
//                Log.e("address",add);



                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
