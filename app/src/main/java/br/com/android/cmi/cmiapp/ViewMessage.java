package br.com.android.cmi.cmiapp;

import android.app.Activity;
import android.widget.Toast;

public class ViewMessage{
	String message = null;
	Activity mParent;
	public ViewMessage(Activity parentActivity){
		mParent = parentActivity;
	}
	
	public void show(String message){
		Toast.makeText(mParent, message, Toast.LENGTH_SHORT).show();
	}
}
