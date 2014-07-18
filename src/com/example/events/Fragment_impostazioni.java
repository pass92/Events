package com.example.events;




import com.example.events.Dialogfragment.NoticeDialogLIstener;
import com.facebook.Session;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_impostazioni extends Fragment {

    @Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
		final View view= inflater.inflate(R.layout.fragment_impostazioni,container,false);
TextView tx=(TextView)view.findViewById(R.id.text_NomeUtente);

		SeekBar S = (SeekBar)view.findViewById(R.id.seekBar1);
		final TextView T = (TextView) view.findViewById(R.id.textkm);
		final ViewGroup mContainerView = (ViewGroup) view.findViewById(R.id.cont);
		ImageButton Btn1 = (ImageButton) view.findViewById(R.id.additem);
		SharedPreferences userDetails = getActivity().getApplicationContext().getSharedPreferences("userdetails",getActivity().getApplicationContext().MODE_PRIVATE);
		String km = userDetails.getString("km", "");
		
		if(km != null)
			try{
				S.setProgress(Integer.parseInt(km));
				T.setText(Integer.parseInt(km)+" km");
			}catch(NumberFormatException ex){
				
			}
		

		S.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			Integer progressChanged = 0;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				progressChanged = progress;
				T.setText(progressChanged+" km");

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				
				
				saveUserDatails(progressChanged);
				
			}
		});
		
		
		Btn1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				final Context context=getActivity();
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
	            alert.setTitle("Alert Dialog"); //Set Alert dialog title here
	            alert.setMessage("Enter TAG"); //Message here
	            final ViewGroup newView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.list_item_example,mContainerView,false);
	           newView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("view: "+v.getId());
					
				}
			});
	            newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		                // Remove the row from its parent (the container view).
		                // Because mContainerView has android:animateLayoutChanges set to true,
		                // this removal is automatically animated.
		               mContainerView.removeView(newView);

		                
		            }
	            });
	 
	            // Set an EditText view to get user input 
	            final EditText input = new EditText(context);
	            alert.setView(input);
	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if(mContainerView.getChildCount()<=4){
	                	 mContainerView.addView(newView, 0);
	                     String srt = input.getEditableText().toString();
	                     if(srt.equals("")){
	                    	 ((TextView) newView.findViewById(android.R.id.text1)).setText("default"); 
	                     }
	                     else{
	                          ((TextView) newView.findViewById(android.R.id.text1)).setText(srt);
	                    // Toast.makeText(context,srt,Toast.LENGTH_LONG).show(); 
	                     }
	                } // End of onClick(DialogInterface dialog, int whichButton)
	                }
	            }); //End of alert.setPositiveButton
	            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int whichButton) {
	                    // Canceled.
	                	
	                      dialog.cancel();
	                 }
	            }); //End of alert.setNegativeButton
	                AlertDialog alertDialog = alert.create();
	                alertDialog.show();
	              
				
			}
				
				
				
			
		});
		
		Button b=(Button)view.findViewById(R.id.button_logout);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			System.out.println("Session.getActiveSession(): "+Session.getActiveSession());
				if (Session.getActiveSession() != null) {
				    Session.getActiveSession().closeAndClearTokenInformation();
				}
                
				Session.setActiveSession(null);
				
				 // definisco l'intenzione  
	            Intent intent = new Intent(getActivity().getApplicationContext(),LogActivity.class);  
	            // passo all'attivazione dell'activity Pagina.java   
	            startActivity(intent);
			}
          
		});
		
			

		return view;
	}
    
    
	private void saveUserDatails(Integer kmUser) {
		
		Context context;
		context = getActivity().getApplicationContext();
		SharedPreferences userDetails = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putString("km", kmUser.toString().trim());
		edit.commit();
		Toast.makeText(context, "Login details are saved..", 3000).show();
		
		
	}

	


	

}
