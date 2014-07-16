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

public class AdapterListViewCercaEvent extends ArrayAdapter<EventsHelper> {
	        private final ArrayList<EventsHelper> eventsmain;
	        private final Context context;
	        private final ArrayList<EventsHelper> events;
	        private int id;
	        private String year;
	        private String day;
	        private String month;
	        private String hour;
	        private String time;
	 
	        public AdapterListViewCercaEvent(Context context, ArrayList<EventsHelper> events) {
	 
	            super(context, R.layout.row,events);
	            eventsmain=(ArrayList<EventsHelper>) MainActivity.getListEvents();
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
	            time=new String(events.get(position).getStart_time());
	            year=new String(time.substring(0, 4));
	            month=new String(time.substring(5,7));
	            day=new String(time.substring(8, 10));
	            if(time.length()>=16){
	            	hour=new String(time.substring(11, 16));
	            }
	            else{
	            	hour=new String("null");
	            }
	            time=new String(day+"/"+month+"/"+year+" alle "+hour);
	            
	            
	            titleView.setText(events.get(position).getTitle());
	            dataView.setText(time);
	            imageView.setImageBitmap(events.get(position).getPhoto());
	            
	            // 5. retrn rowView
	            return rowView;
	        }
	        
	}

