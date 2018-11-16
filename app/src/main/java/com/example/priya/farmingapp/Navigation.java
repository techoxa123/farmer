package com.example.priya.farmingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , LocationListener  {

    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;
    TextView textView,textView1,textView2;
    String lat = "", longi = "";
    String address="",city="",country="";
    ActionBar actionBar;
    RecyclerView rv;
    String pname="",mobile="";
    ProgressDialog pd;
    String request_url="http://dailyupdatework.in/farmer_admin/api/category.php?";
   ArrayList<String> name= new ArrayList<>();
   ArrayList<String> photo= new ArrayList<>();
   String buyname="",buyaddress="",buyemail="",buycontect="";
    @SuppressLint("MissingPermission")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(Navigation.this);
        setContentView(R.layout.activity_navigation);


        locationManager = (LocationManager) getSystemService(Navigation.this.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) Navigation.this);


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Category");
        actionBar.show();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(gridLayoutManager);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        /// rv.setLayoutManager(linearLayoutManager);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getApplicationContext(),name,photo);
        rv.setAdapter(recycleViewAdapter);
        pd=new ProgressDialog(this);


//        if(hasLoggedIn)
//        {
//            //Go directly to main activity.
//        }
        getSerVerData();

        /**FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        textView=header.findViewById(R.id.textView1);
        textView1=header.findViewById(R.id.name);
        textView2=header.findViewById(R.id.phone);
//        SharedPreferences sharedPreferences = getSharedPreferences("mdePre",MODE_PRIVATE);
//        pname =sharedPreferences.getString("name","");
//        mobile=  sharedPreferences.getString("number","");
        SharedPreferences sharedPreferences2 = getSharedPreferences("buy_login",MODE_PRIVATE);
        buyname=sharedPreferences2.getString("name","");
        buyaddress=sharedPreferences2.getString("address","");
        buyemail=sharedPreferences2.getString("email","");
        buycontect=sharedPreferences2.getString("mobile","");
        textView1.setText(buyname);
        textView2.setText(buycontect);
        textView.setText(buyaddress);

    }

    private void getSerVerData() {
        pd= new ProgressDialog(Navigation.this);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue= Volley.newRequestQueue(Navigation.this);
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

                            JSONArray jsonArray=jsonObject.getJSONArray("success");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                SharedPreferences sharedPreferences = getSharedPreferences("mdePre",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("loginstatus",true);
                               // editor.putString("email", "").apply();
                                editor.apply();
                                editor.commit();
//                            String check=jsonObject.getString("success");
//                            if(check.equalsIgnoreCase("0")){
//                                Toast.makeText(Navigation.this, "No Data", Toast.LENGTH_SHORT).show();
//                            }else {
//                            check = jsonObject.getString("data");
                                name.add(jsonObject1.getString("category"));
                                photo.add(jsonObject1.getString("img"));
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
                            RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter(getApplicationContext(),name,photo);
                            rv.setAdapter(recycleViewAdapter);



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


//                params.put("category",category);
//                Log.e("category",category);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Navigation.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.category) {
            Intent intent=new Intent(getApplicationContext(),Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.market_price) {
            Intent intent =new Intent(getApplicationContext(),MarketPrice.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.weather_report) {
            Intent intent=new Intent(getApplicationContext(),WeatherReport.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.feedback) {
            Intent intent=new Intent(getApplicationContext(),Feedback.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if (id == R.id.privacy) {
            Intent intent=new Intent(getApplicationContext(),Privacy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if (id == R.id.about) {
            Intent intent=new Intent(getApplicationContext(),About.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if (id == R.id.logout) {
//            Intent intent = new Intent(getApplicationContext(), LogIn.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            //Getting out sharedpreferences

                            Intent intent = new Intent(Navigation.this, ContactInfo.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("buy_login",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Puting the value false for loggedin
                            editor.putString("loginstatus","t").apply();

                            //Putting blank value to email
                           // editor.putString(e, "");

                            //Saving the sharedpreferences
                            editor.commit();
                            editor.apply();

                            //Starting login activity

                            startActivity(intent);
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            //Showing the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else if (id == R.id.nav_share) {
            final String appPackageName = getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



        } else if (id == R.id.rating) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
