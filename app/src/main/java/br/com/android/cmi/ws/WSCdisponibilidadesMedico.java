package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.DiaSemana;
import br.com.android.cmi.model.Disponibilidade;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

public class WSCdisponibilidadesMedico extends WSCommand implements
		OnExecuteWsCommand {
	
	private static final String MEDICO = "{idMedico}";
	private static final String CLINICA = "{cnpjClinica}";
	
	private static final String SUB_URI_DISPONIBILIDADE_MEDICO = "/medico/disponibilidade/" + MEDICO + "/" + CLINICA;
	
	private Integer mIdMedico;
	private String mCnpjClinica; 

	public WSCdisponibilidadesMedico(Context context, WebServiceConnection ws, Integer idMedico, String cnpjClinica)
			throws InvalidParameterException {
		super(context, ws);
		mIdMedico = idMedico;
		mCnpjClinica = cnpjClinica;
	}

	@Override
	public ResultAction executeWsCommand() {
		List<Disponibilidade> lista = new ArrayList<Disponibilidade>();
		ResultAction result = null;
		//Gson gson;
		result = execute(SUB_URI_DISPONIBILIDADE_MEDICO.replace(MEDICO, String.valueOf(mIdMedico))
				.replace(CLINICA, mCnpjClinica), HttpMethods.GET, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			//Gson dando PAU (BUG) porque quando o Gson convert a Disponibilidade no web service converte a hota no formato
			//americano com AM ou PM no final, mas na hora de converter o JSON para Disponibilidade não reconhece esse formato de data.
//			gson = new Gson();
//			lista = gson.fromJson(ws.mJson, new TypeToken<List<Disponibilidade>>(){}.getType());
			try {
				lista = jsonToListDisponibilidades(ws.mJson);
			} catch (JSONException e) {
				return new ResultAction(R.string.msg_io_error);
			}
			
			Log.d("WSCdisponibilidade", ws.mJson);
			for (Disponibilidade d :lista){
				Log.d("disponibilidade", d.toString());
			}
			
			return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_UNAUTHORIZED){
			return new ResultAction(R.string.msg_acess_denied);
		}
		return new ResultAction(R.string.msg_server_error);
	}
	
	private List<Disponibilidade> jsonToListDisponibilidades(String jsonString) throws JSONException{
		List<Disponibilidade> lista = new ArrayList<Disponibilidade>();
		JSONArray jsonDisponibilidades = new JSONArray(jsonString);
		for (int i = 0;i < jsonDisponibilidades.length(); i++){
			JSONObject jsonDisp = jsonDisponibilidades.getJSONObject(i);
			Disponibilidade disp = new Disponibilidade();
			disp.setCodDisponibilidade(jsonDisp.getInt("codDisponibilidade"));
			String horaInicial = jsonDisp.getString("horaInicial").trim();
			String horaFinal = jsonDisp.getString("horaFinal").trim();
			//horaInicial.replace(" AM", "");
			try {
				disp.setHoraInicial(stringToTime(horaInicial));
				disp.setHoraFinal(stringToTime(horaFinal));
			} catch (Exception e) {
				throw new JSONException(e.getMessage());
			}
			disp.setDiaSemana(DiaSemana.valueOf(jsonDisp.getString("diaSemana")));
			lista.add(disp);
		}
		return lista;
	}
	
	private Date stringToTime(String horaString) throws Exception{
		String sHora = "";
		String[] partesHora;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//GregorianCalendar gcHora = new GregorianCalendar();
		
		partesHora = horaString.split(":");
		
		if (partesHora[2].contains("AM")){
			sHora = horaString.replace("AM", "");
			return sdf.parse(sHora);
		}
		if (partesHora[2].contains("PM")){
			sHora = horaString.replace("PM", "");
			Integer hora = Integer.parseInt(partesHora[0]);
			if (hora != 12)
				hora += 12;
			if (hora == 24)
				hora = 0;
			partesHora[0] = String.valueOf(hora);
			if (partesHora[0].length()==1)
				partesHora[0] = "0" + partesHora[0];
			return sdf.parse(partesHora[0]+":"+partesHora[1]+":"+partesHora[2]);
		}
		return null;
	}

}
