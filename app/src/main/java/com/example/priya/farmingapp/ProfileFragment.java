package com.example.priya.farmingapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Context context;
    CircleImageView circleImageView;
    TextView text_name,text_email, text_contact,address,logout;
    ActionBar actionBar;
    Button button;
    ProgressDialog pd;
    String photo="",name="",email = "", contact = "", addresss = "", crop_rate = "", decription = "";
    String s_no = "";
    String category="";
    private Boolean CallEnded = false;

    public static Fragment newInstance() {
        ProfileFragment profileFragment=new ProfileFragment();
        return profileFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        circleImageView = (CircleImageView) view.findViewById(R.id.image);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("farmer_data", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        email = sharedPreferences.getString("email", "");
        contact = sharedPreferences.getString("contact", "");
        addresss = sharedPreferences.getString("address", "");
        text_name = (TextView) view.findViewById(R.id.name);
        text_name.setText(name);
        text_contact = (TextView) view.findViewById(R.id.phone);
        text_contact.setText(contact);
        text_email = (TextView) view.findViewById(R.id.email);
        text_email.setText(email);
        address = (TextView) view.findViewById(R.id.address);
        address.setText(addresss);
        logout = (TextView) view.findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setMessage("Are you sure you want to logout?");
//                alertDialogBuilder.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                                //Getting out sharedpreferences
//
//                                Intent intent = new Intent(context, ContactInfo.class);
//                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("buy_login",MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                                //Puting the value false for loggedin
//                                editor.putString("loginstatus","t").apply();
//
//                                //Putting blank value to email
//                                // editor.putString(e, "");
//
//                                //Saving the sharedpreferences
//                                editor.commit();
//                                editor.apply();
//
//                                //Starting login activity
//
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//
//                //Showing the alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Are you sure you want to logout?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                Intent intent = new Intent(getContext(), ContactInfo.class);


                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("farmer_data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Puting the value false for loggedin
                                editor.putString("loginstatus", "t").apply();

                                //Putting blank value to email
                                // editor.putString(e, "");

                                //Saving the sharedpreferences
                                editor.commit();
                                editor.apply();
                                startActivity(intent);
                                getActivity().finish();
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
        });

        return view;
    }


}
