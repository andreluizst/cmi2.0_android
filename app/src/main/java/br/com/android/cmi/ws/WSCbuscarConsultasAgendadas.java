package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;
import android.util.Log;

public class WSCbuscarConsultasAgendadas extends WSCommand implements OnExecuteWsCommand {
	
	private static final String PARAM_PACIENTE = "{idPaciente}";
	private static final String PARAM_NOME_MEDICO = "{nomeMedico}";
	private static final String PARAM_DIA = "{dia}";
	private static final String PARAM_MES = "{mes}";
	private static final String PARAM_ANO = "{ano}";
	
	public static final String SUB_URI_CONSULTAS_DO_PACIENTE = "/consulta/historico/" + PARAM_PACIENTE + "/" + PARAM_NOME_MEDICO + "/" 
								+ PARAM_DIA + "/" + PARAM_MES + "/" + PARAM_ANO;
	
	private Integer mCodigoPaciente;
	private String mNomeMedico;
	private Integer mDia;
	private Integer mMes;
	private Integer mAno;
	
	public WSCbuscarConsultasAgendadas(Context context, WebServiceConnection ws, Integer codigoPaciente, String nomeMedico, 
			Integer dia, Integer mes, Integer ano) throws InvalidParameterException {
		super(context, ws);
		mCodigoPaciente = codigoPaciente;
		mNomeMedico = nomeMedico;
		mDia = dia;
		mMes = mes;
		mAno = ano;
	}

	@Override
	public ResultAction executeWsCommand() {
		List<Consulta> lista = new ArrayList<Consulta>();
		Gson gson;
		ResultAction result;
		if (mCodigoPaciente == null)
			mCodigoPaciente = 0;
		if (mDia == null)
			mDia = 0;
		if (mMes == null)
			mMes = 0;
		if (mAno == null)
			mAno = 0;
		if (mNomeMedico == null)
			mNomeMedico = "0";
		if (mNomeMedico.length() == 0)
			mNomeMedico = "0";
		
		Log.e("WSCbuscarConsultas", ws.mUsuario.getLogin() + " | " + ws.mUsuario.getSenha() + " | " +ws.mUsuario.getPerfil().getDescricao());
		
		result = execute(SUB_URI_CONSULTAS_DO_PACIENTE.replace(PARAM_PACIENTE, String.valueOf(mCodigoPaciente))
				.replace(PARAM_NOME_MEDICO, mNomeMedico)
				.replace(PARAM_DIA, String.valueOf(mDia))
				.replace(PARAM_MES, String.valueOf(mMes))
				.replace(PARAM_ANO, String.valueOf(mAno)), HttpMethods.GET, ws.mUsuario);
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_OK:
				gson = new Gson();
				lista = gson.fromJson(ws.mJson, new TypeToken<List<Consulta>>(){}.getType());
//				if (lista.size() > 0)
					return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
//				else
//					return new ResultAction(R.string.msg_no_scheduled_appointments_found);
			case HttpURLConnection.HTTP_NOT_FOUND:
				return new ResultAction(R.string.msg_resource_not_found);
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				return new ResultAction(R.string.msg_acess_denied);
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				return new ResultAction(R.string.msg_server_time_out);
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				return new ResultAction(R.string.msg_server_error);
			case HttpURLConnection.HTTP_NO_CONTENT:
				return new ResultAction(R.string.msg_no_content);
		}
		return new ResultAction(result.getCodResult());
	}

}
