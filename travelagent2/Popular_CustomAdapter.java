package com.example.oscar.travelagent2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Popular_CustomAdapter extends ArrayAdapter<String>{

    public Popular_CustomAdapter(Context context, String[] locations) {
        super(context,R.layout.custom_popular_row,locations);
    }

    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_popular_row, parent, false);

        String singlelocation = getItem(position);
        TextView place = (TextView)customView.findViewById(R.id.poplace);
        ImageView icon = (ImageView)customView.findViewById(R.id.popicon);

        place.setText(singlelocation);
        icon.setImageResource(R.drawable.ic_thumb_up_black_24dp);

        return customView;
    }
}
