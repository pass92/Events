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
 
public class AdapterListView extends ArrayAdapter<EventsHelper> {
 
        private final Context context;
        private final List<EventsHelper> events;
 
        public AdapterListView(Context context, List<EventsHelper> events) {
 
            super(context, R.layout.row,events);

            this.context = context;
            this.events = events;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.row, parent, false);
 
            // 3. Get the two text view from the rowView
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view_event);
            TextView titleView = (TextView) rowView.findViewById(R.id.title_event);
            TextView dataView = (TextView) rowView.findViewById(R.id.data_event);
            
            //List<EventsHelper> events = MainActivity.getListEvents();
            // 4. Set the text for textView 
            BitmapDrawable bdrawable = new BitmapDrawable(events.get(position).getPhoto());
            imageView.setBackgroundDrawable(bdrawable);
            
            titleView.setText(events.get(position).getTitle());
            dataView.setText(events.get(position).getStart_time());
            
            // 5. retrn rowView
            return rowView;
        }
        public List getListDisplayed() { return this.events; }
        public void setListEvent(EventsHelper event){
        	this.events.add(event);
        }
        
}