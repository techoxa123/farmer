package com.example.priya.farmingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {
    public static final String login_API=AIURL.url+"forgot_password.php?";
    EditText editemail,editpass,editconfpass;
    Button submit;
    String email="",pass="",cpass="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(ForgetPassword.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        editemail=(EditText)findViewById(R.id.email);
        editpass=((EditText)findViewById(R.id.password));
        editconfpass=(EditText)findViewById(R.id.cnfpassword);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=editemail.getText().toString().trim();
                pass=editpass.getText().toString().trim();
                cpass=editconfpass.getText().toString().trim();
                if(!pass.equals(cpass)) {
                    Toast.makeText(ForgetPassword.this, "Password Not Matched", Toast.LENGTH_LONG).show();

                } else {
                    Submit();


                }

            }
        });
    }

    private void Submit() {
        pd= new ProgressDialog(ForgetPassword.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(ForgetPassword.this);
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

                            String check = jsonObject.getString("success");

                            if (check.equalsIgnoreCase("1")){
                                Toast.makeText(ForgetPassword.this, "Password Matched", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ForgetPassword.this,LogIn.class);
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


                params.put("email",email);
                Log.e("email",email);

                params.put("new_password",pass);
                Log.e("new_password",pass);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


    }
    }

