package br.com.android.cmi.ws.task;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.cmiapp.ViewMessage;
import br.com.android.cmi.ws.OnExecuteWsCommand;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCommand;


public class TaskForWs extends AsyncTask<WSCommand, Integer, ResultAction> {
	private ProgressBar mProgressBar;
	private int mLastViewVisibility;
    private Context mContext;
    private ViewMessage mViewMessage; 
    //private String msg = null;
    private TextView txtv;
    private Integer mCodResult;
    private Fragment mFragment;
    
    
    public TaskForWs(Context context){
    	this(context, null, null);
    }
    
    public TaskForWs(Context context, ProgressBar progressBar){
    	this(context, progressBar, null);
    }
    
    public TaskForWs(Context context, ViewMessage viewMessage){
    	this(context, null, viewMessage);
    }
    
    public TaskForWs(Context context, ProgressBar progressBar, ViewMessage viewMessage){
    	mFragment = null;
    	mContext = context;
    	mProgressBar = progressBar;
    	mViewMessage = viewMessage;
    	txtv = new TextView(context);
    	mCodResult = -1;
    	if (mProgressBar != null)
    		mLastViewVisibility = mProgressBar.getVisibility();
    }
    
    public TaskForWs(Fragment fragment){
    	this(fragment, null, null);
    }
    
    public TaskForWs(Fragment fragment, ProgressBar progressBar){
    	this(fragment, progressBar, null);
    }
    
    public TaskForWs(Fragment fragment, ViewMessage viewMessage){
    	this(fragment, null, viewMessage);
    }
    
    public TaskForWs(Fragment fragment, ProgressBar progressBar, ViewMessage viewMessage){
    	mFragment = fragment;
    	mContext = fragment.getActivity();
    	mProgressBar = progressBar;
    	mViewMessage = viewMessage;
    	txtv = new TextView(fragment.getActivity());
    	mCodResult = -1;
    	if (mProgressBar != null)
    		mLastViewVisibility = mProgressBar.getVisibility();
    }
    
    
    @Override
    protected void onPreExecute()
    {	
    	if (mProgressBar != null){
    		mProgressBar.setMax(100);
    		mProgressBar.setIndeterminate(true);
    		mProgressBar.setVisibility(View.VISIBLE);
    	}
    }
    
    
    
    @Override
	protected ResultAction doInBackground(WSCommand... commands) {
    	//WSCommand wsc;
    	//wsc = commands[0];
    	publishProgress(0);
    	mCodResult = -1;
    	ResultAction resultado = null;
    	//String json = null;
		for (WSCommand wsc : commands) {
			if (wsc instanceof OnExecuteWsCommand) {
				try {
					resultado = ((OnExecuteWsCommand)wsc).executeWsCommand();
					mCodResult = resultado.getCodResult();
				} catch (Exception e) {
					e.printStackTrace();
					txtv.setText(R.string.msg_could_not_connect_to_server);
					return null;
				}
				return resultado;
			}
		}
//    	if (wsc instanceof WSCautenticarUsuario){
//			try{
//				resultado = ((WSCautenticarUsuario) wsc).autenticarUsuario();
//				mCodResult = resultado.getCodResult();
//			}catch(Exception e){
//				e.printStackTrace();
//				//msg = e.getMessage();
//				txtv.setText(R.string.msg_could_not_connect_to_server);
//				return resultado;
//			}
//			return resultado;
//		}
		/*try {
			//publishProgress(30);
			//for (int i = mProgressBar.getProgress(); i <= 60;i+=10)
				//publishProgress(i);
			//if (mViewMessage != null){
				//Log.i("Task.mTextMessage", msg);
			//}
			//progress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}*/
		//for (int i = mProgressBar.getProgress(); i <= 90;i+=10)
			//publishProgress(i);
		//Log.i("doInBackground", "return true;");
		return null;//mCodResult == R.string.connected;
	}
    
    @Override
    protected void onPostExecute(ResultAction resultAction){
    	super.onPostExecute(resultAction);
    	if (mCodResult > -1)
			txtv.setText(mCodResult);
    	/*try{
    		if (mCodResult > -1)
    			txtv.setText(mCodResult);
    		else
    			if (msg != null && !msg.isEmpty())
    				txtv.setText(Integer.valueOf(msg));
    	}catch(Exception e){
    		txtv.setText(R.string.msg_could_not_connect_to_server);
    	}*/
    	if (mViewMessage != null && resultAction.isCanShowMsg())
    		mViewMessage.show(txtv.getText().toString());//msg);
    	if (resultAction != null){
    		if (mFragment != null){
    			if (mFragment instanceof OnPostExecuteTask){
    				((OnPostExecuteTask)mFragment).postExecuteTask(resultAction);
    			}
    		}else
    			if (mContext instanceof OnPostExecuteTask){
    				((OnPostExecuteTask)mContext).postExecuteTask(resultAction);
    			}
    	}
    	if (mProgressBar != null){
    		mProgressBar.setIndeterminate(false);
    		mProgressBar.setVisibility(mLastViewVisibility);
    	}
    }
    
    @Override
    protected void onProgressUpdate(Integer... values){
    	super.onProgressUpdate(values);
    	/*Log.i("progresso", String.valueOf(values[0]));
    	mProgressBar.setProgress(values[0]);
    	mProgressBar.refreshDrawableState();*/
    }
}
