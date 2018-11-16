package com.example.priya.farmingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {
    Intent intent;
    SharedPreferences mprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(SplashScreen.this);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Intent intent=new Intent(SplashScreen.this,ContactInfo.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
               }
        },3000);
//        mprefs=getSharedPreferences("mPrfs",MODE_PRIVATE);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!mprefs.getString("loginstatus","").equalsIgnoreCase("true")) {
//                    intent = new Intent(getApplicationContext(),LogIn.class);
//                } else {
//                    intent = new Intent(getApplicationContext(), Navigation.class);
//                }
//
//                startActivity(intent);
//                finish();
//            }
//        },3000);
    }
}
