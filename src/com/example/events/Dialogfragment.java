package com.example.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Dialogfragment extends DialogFragment  {
	
	
	public interface NoticeDialogLIstener{
		public void onDialogPositiveClick();
		public void onDialogNegativeClick(); 
	}
	
	
	NoticeDialogLIstener mListener;
	
	public void onAttach(Fragment fragment){
		onAttach(fragment);
		try{
			mListener=(NoticeDialogLIstener) fragment;
		}catch(ClassCastException e){
			throw new ClassCastException(fragment.toString()+" must implement notice Dialog");
		}
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Inserisci un tag")
               .setPositiveButton("aggiugi tag", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the positive button event back to the host activity
                       mListener.onDialogPositiveClick();
                   }
               })
               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the negative button event back to the host activity
                       mListener.onDialogNegativeClick();
                   }
               });
        return builder.create();
    }

}
