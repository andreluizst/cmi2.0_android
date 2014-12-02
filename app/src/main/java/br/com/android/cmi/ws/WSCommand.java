package br.com.android.cmi.ws;

import android.content.Context;

import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Config;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.network.NetConnection;
import br.com.android.cmi.network.NoConnectionException;
import br.com.android.cmi.storage.RepositorioUsuario;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;


public abstract class WSCommand {
	protected WebServiceConnection ws;
	protected Config mConfig;
	protected RepositorioUsuario rep;
	protected String mUriServer;
	protected DataOutputStream mDataOutputStream;

	
	public WSCommand(Context context, WebServiceConnection ws) throws InvalidParameterException {
		if (context == null || ws == null)
			throw new InvalidParameterException();
		this.ws = ws;
		ws.netConnection = new NetConnection(context);
		mUriServer = null;
		rep = new RepositorioUsuario(context);
		mConfig = rep.getConfig();

		/*if (mConfig != null)
			Log.i("WSCommand...rep.getConfig()", mConfig.toString());
		else
			Log.i("WSCommand", "mConfig está NULO");
		*/
	}
	
	
	protected String getJson(){
		return ws.mJson;
	}
	
	protected void setJson(String json){
		ws.mJson = json;
	}
	
	protected ResultAction execute(String uriServerRoot, String subURI, HttpMethods httpMethod, Usuario usuario){
		mUriServer = uriServerRoot;
		return execute(subURI, httpMethod, usuario);
	}
	
	protected ResultAction execute(String subURI, HttpMethods httpMethod, Usuario usuario){
		HttpURLConnection con = null;
		
		//Log.i("WSCommand.execute", "mUsuario == " + ws.mUsuario.getLogin() + "|"+ws.mUsuario.getSenha());
		
		if (mUriServer == null){
			
			//Log.i("WSCommand.execute", "mUriServer == null...");
			
			if (mConfig != null && mConfig.getServerAddress() != null
					&& mConfig.getServerAddress().length() > 0) {
				
				//Log.i("WSCommand.execute", "Pegando endreço do servidor no respositório...");
				
				mUriServer = mConfig.getServerAddress();
			}else{
				mUriServer = WebServiceConnection.URI_CMI;
			}
		}
		try {
			
			//Log.i(null, "WSCommand.execute()...ws.open(...).");
			//Log.i("WSCommand.execute()", "URI=" + mUriServer + subURI);
			
			con = ws.open(mUriServer + subURI, httpMethod, usuario);
			if (httpMethod == HttpMethods.GET || httpMethod == HttpMethods.DELETE) {
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
					ws.mJson = WebServiceConnection.bytesToString(con.getInputStream());
					return new ResultAction(HttpURLConnection.HTTP_OK);
				}
			}
			if (httpMethod == HttpMethods.POST || httpMethod == HttpMethods.PUT){
				ws.writeJsonToOutpuStream(ws.mJson, con);
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
					ws.mJson = WebServiceConnection.bytesToString(con.getInputStream());
					return new ResultAction(HttpURLConnection.HTTP_OK);
				}
			}
			/*
			if (con.getErrorStream() != null ){
				Log.e("WSCommand", "ERRO: " + WebServiceConnection.bytesToString(con.getErrorStream()));
				Log.e("WSCoomand", "Mensagem: " + con.getResponseMessage());
			}
			Log.e("WSCommand.execute()", "getResponseCode()=" + con.getResponseCode());
			*/
			ResultAction result = new ResultAction(con.getResponseCode()); 
			switch (result.getCodResult()){
				case HttpURLConnection.HTTP_OK:
					return result;
				case HttpURLConnection.HTTP_NOT_FOUND:
					return result;
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					return result;
				case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
					return result;
				case HttpURLConnection.HTTP_NO_CONTENT:
					return result;
				default:
					return new ResultAction(HttpURLConnection.HTTP_INTERNAL_ERROR);
			}
		}catch(ConnectException cex){
			return new ResultAction(R.string.msg_could_not_connect_to_server);
		}catch(NoConnectionException conE){
			conE.printStackTrace();
			return new ResultAction(R.string.msg_no_connection);
		}catch(Exception ex){
			ex.printStackTrace();
			return new ResultAction(R.string.msg_unsuccessful_connection);
		}
	}

}
