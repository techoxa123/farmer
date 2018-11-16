package com.example.priya.farmingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Crop_Detail extends AppCompatActivity {
    String crop="";
    Spinner spinner;
    final ArrayList<String> SPINNERLIST = new ArrayList<>(Arrays.asList("Select One", "Rice", "Ray", "Creals", "Moong", "Rice", "Crops", "Pulses", "Wheat", "Ray", "Moong"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop__detail);
        spinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                crop= SPINNERLIST.get(position);
                Toast.makeText(getBaseContext(), SPINNERLIST.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
