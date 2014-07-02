package com.example.events;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_i_miei_eventi extends Fragment{
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_i_miei_eventi,container,false);
	 return view;
	}

}
