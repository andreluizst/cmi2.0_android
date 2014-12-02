package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Especialidade;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class WSClistarEspecialidades extends WSCommand implements OnExecuteWsCommand {
	
	public static final String SUB_URI_FILTRO_ESPECIALIDADES = "/listagem/especialidades";

	
	public WSClistarEspecialidades(Context context, WebServiceConnection ws) throws InvalidParameterException {
		super(context, ws);
	}
	

	@Override
	public ResultAction executeWsCommand() {
		List<Especialidade> lista = new ArrayList<Especialidade>();
		ResultAction result = null;
		Gson gson = null;
		
		result = execute(SUB_URI_FILTRO_ESPECIALIDADES, HttpMethods.GET, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			gson = new Gson();
			lista = gson.fromJson(ws.mJson, new TypeToken<List<Especialidade>>(){}.getType());
			return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_UNAUTHORIZED){
			return new ResultAction(R.string.msg_acess_denied);
		}
		return new ResultAction(R.string.msg_server_error);
	}

}
