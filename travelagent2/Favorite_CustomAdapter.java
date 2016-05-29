package com.example.oscar.travelagent2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Favorite_CustomAdapter extends ArrayAdapter<String> {



    public Favorite_CustomAdapter(Context context, String[] locations) {
        super(context,R.layout.custom_favorite_row,locations);
    }

    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_favorite_row, parent, false);

        String singlelocation = getItem(position);
        TextView place = (TextView)customView.findViewById(R.id.favorite_place);
        ImageView icon = (ImageView)customView.findViewById(R.id.favorite_icon);

        place.setText(singlelocation);
        icon.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        return customView;
    }

}
