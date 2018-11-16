package com.example.priya.farmingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FarmerLogin extends AppCompatActivity {
    EditText emailee,passee;
    Button login;
    public static final String SHARED_PREF_NAME = "myloginapp";
    TextView tv,forget;
    public static final String login_API="http://dailyupdatework.in/farmer_admin/api/farmer_login.php?";
    String email="";String pass="";
    String name="",phone="",add="";
    ProgressDialog pd;
    private CheckBox remember;
    private boolean loggedIn = false;
    String photo="",contact = "", crops = "", crop_rate = "", decription = "";
    int Permission_All=1;

    String[] PERMISSIOSN;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(FarmerLogin.this);
        setContentView(R.layout.activity_farmer_login);
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),FarmerData.class);
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
                Intent intent=new Intent(getApplicationContext(),FarmerRegister.class);
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
        pd= new ProgressDialog(FarmerLogin.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(FarmerLogin.this);
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
                            String check=jsonObject.getString("data");
                            if(check.equalsIgnoreCase("0")){
                                Toast.makeText(FarmerLogin.this, "Some error occured", Toast.LENGTH_SHORT).show();
                            }else {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                //email=jsonObject1.getString("email");

                                // pass=jsonObject1.getString("Password");
//                                photo=jsonObject1.getString("farmer_photo");
                                name=jsonObject1.getString("farmer_name");
                                email=jsonObject1.getString("email");
                                contact=jsonObject1.getString("mobile");
                                decription=jsonObject1.getString("description");
                                add=jsonObject1.getString("address");
                                SharedPreferences sharedPreferences = getSharedPreferences("farmer_data",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",email).apply();
                                editor.putString("name",name).apply();
                                editor.putString("contact",contact).apply();
                                editor.putString("address",add).apply();
                                editor.putString("loginstatus","true");
                                editor.apply();
                                editor.commit();
                                Intent intent=new Intent(FarmerLogin.this,FarmerData.class);
                                Toast.makeText(FarmerLogin.this, "Successfully Login", Toast.LENGTH_SHORT).show();
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

//                email=neha@gmail.com&password=123
                params.put("email",email);
                Log.e("email",email);

                params.put("password",pass);
                Log.e("password",pass);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


    }
}
