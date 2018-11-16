package com.example.priya.farmingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class DiscussArrayAdapter  extends ArrayAdapter<OneComment> {
    private TextView countryName;
    private List<OneComment> countries = new ArrayList<OneComment>();
    private LinearLayout wrapper;
    public DiscussArrayAdapter(Context context, int their_message) {
        super(context,their_message);
    }
    @Override
    public void add(OneComment object) {
        countries.add(object);
       super.add(object);
    }



    public int getCount() {
        return this.countries.size();
    }

    public OneComment getItem(int index) {
        return this.countries.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.their_message, parent, false);
        }


        OneComment coment;
        coment = getItem(position);

        countryName = (TextView) row.findViewById(R.id.name);

        countryName.setText(coment.comment);

//        countryName.setBackgroundResource(coment.left ? R.drawable.their_message : R.drawable.their_message);
//        wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



}
