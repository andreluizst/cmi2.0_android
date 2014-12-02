package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import com.google.gson.Gson;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;
import android.util.Log;

public class WSCagendarConsulta extends WSCommand implements OnExecuteWsCommand {
	
	private static final String SUB_URI_AGENDAR_CONSULTA = "/consulta/agendar";

	private Consulta consulta;
	
	public WSCagendarConsulta(Context context, WebServiceConnection ws, Consulta consulta)
			throws InvalidParameterException {
		super(context, ws);
		this.consulta = consulta;
	}

	@Override
	public ResultAction executeWsCommand() {
		ResultAction result = null;
		Gson gson = new Gson();
		ws.mJson = gson.toJson(consulta);
		WSMessage wsMsg;
		result = execute(SUB_URI_AGENDAR_CONSULTA, HttpMethods.POST, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			
			Log.i("WSCagendarConsulta", "HttpURLConnection.HTTP_OK");
			
			wsMsg = gson.fromJson(ws.mJson, WSMessage.class);
			if (wsMsg.getServerCode() != HttpURLConnection.HTTP_OK)
				result = new ResultAction(wsMsg.getServerCode());
			else
				return new ResultAction(R.string.msg_scheduling_successful);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_UNAUTHORIZED){
			
			Log.e("WSCagendarConsulta", "HttpURLConnection.HTTP_UNAUTHORIZED");
			
			return new ResultAction(R.string.msg_acess_denied);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_NOT_FOUND){
			
			Log.e("WSCagendarConsulta", "HttpURLConnection.HTTP_NOT_FOUND");
			
			return new ResultAction(R.string.msg_resource_not_found);
		}
		
		Log.e("WSCagendarConsulta", "hove algum erro no servidor!!!!");
		return new ResultAction(R.string.msg_server_error);
	}

}
