package com.example.priya.farmingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity  implements LocationListener    {
    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;
    String lat = "", longi = "";
    private EditText namee, emaile, passworde, cpassword, mobbilee, addresse;
    public static String registrationApi = "http://dailyupdatework.in/farmer_admin/api/buyer_signup.php?";
    String name = "";
    String email = "";
    String pass = "";
    String cpass = "";
    String mobile = "";
    String add = "";
    int Permission_All=1;

    String[] PERMISSIOSN;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressDialog pd;
    private Button register;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Phone = "phoneKey";
    public static final String Address = "addkey";
    SharedPreferences sharedpreferences;
    String address="",city="",country="";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(Register.this);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        locationManager = (LocationManager) getSystemService(Register.this.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,  Register.this);


        //   tvLatitude = (TextView)findViewById(R.id.tv_latitud);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null) {
        //  pd.dismiss();
            Toast.makeText(getApplicationContext(), "GPS signal not found", Toast.LENGTH_SHORT).show();
        }
        if (location != null) {
            Log.e("locatin", "location--" + location);

            Log.e("latitude at beginning",
                    "@@@@@@@@@@@@@@@" + location.getLatitude());
            onLocationChanged(location);
        }




        namee = findViewById(R.id.nameedit);
        emaile = findViewById(R.id.emailedit);
        passworde = findViewById(R.id.passwordedit);
        cpassword = findViewById(R.id.cpasswordedit);
        mobbilee = findViewById(R.id.mobileedit);
        addresse = findViewById(R.id.addresssedit);
        register = findViewById(R.id.registerbtn);
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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),LogIn.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                name = namee.getText().toString().trim();
                email = emaile.getText().toString().trim();
                pass = passworde.getText().toString().trim();
                cpass = cpassword.getText().toString().trim();
                mobile = mobbilee.getText().toString().trim();
                add = addresse.getText().toString().trim();
                Matcher matcherObj = Pattern.compile(EMAIL_PATTERN).matcher(email);
                if (matcherObj.matches()) {
                    Toast.makeText(v.getContext(), email+" is valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), email+" is InValid", Toast.LENGTH_SHORT).show();
                }

                if(!pass.equals(cpass)) {
                    Toast.makeText(Register.this, "Password Not Matched", Toast.LENGTH_LONG).show();
                    }

                if (name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || pass.equalsIgnoreCase("") || cpass.equalsIgnoreCase("") || mobile.equalsIgnoreCase("") || add.equalsIgnoreCase("")) {
                   Toast.makeText(Register.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                    } else {

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.putString(Name, name);
                    editor.putString(Email,email);
                    editor.putString(Phone, mobile);
                    editor.putString(Address, add);
                    editor.commit();
                    editor.apply();
                    registerData();
                }

            }
        });


    }

    private void registerData() {
        pd = new ProgressDialog(Register.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        final String response = null;
        final String finalRespose = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, registrationApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String check = jsonObject.getString("success");

                            if (check.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(Register.this, LogIn.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("mdePre", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", name).apply();
                                editor.putString("number", mobile).apply();
                                editor.apply();
                                editor.commit();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, "Check EmailAddress", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();
//                        Log.d("ErrorResponse",finalRespose);
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

//                name=maan&address=panjab&contact=456456&email=maan@gmail.com&password=1237&c_pass=1237
                params.put("name", namee.getText().toString());
                Log.e("name", namee.getText().toString());

                params.put("email", emaile.getText().toString().trim());
                Log.e("email", emaile.getText().toString().trim());

                params.put("pass", passworde.getText().toString().trim());
                Log.e("pass", passworde.getText().toString().trim());

                params.put("contact", mobbilee.getText().toString().trim());
                Log.e("contact", mobbilee.getText().toString().trim());

                params.put("address", addresse.getText().toString().trim());
                Log.e("address", addresse.getText().toString().trim());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


    }






    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e("latitude", "latitude--" + latitude);

        try {
            Log.e("latitude", "inside latitude--" + latitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);





            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                Log.i("plot65dvjfh",address);
                city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
              //  Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
                //   tvLatitude.setText(address + " " + city + " " + country);


            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
        // tvLatitude.setText(address + " " + city + " " + country);
    }



}

