package br.com.android.cmi.ws;

import android.content.Context;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

public class WSCcancelarAgendamento extends WSCommand implements
		OnExecuteWsCommand {
	
	private static final String ID_CONSULTA = "{idConsulta}";
	private static final String SUB_URI_CANCELAR_CONSULTA = "/consulta/cancelar/" + ID_CONSULTA;;
	
	private Consulta mConsulta;

	public WSCcancelarAgendamento(Context context, WebServiceConnection ws, Consulta consulta)
			throws InvalidParameterException {
		super(context, ws);
		mConsulta = consulta;
	}

	@Override
	public ResultAction executeWsCommand() {
		ResultAction result = null;
//		Gson gson = new Gson();
//		WSMessage wsMsg;
		result = execute(SUB_URI_CANCELAR_CONSULTA.replace(ID_CONSULTA, String.valueOf(mConsulta.getCodConsulta())),
					HttpMethods.GET, ws.mUsuario);
		
		//Log.e("cancelarAgendamentoTask", "código do resultado="+result.getCodResult());
		
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_OK:
				//lista = gson.fromJson(ws.mJson, new TypeToken<List<Consulta>>(){}.getType());
				return new ResultAction(R.string.msg_cancellation_done);
			case HttpURLConnection.HTTP_NOT_FOUND:
				return new ResultAction(R.string.msg_resource_not_found);
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				return new ResultAction(R.string.msg_acess_denied);
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				return new ResultAction(R.string.msg_server_time_out);
		}
		return new ResultAction(result.getCodResult());
	}

}
