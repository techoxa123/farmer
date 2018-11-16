package com.example.priya.farmingapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WeatherReport extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;
    TextView textView;
    String lat = "", longi = "";
    String weatherWebserviceURL = "";
    String city="";
    //the loading Dialog
    ProgressDialog pDialog;
    // Textview to show temperature and description
    TextView temperature, description;
    // background image
    ImageView weatherBackground;
    // JSON object that contains weather information
    JSONObject jsonObj;

    ActionBar actionBar;
    String address="",country="";
    @SuppressLint("MissingPermission")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(WeatherReport.this);
        setContentView(R.layout.activity_weather_report);
        locationManager = (LocationManager) getSystemService(WeatherReport.this.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,  WeatherReport.this);


        //   tvLatitude = (TextView)findViewById(R.id.tv_latitud);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null) {
            //pd.dismiss();
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
        actionBar.setTitle("Weaher Report");
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
       /* AdRequest adRequest = new AdRequest.Builder().build();

        adView = (AdView) findViewById(R.id.adViewz);
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (adView.getVisibility() == View.GONE) {
                    adView.setVisibility(View.VISIBLE);
                }

            }
        });*/
         textView=(TextView)findViewById(R.id.city);
        textView.setText(city);
        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.description);
        weatherBackground = (ImageView) findViewById(R.id.weatherbackground);


        // prepare the loading Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait while retrieving the weather condition ...");
        pDialog.setCancelable(false);

        // Check if Internet is working
        if (!isNetworkAvailable(this)) {
            // Show a message to the user to check his Internet
            Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_LONG).show();
        } else {

            pDialog.show();

            // make HTTP request to retrieve the weather
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2156e2dd5b92590ab69c0ae1b2d24586&units=metric", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Parsing json object response
                        // response will be a json object


                        jsonObj = (JSONObject) response.getJSONArray("weather").get(0);
                        // display weather description into the "description textview"

                        description.setText(jsonObj.getString("description"));
                        // display the temperature
                        temperature.setText(response.getJSONObject("main").getString("temp") + " °C");

                        String backgroundImage = "";

                        //choose the image to set as background according to weather condition
                        if (jsonObj.getString("main").equals("Clouds")) {
                            backgroundImage = "https://marwendoukh.files.wordpress.com/2017/01/clouds-wallpaper2.jpg";
                        } else if (jsonObj.getString("main").equals("Rain")) {
                            backgroundImage = "https://marwendoukh.files.wordpress.com/2017/01/rainy-wallpaper1.jpg";
                        } else if (jsonObj.getString("main").equals("Snow")) {
                            backgroundImage = "https://marwendoukh.files.wordpress.com/2017/01/snow-wallpaper1.jpg";
                        }

                        // load image from link and display it on background
                        // We'll use the Glide library
                        Glide
                                .with(getApplicationContext())
                                .load(backgroundImage)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        System.out.println(e.toString());
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(weatherBackground);

                        // hide the loading Dialog
                        pDialog.dismiss();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error , try again ! ", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    pDialog.dismiss();
                }
            });

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(jsonObjReq);


        }

    }

    ////////////////////check internet connection
    public boolean isNetworkAvailable(final WeatherReport context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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
                  Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
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
