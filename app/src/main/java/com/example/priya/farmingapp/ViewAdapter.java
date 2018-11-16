package com.example.priya.farmingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{
    Context context;
    ArrayList<String> price= new ArrayList<>();
    ArrayList<String> photo= new ArrayList<>();



    public ViewAdapter(Context context, ArrayList<String> price, ArrayList<String> photo) {
        this.context=context;
        this.price=price;
        this.photo=photo;
    }
    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.marketlist,viewGroup,false);
        ViewAdapter.ViewHolder viewHolder=new ViewAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(context).load(photo.get(position)).into(holder.imageView);
        holder.textView.setText(price.get(position));

    }

    @Override
    public int getItemCount() {
        return price.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.image);
            textView=(TextView) itemView.findViewById(R.id.cropprice);
        }
    }
}
