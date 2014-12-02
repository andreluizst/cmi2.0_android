package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Cidade;
import br.com.android.cmi.model.Estado;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;

public class WSClistarCidadesDoEstado extends WSCommand implements OnExecuteWsCommand {
	
	public static final String SUB_URI_FILTRO_CIDADES_DO_ESTADO = "/listagem/cidades/{pUF}";
	
	private Estado mEstado;

	public WSClistarCidadesDoEstado(Context context, WebServiceConnection ws, Estado estado)
			throws InvalidParameterException {
		super(context, ws);
		if (estado == null)
			throw new InvalidParameterException("estado=nulo");
		if (estado.getUf() == null)
			throw new InvalidParameterException("estado.uf=nulo");
		this.mEstado = estado; 
	}

	@Override
	public ResultAction executeWsCommand() {
		List<Cidade> lista = new ArrayList<Cidade>();
		ResultAction result = null;
		Gson gson = null;
		
		result = execute(SUB_URI_FILTRO_CIDADES_DO_ESTADO.replace("{pUF}", mEstado.getUf()), HttpMethods.GET, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			gson = new Gson();
			lista = gson.fromJson(ws.mJson, new TypeToken<List<Cidade>>(){}.getType());
			return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_UNAUTHORIZED){
			return new ResultAction(R.string.msg_acess_denied);
		}
		return new ResultAction(R.string.msg_server_error);
	}

}
