package com.example.events;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AdapterUser extends ArrayAdapter<UserHelper> {
 
        private final Context context;
        private final List<UserHelper> user;

        
        public AdapterUser(Context context, List<UserHelper> user) {
 
            super(context, R.layout.row_user, user);

            this.context = context;
            this.user = user;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.row_user, parent, false);
 
            // 3. Get the two text view from the rowView
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view_user);
            TextView titleView = (TextView) rowView.findViewById(R.id.name_user);
            
            //List<EventsHelper> events = MainActivity.getListEvents();
            // 4. Set the text for textView 
            
            //BitmapDrawable bdrawable = new BitmapDrawable(events.get(position).getPhoto());
            //imageView.setBackgroundDrawable(bdrawable);

            titleView.setText(user.get(position).getName());
            
            // 5. retrn rowView
            return rowView;
        }
        
}