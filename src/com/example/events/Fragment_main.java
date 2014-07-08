package com.example.events;

import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_main extends Fragment{
	Button b1;
	private static List<EventsHelper> events;
	//List<EventsHelper> events;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_main,container,false);
	 final Communicator comm;
	 comm=(Communicator) getActivity();
	 ViewGroup l=(ViewGroup)view.findViewById(R.id.layoutTest);

	 if(MainActivity.getListEvents().size()==0){
	 DownloadEventsTask taskEvents = new DownloadEventsTask(view,comm,l);
	 taskEvents.execute();	 
	 }
	 else{
            events=MainActivity.getListEvents();
            int count=0;
			for(int i=99;i>89;i--){
				Button b11= new Button(view.getContext());
				b11.setId(i);
				//b11.setId(events.get(i).getId().toString());
				b11.setText(""+events.get(i).getDescription());
				b11.setMaxHeight(70);
				if(count%2==0){
			        b11.setBackgroundResource(R.drawable.festa);
				}
				else{
					b11.setBackgroundResource((R.drawable.festa));
				}
			    b11.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//System.out.println(v.getId());
						MainActivity.setidEvents(v.getId());
						
						
						comm.respond("fragment_event",v.getId());
						// TODO Auto-generated method stub
						
					}
				});
			   
				l.addView(b11);
				Button b10= new Button(view.getContext());
				b10.setText(""+events.get(i).getStart_time());
			    b10.setHeight(75);
			    l.addView(b10);
			    count++;
			}
	 }
	
	 
	 return view;
	 } 
}
/*  Bitmap contact_pic;    //a picture to show in drawable
    Drawable drawable=new BitmapDrawable(contact_pic); 
    Drawable d =new BitmapDrawable(getResources(),bitmap);
*/
