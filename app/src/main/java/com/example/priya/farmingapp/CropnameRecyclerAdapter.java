package com.example.priya.farmingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Atul's pc on 25/10/2018.
 */

class CropnameRecyclerAdapter extends RecyclerView.Adapter<CropnameRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<String> cropname=new ArrayList<>();
    ArrayList<String> cropPrice=new ArrayList<>();


    public CropnameRecyclerAdapter(Context context, ArrayList<String> cropName, ArrayList<String> cropPrice) {
        this.context=context;
        this.cropname=cropName;
        this.cropPrice=cropPrice;
    }

    @NonNull
    @Override
    public CropnameRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cropname, viewGroup, false);

        return new CropnameRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.Cropname.setText(cropname.get(position));
    holder.price.setText("₹. "+cropPrice.get(position)+" /quintile");
        Log.e("CROP",cropname.get(position));
    }


    @Override
    public int getItemCount() {
        return cropname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Cropname,price;
        public ViewHolder(View itemView) {
            super(itemView);
            Cropname=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
        }
    }
}
