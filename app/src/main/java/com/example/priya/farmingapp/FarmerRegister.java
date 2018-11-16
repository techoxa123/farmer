package com.example.priya.farmingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FarmerRegister extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;
    String lat = "", longi = "";
    private EditText namee, emaile, passworde, cpassword, mobbilee, addresse, price;
    public static String registrationApi = "http://dailyupdatework.in/farmer_admin/api/farmer_signup.php?";
    String name = "", email = "",image="";
    String pass = "";
    String cpass = "";
    String mobile = "";
    String add = "";
    String spinners = "";
    //String crop="";
    String des="";
    String rate = "";
    int Permission_All = 1;
    SharedPreferences sharedpreferences;
    String address = "", city = "", country = "";
    String[] PERMISSIOSN;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressDialog pd;
    private Button fregister;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Spinner spinner;
    final ArrayList<String> SPINNERLIST = new ArrayList<>(Arrays.asList("Select One", "Rice", "Ray", "Creals", "Moong", "Rice", "Crops", "Pulses", "Wheat", "Ray", "Moong"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_register);
        getSupportActionBar().hide();

        locationManager = (LocationManager) getSystemService(FarmerRegister.this.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, FarmerRegister.this);


        //   tvLatitude = (TextView)findViewById(R.id.tv_latitud);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null) {
            //  pd.dismiss();
            Toast.makeText(getApplicationContext(), "GPS signal not found",
                    Toast.LENGTH_SHORT).show();
        }
        if (location != null) {
            Log.e("locatin", "location--" + location);

            Log.e("latitude at beginning",
                    "@@@@@@@@@@@@@@@" + location.getLatitude());
            onLocationChanged(location);
        }
        namee=findViewById(R.id.nameedit);
        emaile=findViewById(R.id.emailedit);
        passworde=findViewById(R.id.passwordedit);
        cpassword=findViewById(R.id.cpasswordedit);
        mobbilee=findViewById(R.id.mobileedit);
        addresse=findViewById(R.id.addresssedit);
        price=findViewById(R.id.priceedit);
        fregister=findViewById(R.id.farmerregisterbtn);
        spinner=(Spinner) findViewById(R.id.spinner);
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinners= SPINNERLIST.get(position);
                Toast.makeText(getBaseContext(), SPINNERLIST.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),FarmerLogin.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                name = namee.getText().toString().trim();
                email = emaile.getText().toString().trim();
                pass = passworde.getText().toString().trim();
                cpass = cpassword.getText().toString().trim();
                mobile = mobbilee.getText().toString().trim();
                add = addresse.getText().toString().trim();
                rate=price.getText().toString().trim();
                Matcher matcherObj = Pattern.compile(EMAIL_PATTERN).matcher(email);
                if (matcherObj.matches()) {
                    Toast.makeText(v.getContext(), email+" is valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), email+" is InValid", Toast.LENGTH_SHORT).show();
                }

                if(!pass.equals(cpass)) {
                    Toast.makeText(FarmerRegister.this, "Password Not Matched", Toast.LENGTH_LONG).show();
                }

                if (name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || pass.equalsIgnoreCase("") || cpass.equalsIgnoreCase("")||mobile.equalsIgnoreCase("")||add.equalsIgnoreCase("")) {
                    Toast.makeText(FarmerRegister.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();


                }else {
                    registerData();
                }

            }
        });
    }
    public  void registerData() {

        pd= new ProgressDialog(FarmerRegister.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(FarmerRegister.this);
        final String response=null;
        final  String finalRespose= response;

        StringRequest postRequest= new StringRequest(Request.Method.POST,registrationApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String check=jsonObject.getString("success");

                            if (check.equalsIgnoreCase("1"))
                            {
                                SharedPreferences sharedPreferences = getSharedPreferences("mdePre", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("farmer_photo",image);
                                editor.putString("farmer_name", name).apply();
                                editor.putString("email", email).apply();
                                editor.putString("mobile", mobile).apply();
                                editor.putString("crops_price",rate).apply();
                                editor.putString("crop_name",spinners).apply();
                                editor.putString("description",des).apply();
                                editor.putString("address",add);
                                editor.apply();
                                editor.commit();
                                Intent intent = new Intent(FarmerRegister.this,FarmerLogin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(FarmerRegister.this, "Error in your detail", Toast.LENGTH_SHORT).show();
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
//                fname=jayas&mobile=9978568923&femail=jay@gmail.com&cprice=300&category=wheat&cname=Creals&image=ght&desc=hghghj&password=123&address=Jaipur
                params.put("fname",namee.getText().toString());
                Log.e("fname",namee.getText().toString());

                params.put("femail",emaile.getText().toString());
                Log.e("femail",emaile.getText().toString());

                params.put("password",passworde.getText().toString());
                Log.e("password",passworde.getText().toString());

//                params.put("c_pass",cpassword.getText().toString());
//                Log.e("c_pass",cpassword.getText().toString());

                params.put("mobile",mobbilee.getText().toString());
                Log.e("mobile",mobbilee.getText().toString());
                params.put("address",addresse.getText().toString());
                Log.e("address",addresse.getText().toString());
//                params.put("cname",spinners);
//                Log.e("cname",spinners);
//                params.put("cprice",price.getText().toString());
//                Log.e("cprice",price.getText().toString());




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
                Log.i("plot65",address);
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
