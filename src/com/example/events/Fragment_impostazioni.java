package com.example.events;




import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_impostazioni extends Fragment{




	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
		View view= inflater.inflate(R.layout.fragment_impostazioni,container,false);

		SeekBar S = (SeekBar)view.findViewById(R.id.seekBar1);
		final TextView T = (TextView) view.findViewById(R.id.textkm);
		
		





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