package com.example.priya.farmingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InterestDetail extends AppCompatActivity {
    TextView mobile,email,address,name,des;
    String mobiles="",emails="",addresss="",names="",description="";
    Button call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_detail);
        mobile=(TextView)findViewById(R.id.phone);
        email=(TextView)findViewById(R.id.email);
        address=(TextView)findViewById(R.id.address);
        name=(TextView)findViewById(R.id.name);
        call=(Button)findViewById(R.id.call);
        des=(TextView)findViewById(R.id.description);
        description=getIntent().getStringExtra("description");
        mobiles=getIntent().getStringExtra("contect");
        emails=getIntent().getStringExtra("email");
        addresss=getIntent().getStringExtra("address");
        names=getIntent().getStringExtra("name");
        mobile.setText(mobiles);
        email.setText(emails);
        name.setText(names);
        address.setText(addresss);
        des.setText(description);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDial();
            }
        });

}
    private void performDial() {
        Intent dial = new Intent(Intent.ACTION_CALL);
        dial.setData(Uri.parse("tel:"+mobiles));

        if (ActivityCompat.checkSelfPermission(InterestDetail.this,     Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(InterestDetail.this, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
            requestPermissions();
            return;

        }
        startActivity(dial);


    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},1);

    }

}
