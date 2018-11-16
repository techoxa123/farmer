package com.example.priya.farmingapp;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Atul's pc on 26/10/2018.
 */

class InterestsRecyclerAdapter extends RecyclerView.Adapter<InterestsRecyclerAdapter.ViewHolder> {
    Context context;
    String deleat_url="http://dailyupdatework.in/farmer_admin/api/aad_delete.php?";
    ProgressDialog pd;
    String email="";
    ArrayList<String> interestname=new ArrayList<>();
    ArrayList<String> interestcontect=new ArrayList<>();
    ArrayList<String> interestemail=new ArrayList<>();
    ArrayList<String> interestaddress=new ArrayList<>();
    ArrayList<String> Description=new ArrayList<>();

    public InterestsRecyclerAdapter(Context context,  ArrayList<String> description,ArrayList<String> interestName, ArrayList<String> interestContect, ArrayList<String> interestEmail, ArrayList<String> interestAddress) {
        this.context=context;
        this.interestname=interestName;
        this.interestcontect=interestContect;
        this.interestaddress=interestAddress;
        this.interestemail=interestEmail;
        this.Description=description;
    }

    @NonNull
    @Override
    public InterestsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interestlist, viewGroup, false);
        return new InterestsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(interestname.get(position));
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InterestDetail.class);
                intent.putExtra("name",interestname.get(position));
                intent.putExtra("address",interestaddress.get(position));
                intent.putExtra("contect",interestcontect.get(position));
                intent.putExtra("email",interestemail.get(position));
                intent.putExtra("description",Description.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
      holder.decline.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              email=interestemail.get(position);
            deleat();
          }
      });
    }

    private void deleat() {
        pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String response = null;
        final String finalRespose = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, deleat_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("res", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String check = jsonObject.getString("success");
                            if (check.equalsIgnoreCase("1")) {
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, FarmerData.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();
//                        Log.d("ErrorResponse",finalRespose);
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
//                name=maan&address=panjab&contact=456456&email=maan@gmail.com&password=1237&c_pass=1237

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    @Override
    public int getItemCount() {
        return interestname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button accept,decline;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            accept=(Button)itemView.findViewById(R.id.accept);
            decline=(Button)itemView.findViewById(R.id.decline);
        }
    }
}
