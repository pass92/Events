package com.example.events;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_main extends Fragment{
	Button b1;
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_main,container,false);
	 final Communicator comm;
	 comm=(Communicator) getActivity();
	 
	
	
	
	/* Bitmap bMap = BitmapFactory.decodeFile("//media/external/images/media/103");
	 if(bMap==null){
		 System.out.println("non vaaa");
	 }
	 else{
	 Drawable d =new BitmapDrawable(getResources(),bMap);
	 b1.setBackground(d);
	 }
	 */
	 
	 b1=(Button)view.findViewById(R.id.button_event);
	 b1.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				comm.respond("fragment_event");
				
			}
		});
	 Button b2=(Button)view.findViewById(R.id.button_tuoi_eventi);
	 b2.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			comm.respond("fragment_i_miei_eventi");
			
			
		}
	});
	 
	 
	 return view;
	 } 
}
/*  Bitmap contact_pic;    //a picture to show in drawable
    Drawable drawable=new BitmapDrawable(contact_pic); 
    Drawable d =new BitmapDrawable(getResources(),bitmap);
*/
