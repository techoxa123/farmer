package com.example.priya.farmingapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    Context context;
    CircleImageView circleImageView;
    TextView text_name,text_email, text_contact,crop_name, crop,crops_rate, price, text_descrit,descript,address;
    ActionBar actionBar;
    Button button,sendinterest;
    ProgressDialog pd;
    String request_url = "http://dailyupdatework.in/farmer_admin/api/category_details.php?";
    String interest_url = "http://dailyupdatework.in/farmer_admin/api/aad_by.php?";
    String addresss="",name="",email ="", contact ="", crops ="", crop_rate ="", decription ="";
    String s_no = "",buy_email="";
    String category="",check="",descriptionstring="";
    private Boolean CallEnded = false;
    EditText des;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(Profile.this);
        setContentView(R.layout.activity_profile);
        circleImageView = (CircleImageView) findViewById(R.id.image);
        text_name=(TextView)findViewById(R.id.name);
        text_contact = (TextView) findViewById(R.id.phone);
        text_email = (TextView) findViewById(R.id.email);
        crop_name=(TextView)findViewById(R.id.crops_name);
        crop = (TextView) findViewById(R.id.crop);
        crops_rate=(TextView)findViewById(R.id.crops_price);
        price = (TextView) findViewById(R.id.price);
        descript = (TextView) findViewById(R.id.des);
        text_descrit=(TextView)findViewById(R.id.description);
        address=(TextView)findViewById(R.id.address);
        sendinterest=(Button)findViewById(R.id.sendinterest);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Farmer's Details");
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("buy_login",MODE_PRIVATE);
        buy_email=sharedPreferences.getString("email","");

        final TelephonyManager telephoneM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener listener;
        listener = new PhoneStateListener() {

            public void onCallStateChanged(int state, String incomingNumber) {


                switch (state) {

                    case TelephonyManager.CALL_STATE_IDLE:

                        if(CallEnded){

                            performDial();

                        }


                        break;


                    case TelephonyManager.CALL_STATE_RINGING:

                        break;


                    case TelephonyManager.CALL_STATE_OFFHOOK:

                        CallEnded=true;

                }
            }
        };


        telephoneM.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        button = (Button) findViewById(R.id.call);
          s_no = getIntent().getStringExtra("s_no");
        category = getIntent().getStringExtra("category");
        profileData();
        sendinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Profile.this);
                dialog.setContentView(R.layout.send_interest);
                des=(EditText)dialog.findViewById(R.id.description);

                send=(Button)dialog.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descriptionstring=des.getText().toString().trim();
                        if (descriptionstring.equalsIgnoreCase(""))
                        {
                            des.setError("Please fill this fild");
                        }else
                        {
                            InterestApi();
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();

//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
//                alertDialogBuilder.setMessage("Are you sure you want send interest to farmer?");
//                alertDialogBuilder.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            InterestApi();
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//
//                //Showing the alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDial();
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:999"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(Profile.this, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
//                    requestPermissions();
//                    return;
//                }
//                startActivity(intent);




    }

//            private void requestPermissions() {
//                ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},1);
//            }

        });

    }

    private void InterestApi() {
        pd= new ProgressDialog(Profile.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,interest_url,
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


                                Toast.makeText(getApplicationContext(), "Sent...", Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Some error created...", Toast.LENGTH_SHORT).show();
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

                params.put("by_id",buy_email);
                params.put("farmer_id",email);
                params.put("category",crops);
                params.put("price",crop_rate);
                params.put("description",descriptionstring);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void performDial() {
        Intent dial = new Intent(Intent.ACTION_CALL);
        dial.setData(Uri.parse("tel:"+contact));

        if (ActivityCompat.checkSelfPermission(Profile.this,     Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(Profile.this, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
            requestPermissions();
            return;

        }
        startActivity(dial);


    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},1);

    }

    private void profileData() {
        pd= new ProgressDialog(Profile.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(Profile.this);
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
//                            check = jsonObject.getString("data");

//                            String ch=jsonObject.getString("data");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

//                                    photo=jsonObject1.getString("farmer_photo");
                                    name=jsonObject1.getString("farmer_name");
                                    Log.e("name",name);
                                    email=jsonObject1.getString("email");
                                    Log.e("em",email);
                                    addresss=jsonObject1.getString("address");
                                Log.e("add",addresss);
                                    contact=jsonObject1.getString("contact");
                                    Log.e("con",contact);
                                    crops=jsonObject1.getString("crop_name");
                                    Log.e("crop",crops);
                                    crop_rate=jsonObject1.getString("crop_price");
                                    Log.e("price",crop_rate);
                                    decription=jsonObject1.getString("description");
                                    Log.e("description",decription);
//                                    Picasso.with(context).load(photo).into(circleImageView);
                                  // circleImageView.setImageResource(Integer.parseInt(photo));
                                    text_name.setText(name);
                                    text_email.setText(email);
                                    text_contact.setText(contact);
                                    crop.setText(crops);
                                    price.setText("₹. "+crop_rate+" /quintile");
                                    descript.setText(decription);
                                     address.setText(addresss);

                            }





//                            JSONArray jsonArray=jsonObject.getJSONArray("data");
//
//                            for(int i=0;i<jsonArray.length();i++)
//                            {
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                                if (s_no.equalsIgnoreCase(jsonObject1.getString("s_no"))){
//
//                                    email=jsonObject1.getString("email");
//                                    contact=jsonObject1.getString("contact");
//                                    crop=jsonObject1.getString("crops");
//                                    crop_rate=jsonObject1.getString("crop_rate");
//                                    decription=jsonObject1.getString("description");
//
//                                }
//                            }


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


//                params.put("s_no",s_no);
//                Log.e("s_no",s_no);
                params.put("sno",s_no);
                Log.e("sno",s_no);
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
