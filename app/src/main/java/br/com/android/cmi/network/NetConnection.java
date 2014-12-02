package br.com.android.cmi.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.util.Log;

public class NetConnection {
    private Context mContext = null;
    
    public NetConnection(Context context){
        mContext = context;
        //Log.i(null, "NetConnectio - contrutor....");
    }
 
    public boolean exists(){
    	//if (mContext == null)
    	//	Log.i(null, "NetConnection.exists()..mContext ISNULL!!!..");
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Log.i(null, "NetConnection.exists()....getSystemService(Context.CONNECTIVITY_SERVICE)..executado");
       	if (connectivityManager.getActiveNetworkInfo() != null
	            && connectivityManager.getActiveNetworkInfo().isAvailable()  
	            && connectivityManager.getActiveNetworkInfo().isConnected()) {
       		return true;
       	}
   	  return false;
    }
    
}
