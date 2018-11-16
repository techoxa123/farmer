package com.example.priya.farmingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
   Context context;
    ArrayList<String> name= new ArrayList<>();
   ArrayList<String> photo= new ArrayList<>();

    public RecycleViewAdapter(Context context, ArrayList<String>name,ArrayList<String>photo) {
        this.context=context;
        this.name=name;
        this.photo=photo;

    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.cardview,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(context).load(photo.get(position)).into(holder.imageView);
        holder.textView.setText(name.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SubCategory.class);
                intent.putExtra("name",name.get(position));
              //  intent.putExtra("image",img.get(position));
                context.startActivity(intent);

            }
        });
        }

    @NonNull


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.image);
            textView=(TextView) itemView.findViewById(R.id.name);


        }

    }

}
