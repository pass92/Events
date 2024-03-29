package com.example.adapter;


import java.util.ArrayList;
import java.util.List;

import com.example.events.MainActivity;
import com.example.events.R;
import com.example.events.R.id;
import com.example.events.R.layout;
import com.example.helper.EventsHelper;
import com.example.helper.StorageHelper;

import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
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
	 
	        	ViewHolderItem viewHolder;

	    		if (convertView == null) {
	    			 // inflate the layout
	    			LayoutInflater inflater = (LayoutInflater) context
	    					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	        convertView = inflater.inflate(R.layout.row, parent, false);


	    			// well set up the ViewHolder
	    			viewHolder = new ViewHolderItem();
	    			viewHolder.imageView = (ImageView) convertView
	    					.findViewById(R.id.image_view_event);
	    			viewHolder.title = (TextView) (TextView) convertView
	    					.findViewById(R.id.title_event);
	    			viewHolder.date = (TextView) convertView
	    					.findViewById(R.id.data_event);
	    			
	    			convertView.setTag(viewHolder);
	    			
	    		}
	    		else{
	    			
	    			viewHolder = (ViewHolderItem) convertView.getTag();
	    		}
	            
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
	            
	            
	            viewHolder.title.setText(events.get(position).getTitle());
	            viewHolder.date.setText(time);
	            Bitmap bitmapImage = StorageHelper.loadImageFromStorage(StorageHelper.pathStorage,events.get(position).getId());

	    		if(bitmapImage!=null){
	    		     BitmapDrawable bdrawable = new BitmapDrawable(bitmapImage);
	    		     viewHolder.imageView.setBackgroundDrawable(bdrawable);
	    		}
	    		else{
	    			 BitmapDrawable bdrawable = new BitmapDrawable(events.get(position).getPhoto());
	    			 viewHolder.imageView.setBackgroundDrawable(bdrawable);
	    			//imageView.setImageResource(R.drawable.default_event);
	    		}

	            // 5. retrn rowView
	            return convertView;
	        }
	    	private static class ViewHolderItem {
	    		ImageView imageView;
	    		TextView title;
	    		TextView date;
	    	}
	}

