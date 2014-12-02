package br.com.android.cmi.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.widget.TextView;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Mes;
import br.com.android.cmi.model.Usuario;

public class Util {
	
	public static final int ANO_MINIMO = 1900;
	public static final int REQUEST_AVALIAR_ATENDIMENTO = 100;
	
	public static final String CONFIG_EXTRA = "CONFIG_EXTRA";
	public static final String USER_EXTRA = "USER_EXTRA";
	public static final String LISTA_FUNCIONARIOS = "LISTA_FUNCIONARIOS";
	public static final String LISTA_AGENDA_EXTRA = "LISTA_AGENDA_EXTRA";
	public static final String MEDICO_EXTRA = "MEDICO_EXTRA";
	public static final String PACIENTE_EXTRA = "PACIENTE_EXTRA";
	public static final String CLINICA_EXTRA = "CLINICA_EXTRA";
	public static final String LISTA_DISPONIBILIDADES_EXTRA = "LISTA_DIPONIBILIDADES_EXTRA";
	public static final String MESES_DO_ANO_EXTRA = "MESES_DO_ANO_EXTRA";
	public static final String MESES_AGENDA = "MESES_AGENDA_EXTRA";
	public static final String LISTA_CONSULTAS_AGENDADAS_EXTRA = "LISTA_CONSULTAS_AGENDADAS_EXTRA";
	public static final String LISTA_ATENDIMENTOS_EXTRA = "LISTA_ATENDIMENTOS_EXTRA";
	public static final String MAPA_ATENDIMENTOS_EXTRA = "MAPA_ATENDIMENTOS_EXTRA";
	public static final String CONSULTA_EXTRA = "CONSULTA_EXTRA";
	public static final String ATENDIMENTO_EXTRA = "ATENDMENTO_EXTRA";
	public static final String ATENDIMENTO_EM_AVALIACAO_EXTRA = "ATENDIMENTO_EM_AVALIACAO_EXTRA";
	public static final String AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA = "AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA";
	
	public static final String ADMINISTRADOR = "ADMINISTRADOR";
	public static final String DIRETOR = "DIRETOR";
	public static final String DIRETOR_MEDICO = "DIRETOR_MEDICO";
	public static final String MEDICO = "MEDICO";
	public static final String SECRETARIA = "SECRETARIA";
	public static final String PACIENTE = "PACIENTE";
	public static final String MAIN_VISIBLE_OPTIONS_EXTRA = "MAIN_VISIBLE_OPTIONS_EXTRA";
	public static final String IS_USUARIO_AUTENTICADO_EXTRA = "USUARIO_AUTENTICADO_EXTRA";
	
	public static final String TAG_AGENDAR_CONSULTA_TABLET = "tagFragmentAgendarConsulta_tablet";
	public static final String TAG_CONSULTAS_AGENDADAS = "tagFragmentConsultasAgendadas";
	public static final String TAG_CONSULTAS_AGENDADAS_TABLET = "tagFragmentConsultasAgendadas_tablet";
	public static final String TAG_AVALIACAO_DO_ATENDIMENTO = "tagFragmentAvaliacaoAtendimento";
	public static final String TAG_AVALIACAO_DO_ATENDIMENTO_TABLET = "tagFragmentAvaliacaoAtendimento_tablet";
	
	
	public static Usuario usuarioAtual;
	

	public Util() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean usuarioTemPerfil(Usuario usuario, String...descricoesDosPerfis){
        if (usuario.getPerfil() == null)
            return false;
        for (String descricao : descricoesDosPerfis) {
            if (descricao.toLowerCase().equals(
                    usuario.getPerfil().getDescricao().toLowerCase()))
                return true;
        }
        return false;
    }
	
	public static List<String> gerarListaDeDias(Calendar calendario, String textoParaIndex0){
		List<String> lista = new ArrayList<String>();
		Integer ultimoDia = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (textoParaIndex0 != null)
			lista.add(textoParaIndex0);
		for (int i=1;i<=ultimoDia;i++)
			lista.add(String.valueOf(i));
		return lista;
	}
	
	public static List<String> gerarListaDeAnos(int anoInicial, int anoFinal, String textoParaIndex0){
		List<String> lista = new ArrayList<String>();
		if (textoParaIndex0 != null)
			lista.add(textoParaIndex0);
		for (int i = anoInicial;i <= anoFinal;i++)
			lista.add(String.valueOf(i));
		return lista;
	}
	 
	public static List<Mes> gerarListaDeMeses(TextView txtAux, String textoParaIndex0){
		List<Mes> lista = new ArrayList<Mes>();
		if (textoParaIndex0 != null){
			lista.add(new Mes(0, textoParaIndex0, Mes.ToStringBehavior.MES));
		}
		txtAux.setText(R.string.january);
		lista.add(new Mes(1, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.february);
		lista.add(new Mes(2, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.march);
		lista.add(new Mes(3, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.april);
		lista.add(new Mes(4, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.may);
		lista.add(new Mes(5, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.june);
		lista.add(new Mes(6, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.july);
		lista.add(new Mes(7, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.august);
		lista.add(new Mes(8, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.september);
		lista.add(new Mes(9, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.october);
		lista.add(new Mes(10, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.november);
		lista.add(new Mes(11, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		txtAux.setText(R.string.december);
		lista.add(new Mes(12, txtAux.getText().toString(), Mes.ToStringBehavior.MES));
		return lista;
	}
	
}
