package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;
import android.util.Log;

public class WSClistarAtendimentosRealizadosUsuario extends WSCommand implements OnExecuteWsCommand {
	
	public static final String SUB_URI_ATENDIMENTOS_REALIZADOS = "/consulta/atendimentosrealizados";

	public WSClistarAtendimentosRealizadosUsuario(Context context, WebServiceConnection ws) throws InvalidParameterException {
		super(context, ws);
	}

	@Override
	public ResultAction executeWsCommand() {
		List<Atendimento> atendimentos;
		ResultAction result;
		Gson gson;
		
		Log.e("WSClistarAtendimentos", ws.mUsuario.getLogin() + " | " + ws.mUsuario.getSenha() + " | " +ws.mUsuario.getPerfil().getDescricao());
		
		result = execute(SUB_URI_ATENDIMENTOS_REALIZADOS, HttpMethods.GET, ws.mUsuario);
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_OK:
				gson = new Gson();
				atendimentos = gson.fromJson(ws.mJson, new TypeToken<List<Atendimento>>(){}.getType());
				if (atendimentos.size() > 0)
					return new ResultAction(R.string.ok, atendimentos, ResultAction.NOT_SHOW_MSG);
				else
					return new ResultAction(R.string.msg_no_service_found, null, ResultAction.NOT_SHOW_MSG);
			case HttpURLConnection.HTTP_NOT_FOUND:
				return new ResultAction(R.string.msg_resource_not_found);
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				return new ResultAction(R.string.msg_acess_denied);
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				return new ResultAction(R.string.msg_server_time_out);
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				result = new ResultAction(R.string.msg_server_error);
				break;
		}
		return new ResultAction(result.getCodResult());
	}

}
