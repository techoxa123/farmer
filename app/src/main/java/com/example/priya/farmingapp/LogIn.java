package com.example.priya.farmingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

    public class LogIn extends AppCompatActivity  {
        EditText emailee,passee;
    Button login;
    public static final String SHARED_PREF_NAME = "myloginapp";
    TextView tv,forget;
    public static final String login_API="http://dailyupdatework.in/farmer_admin/api/buyer_login.php?";
    String email="";String pass="";
    String name="",phone="",add="";
    ProgressDialog pd;
    private CheckBox remember;
    private boolean loggedIn = false;
    int Permission_All=1;
    String[] PERMISSIOSN;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(LogIn.this);
        setContentView(R.layout.activity_log_in);
        emailee=findViewById(R.id.emailedit);
        passee=findViewById(R.id.passwordedit);
        remember=findViewById(R.id.remembercheckbox);
        login=findViewById(R.id.btn_login);
        tv=findViewById(R.id.link_signup);
        forget=(TextView)findViewById(R.id.forgetpassword);
        getSupportActionBar().hide();

        PERMISSIOSN = new String[]{android.Manifest.permission.INTERNET, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        // List of permissions required

        for (String permission : PERMISSIOSN) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIOSN, Permission_All);
                }
            }
        }

//        SharedPreferences sharedPreferences = getSharedPreferences("mdePre",MODE_PRIVATE);
//        loggedIn = sharedPreferences.getBoolean("User Login", false);
//        if(loggedIn){
//                //We will start the Profile Activity
//                Intent intent = new Intent(LogIn.this, Navigation.class);
//                startActivity(intent);
//                }




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),Navigation.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                email=emailee.getText().toString().trim();
                pass=passee.getText().toString().trim();

                if (email.equalsIgnoreCase(""))

                {
                    emailee.setError("Can'nt be blank");
                }
                else if (pass.equalsIgnoreCase(""))
                {
                    passee.setError("Can'nt be blank");
                }
                else {
                    UserLogin();
                }

            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgetPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    private void UserLogin() {
        pd= new ProgressDialog(LogIn.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(LogIn.this);
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,login_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String check=jsonObject.getString("success");
                            if(check.equalsIgnoreCase("0")){
                                Toast.makeText(LogIn.this, "Some error occured", Toast.LENGTH_SHORT).show();
                            }else {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("success");
                                email=jsonObject1.getString("email");
                                name=jsonObject1.getString("name");
                                add=jsonObject1.getString("address");
                                phone=jsonObject1.getString("contact");

                               // pass=jsonObject1.getString("Password");

                                Intent intent=new Intent(LogIn.this,Navigation.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("buy_login",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email).apply();
                                editor.putString("name",name).apply();
                                editor.putString("address",add).apply();
                                editor.putString("mobile",phone).apply();
                                editor.putString("loginstatus","true");
                                editor.apply();
                                editor.commit();
                                Toast.makeText(LogIn.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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
//                        Log.d("ErrorResponse",finalRespose);
                    }
                }
        )
        {
            protected Map<String, String> getParams()
            {
                Map<String,String> params= new HashMap<>();

//                Email=ggh@gmail.com&Password=123
                params.put("email",email);
                Log.e("Email",email);

                params.put("pass",pass);
                Log.e("Password",pass);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


    }
    }
