package com.example.events;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_descritpion extends Fragment{
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_description,container,false);
	 return view;
	}
}
