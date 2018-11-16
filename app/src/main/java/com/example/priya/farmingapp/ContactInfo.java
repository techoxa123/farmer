package com.example.priya.farmingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactInfo extends AppCompatActivity {
    Button buybtn,farbtn;
    SharedPreferences mprefs,mprefs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        getSupportActionBar().hide();
        farbtn=findViewById(R.id.farmerbtn);
        buybtn=findViewById(R.id.buyerbtn);
        mprefs=getSharedPreferences("farmer_data",MODE_PRIVATE);
        mprefs2=getSharedPreferences("buy_login",MODE_PRIVATE);

        farbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mprefs.getString("loginstatus","").equalsIgnoreCase("true")) {
                    Intent intent = new Intent(getApplicationContext(), FarmerLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), FarmerData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mprefs2.getString("loginstatus","").equalsIgnoreCase("true")) {
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), Navigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
