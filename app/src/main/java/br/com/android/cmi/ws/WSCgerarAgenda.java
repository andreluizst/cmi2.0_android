package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Agenda;
import br.com.android.cmi.model.Clinica;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Mes;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;
import android.util.Log;

public class WSCgerarAgenda extends WSCommand implements OnExecuteWsCommand {
	
	private static final String MEDICO = "{idMedico}";
	private static final String CLINICA = "{cnpjClinica}";
	private static final String MES = "{idMes}";
	private static final String ANO = "{idAno}";
	
	private static final String SUB_URI_GERAR_AGENDA = "/medico/geraragenda/" + MEDICO + "/" + CLINICA + "/" + MES + "/" + ANO;
	
	private Integer idMedico;
	private String cnpj;
	private Integer iMes, iAno;
	
	
	public WSCgerarAgenda(Context context, WebServiceConnection ws, Funcionario funcionario, Clinica clinica, Mes mes)
			throws InvalidParameterException {
		super(context, ws);
		this.idMedico = funcionario.getCodFuncionario();
		this.cnpj = clinica.getCpf_cnpj();
		this.iMes = mes.getId()+1;
		this.iAno = mes.getAno();
	}
	

	@Override
	public ResultAction executeWsCommand() {
		List<Agenda> lista = new ArrayList<Agenda>();
		ResultAction result = null;
		Gson gson;
		result = execute(SUB_URI_GERAR_AGENDA.replace(MEDICO, String.valueOf(idMedico))
				.replace(CLINICA, cnpj).replace(MES, String.valueOf(iMes))
				.replace(ANO, String.valueOf(iAno)), HttpMethods.GET, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			
			Log.d("WSCgerarAgenda", ws.mJson);
			
			gson = new Gson();
			lista = gson.fromJson(ws.mJson, new TypeToken<List<Agenda>>(){}.getType());
			return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
		}
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				result = new ResultAction(R.string.msg_acess_denied);
				break;
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				result = new ResultAction(R.string.msg_server_time_out);
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:
				result = new ResultAction(R.string.msg_resource_not_found);
				break;
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				result = new ResultAction(R.string.msg_server_error);
				break;
		}
		return result;
	}

}
