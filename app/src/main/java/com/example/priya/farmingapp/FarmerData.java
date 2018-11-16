package com.example.priya.farmingapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class FarmerData extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_data);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Farmer Details");
        actionBar.show();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.map:
                                selectedFragment =  MapFragment.newInstance();
                                loadfragment(selectedFragment);

                                break;
//                    mTextMessage.setText(R.string.title_near);
                            //return true;
                            case R.id.chart:
                                selectedFragment = ChartFragment.newInstance();
                                loadfragment(selectedFragment);
                                break;
//                    mTextMessage.setText(R.string.title_explore);
                            //  return true;

                            case R.id.chat:
//                    mTextMessage.setText(R.string.title_pop);
                                // return true;
                                selectedFragment = ChatFragment.newInstance();
                                loadfragment(selectedFragment);
                                break;
                            case R.id.account:
//                    mTextMessage.setText(R.string.title_cart);
                                /// return true;
                                selectedFragment =ProfileFragment.newInstance();
                                loadfragment(selectedFragment);
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new MapFragment());
        transaction.commit();

    }

    private void loadfragment(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
//                // if this doesn't work as desired, another possibility is to call `finish()` here.
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FarmerData.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerData.this);
//        alertDialogBuilder.setMessage("Are you sure you want to logout?");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Intent intent = new Intent(getApplicationContext(), FarmerLogin.class);
//                        SharedPreferences sharedPreferences = getSharedPreferences("farmer_data",MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        //Puting the value false for loggedin
//                        editor.putString("loginstatus","t").apply();
//
//                        //Putting blank value to email
//                        // editor.putString(e, "");
//
//                        //Saving the sharedpreferences
//                        editor.commit();
//                        editor.apply();
//
//
//
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });
//
//        //Showing the alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }
//        }
//
//    }
}
