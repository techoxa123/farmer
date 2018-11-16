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

import de.hdodenhof.circleimageview.CircleImageView;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private Context context;
//    ArrayList<String> nameArray;
//    ArrayList<String> imageArray;
ArrayList<String>nameArray=new ArrayList<>();
    ArrayList<String>imageArray=new ArrayList<>();
    ArrayList<String>s_no=new ArrayList<>();
    ArrayList<String>category=new ArrayList<>();

    public RVAdapter(Context context, ArrayList<String> nameArray,ArrayList<String> imageArray, ArrayList<String>s_no,ArrayList<String>category ) {
        this.context=context;
        this.nameArray=nameArray;
        this.imageArray=imageArray;
        this.s_no=s_no;
        this.category=category;
    }


    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.list,viewGroup,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(nameArray.get(position));
//        if (imageArray.get(position).equalsIgnoreCase(""))
//        {
//            Picasso.with(context).load(R.drawable.placeholder).into(holder.circleImageView);
//
//        }
//
//        else
//            {
//                Picasso.with(context).load(imageArray.get(position)).into(holder.circleImageView);
//            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     Intent intent=new Intent(context,Profile.class);
                     intent.putExtra("s_no",s_no.get(position));
                     intent.putExtra("category",category.get(position));
                     context.startActivity(intent);
            }
        });
    }


    @NonNull


    @Override
    public int getItemCount() {
        return nameArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textView;
       CircleImageView circleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.name);
            circleImageView=itemView.findViewById(R.id.profile_image);


        }

    }
}
