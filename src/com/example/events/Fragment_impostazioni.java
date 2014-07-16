package com.example.events;




import com.example.events.Dialogfragment.NoticeDialogLIstener;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_impostazioni extends Fragment implements NoticeDialogLIstener{

    @Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
		final View view= inflater.inflate(R.layout.fragment_impostazioni,container,false);

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
				
				DialogFragment filtroDIalog = new Dialogfragment();
				filtroDIalog.show(getFragmentManager(), "missiles");
				
				final ViewGroup newView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.list_item_example,mContainerView,false);

		        // Set the text in the new row to a random country.
		        ((TextView) newView.findViewById(android.R.id.text1)).setText("ciao");

		        // Set a click listener for the "X" button in the row that will remove the row.
		        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		                // Remove the row from its parent (the container view).
		                // Because mContainerView has android:animateLayoutChanges set to true,
		                // this removal is automatically animated.
		               mContainerView.removeView(newView);

		                
		            }
		        });

		        // Because mContainerView has android:animateLayoutChanges set to true,
		        // adding this view is automatically animated.
		        mContainerView.addView(newView, 0);
				
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

	


	@Override
	public void onDialogPositiveClick() {
		
			
			
			/*final ViewGroup newView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.list_item_example,mContainerView,false);

	        // Set the text in the new row to a random country.
	        ((TextView) newView.findViewById(android.R.id.text1)).setText("ciao");

	        // Set a click listener for the "X" button in the row that will remove the row.
	        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Remove the row from its parent (the container view).
	                // Because mContainerView has android:animateLayoutChanges set to true,
	                // this removal is automatically animated.
	               mContainerView.removeView(newView);

	                
	            }
	        });

	        // Because mContainerView has android:animateLayoutChanges set to true,
	        // adding this view is automatically animated.
	        mContainerView.addView(newView, 0);
			*/
		
		
	}


	@Override
	public void onDialogNegativeClick() {
		// TODO Auto-generated method stub
		
	}
	

}