package com.example.priya.farmingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    ActionBar actionBar;

    EditText distext,titletxt;
    ProgressDialog pd;


    AppCompatButton subbmit;
    String check;
    public static String login_url="http://dailyupdatework.in/farmer_admin/api/feedback.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(Feedback.this);
       /// actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setTitle("Feedback");
//        actionBar.show();
        setContentView(R.layout.activity_feedback);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Feedback");
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);

        AdRequest adRequest = new AdRequest.Builder().build();




        titletxt=(EditText)findViewById(R.id.title);
        distext=(EditText)findViewById(R.id.dis);


        subbmit=(AppCompatButton)findViewById(R.id.send);
        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done() {
        pd= new ProgressDialog(Feedback.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(Feedback.this);
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

                                Intent i=new Intent(getApplicationContext(),Navigation.class);
                                Toast.makeText(Feedback.this, "Feedback Sent...", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Feedback.this, "some error occured", Toast.LENGTH_SHORT).show();
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

//                params.put("email",EMAIL);
//
//                params.put("password",PASSWORD);

                params.put("id","1");
                params.put("title",titletxt.getText().toString());
                params.put("description", distext.getText().toString());




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
