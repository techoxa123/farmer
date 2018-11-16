package com.example.priya.farmingapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MarketPrice extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;
    String lat = "", longi = "";
    String address="",city="",country="";
    String request_url="http://dailyupdatework.in/farmer_admin/api/price_fetch.php?";
    ActionBar actionBar;
    RecyclerView recyclerView;
    ProgressDialog pd;
    ArrayList<String> photo= new ArrayList<>();
    ArrayList<String> price= new ArrayList<>();
//    ArrayList<String> price= new ArrayList<>(Arrays.asList("200Rs","500Rs","600Rs","400Rs","700Rs","300Rs","800Rs","100Rs","400Rs","200Rs"));
//    ArrayList<String> photo= new ArrayList<>(Arrays.asList("https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://cdn.dnaindia.com/sites/default/files/styles/full/public/2016/07/11/480841-pulses-dna.jpg","https://i.ytimg.com/vi/a-vzOoq1pLc/hqdefault.jpg","https://www.agrifarming.in/wp-content/uploads/2015/05/Bajra-Cultivation.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg","https://www.platingsandpairings.com/wp-content/uploads/2017/03/instant-pot-perfect-rice-2.jpg"));
@SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MultiDex.install(MarketPrice.this);
        setContentView(R.layout.activity_market_price);

    locationManager = (LocationManager) getSystemService(MarketPrice.this.LOCATION_SERVICE);

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) MarketPrice.this);


    //   tvLatitude = (TextView)findViewById(R.id.tv_latitud);

    Criteria criteria = new Criteria();
    String bestProvider = locationManager.getBestProvider(criteria, true);
    Location location = locationManager.getLastKnownLocation(bestProvider);

    if (location == null) {
    pd.dismiss();
        Toast.makeText(getApplicationContext(), "GPS signal not found",
                Toast.LENGTH_SHORT).show();
    }
    if (location != null) {
        Log.e("locatin", "location--" + location);

        Log.e("latitude at beginning",
                "@@@@@@@@@@@@@@@" + location.getLatitude());
        onLocationChanged(location);
    }
        actionBar = getSupportActionBar();
        actionBar.setTitle("Market Price");
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        ViewAdapter viewAdapter = new ViewAdapter(getApplicationContext(),price,photo);
        recyclerView.setAdapter(viewAdapter);
        pd=new ProgressDialog(this);
        getSerVerData();



    }

    private void getSerVerData() {
        pd= new ProgressDialog(MarketPrice.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(MarketPrice.this);
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
                               /// if((city.equalsIgnoreCase(jsonObject1.getString("city"))))
//                            String check=jsonObject.getString("success");
//                            if(check.equalsIgnoreCase("0")){
//                                Toast.makeText(Navigation.this, "No Data", Toast.LENGTH_SHORT).show();
//                            }else {
//                            check = jsonObject.getString("data");
                                price.add(jsonObject1.getString("price"));
                                photo.add(jsonObject1.getString("image"));


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
                            RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter(getApplicationContext(),price,photo);
                            recyclerView.setAdapter(recycleViewAdapter);



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


                params.put("city",city);
                Log.e("city",city);
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
