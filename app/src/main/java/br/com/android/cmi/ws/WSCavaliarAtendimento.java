package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import android.content.Context;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

import com.google.gson.Gson;

public class WSCavaliarAtendimento extends WSCommand implements OnExecuteWsCommand {
	
	private static final String SUB_URI_AVALIAR_ATENDIMENTO = "/consulta/avaliaratendimento";
	
	private Atendimento mAtendimento;

	public WSCavaliarAtendimento(Context context, WebServiceConnection ws, Atendimento atendimento) throws InvalidParameterException {
		super(context, ws);
		mAtendimento = atendimento;
	}

	@Override
	public ResultAction executeWsCommand() {
		ResultAction result = null;
		Gson gson = new Gson();
		ws.mJson = gson.toJson(mAtendimento);
		WSMessage wsMsg;
		result = execute(SUB_URI_AVALIAR_ATENDIMENTO, HttpMethods.POST, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			gson = new Gson();
			wsMsg = gson.fromJson(ws.mJson, WSMessage.class);
			if (wsMsg.getServerCode() == HttpURLConnection.HTTP_OK)
				return new ResultAction(R.string.msg_evaluation_performed);
			else
				result = new ResultAction(wsMsg.getServerCode());
		}
		switch (result.getCodResult()){
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
