package com.example.events;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_partecipant extends Fragment{
	@Override 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
	 View view= inflater.inflate(R.layout.fragment_partecipant,container,false);
	 
	 
	 final  EditText tx=(EditText)view.findViewById(R.id.edit_text_email);
	 
	 Button b1=(Button)view.findViewById(R.id.button_send_email);
	 b1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println(tx.getText());
			String emailToSend=tx.getText().toString();
			
			if ((emailToSend.startsWith("www."))){//mettere controllo sull email
			      
				Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "INVITO");
				intent.putExtra(Intent.EXTRA_TEXT, "Hey! raggiungimi su BestEvents ");
				intent.setData(Uri.parse("mailto:"+emailToSend)); // or just "mailto:" for blank
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
				startActivity(intent);
				
				intent.setData(Uri.parse("mailto:default@recipient.com")); // or just "mailto:" for blank
			
			}
			else{
				Context context = getActivity().getApplicationContext();
				CharSequence text = "Inserire Email!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
	});
	 
	 
	 return view;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getView().removeCallbacks(null);
	}
}
