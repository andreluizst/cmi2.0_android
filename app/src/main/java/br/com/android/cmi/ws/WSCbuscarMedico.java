package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WSCbuscarMedico extends WSCommand implements OnExecuteWsCommand {
	private static final String NOME_MEDICO = "{pNomeMedico}";
	private static final String NOME_CLINICA = "{pNomeClinica}";
	private static final String UF = "{pUF}";
	private static final String CIDADE = "{pCidade}";
	private static final String ESPECIALIDADE = "{pEspecialidade}";
	private static final String PLANO_DE_SAUDE = "{pPlano}";
	
	public static final String SUB_URI_BUSCAR_MEDICO = "/medico/buscar/" + NOME_MEDICO +"/"+ NOME_CLINICA +"/"+ UF 
			+"/"+ CIDADE +"/"+ ESPECIALIDADE +"/"+ PLANO_DE_SAUDE;
	
	private String nomeMedico, nomeClinica, uf;
	private Integer codCidade;
	private Integer codEspecialidade;
	private Integer codPlano;
	
	public WSCbuscarMedico(Context context, WebServiceConnection ws, String nomeMedico, String nomeClinica, String uf,
			Integer codCidade, Integer codEspecialidade, Integer codPlano)
			throws InvalidParameterException {
		super(context, ws);
		this.nomeMedico = nomeMedico;
		this.nomeClinica = nomeClinica;
		this.uf = uf;
		this.codCidade = codCidade;
		this.codEspecialidade = codEspecialidade;
		this.codPlano = codPlano;
	}

	@Override
	public ResultAction executeWsCommand() {
		List<Usuario> preLista = new ArrayList<Usuario>();
		List<Funcionario> lista = new ArrayList<Funcionario>();
		ResultAction result = null;
		Gson gson = null;
		if (nomeMedico == null)
			nomeMedico = "0";
		if (nomeMedico != null && nomeMedico.length() == 0)
			nomeMedico = "0";
		if (nomeClinica == null)
			nomeClinica = "0";
		if (nomeClinica != null && nomeClinica.length() == 0)
			nomeClinica = "0";
		if (uf == null)
			uf = "0";
		if (uf != null && uf.length() == 0 || uf.length() > 2)
			uf = "0";
		if (codCidade == null)
			codCidade = 0;
		if (codEspecialidade == null)
			codEspecialidade = 0;
		if (codPlano == null)
			codPlano = 0;
		result = execute(SUB_URI_BUSCAR_MEDICO.replace(UF, this.uf)
				.replace(NOME_MEDICO, nomeMedico)
				.replace(NOME_CLINICA, nomeClinica)
				.replace(UF, uf)
				.replace(CIDADE, String.valueOf(codCidade))
				.replace(ESPECIALIDADE, String.valueOf(codEspecialidade))
				.replace(PLANO_DE_SAUDE, String.valueOf(codPlano)), HttpMethods.GET, ws.mUsuario);
		if (result.getCodResult() == HttpURLConnection.HTTP_OK){
			gson = new Gson();
			preLista = gson.fromJson(ws.mJson, new TypeToken<List<Usuario>>(){}.getType());
			
			Log.i("WSCbuscarMedico", ws.mJson);

			boolean funcForaDaLista = true;
			if (preLista.size() > 0){
				for (Usuario u : preLista){
					for (int i=0;i<lista.size();i++){
						if (lista.get(i).getCodFuncionario() == u.getFuncionario().getCodFuncionario()){
							lista.get(i).getClinicas().add(u.getClinica());
							funcForaDaLista = false; 
						}
					}
					if (funcForaDaLista){
						u.getFuncionario().getClinicas().add(u.getClinica());
						lista.add(u.getFuncionario());
					}
					funcForaDaLista = true;
				}
				return new ResultAction(R.string.ok, lista, ResultAction.NOT_SHOW_MSG);
			}
			else
				return new ResultAction(R.string.msg_no_doctor_found);
		}
		if (result.getCodResult() == HttpURLConnection.HTTP_UNAUTHORIZED){
			return new ResultAction(R.string.msg_acess_denied);
		}
		return new ResultAction(result.getCodResult());//new ResultAction(R.string.msg_server_error);
	}

}
