package br.com.android.cmi.cmiapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import br.com.android.cmi.cmiapp.AgendaAdapter.DayHolder;
import br.com.android.cmi.model.Agenda;
import br.com.android.cmi.model.Clinica;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.model.Disponibilidade;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Mes;
import br.com.android.cmi.model.Paciente;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCagendarConsulta;
import br.com.android.cmi.ws.WSCgerarAgenda;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;

public class AgendarConsultaFragment extends Fragment implements OnClickListener, OnItemClickListener, OnPostExecuteTask {
	
	private static final String HORA1_EXTRA = "HORA1_EXTRA";
	private static final String HORA2_EXTRA = "HORA2_EXTRA";
	private static final String MES_SELECIONADO_EXTRA = "MES_SELECIONADO_EXTRA";
	
	
	private Usuario mUsuario;
	private Funcionario mMedico;
	//private Paciente mPaciente;
	private Clinica mClinica;
	private Consulta mConsulta;
	private AgendaAdapter mAdapter;
	private ArrayList<Disponibilidade> mDisponibilidades;
	
	private ArrayList<Agenda> mListaAgenda;
	
	private ArrayList<Mes> mMeses;
	private String[] mMesesDoAno = new String[12];
	private Mes mMesAtual;
	private Integer mMesSelecionadoIndex;

	
	private TextView mMesAnterior;
	private TextView mProximoMes;
	private GridView mGrdvAgenda;
	private TextView txtAux;
	private TextView mTxtMesAtual;
	private TextView mTxtMedico;
	private TextView mTxtClinica;
	private TextView mTxtEspecialidades;
	private RadioButton mRbtnHora1;
	private RadioButton mRbtnHora2;
	private ProgressBar mProgressBar;
	
//	private MenuItem mnAgendar;
//	private MenuItem mnCancelar;
	
	private ViewMessage mViewMessage;
	
	
	public static AgendarConsultaFragment novaInstancia(Usuario usuario, Funcionario medico, Paciente paciente, Clinica clinica){
		Bundle parametros = new Bundle();
		parametros.putSerializable(Util.MEDICO_EXTRA, medico);
		//parametros.putSerializable(Util.PACIENTE_EXTRA, paciente);
		parametros.putSerializable(Util.CLINICA_EXTRA, clinica);
		parametros.putSerializable(Util.USER_EXTRA, usuario);
		//parametros.putSerializable(LISTA_AGENDA_EXTRA, listaAgenda);
		AgendarConsultaFragment novoFragment = new AgendarConsultaFragment();
		novoFragment.setArguments(parametros);
		return novoFragment;
	}
	
	public static AgendarConsultaFragment novaInstancia(ArrayList<Agenda> listaAgenda){
		Bundle parametros = new Bundle();
		parametros.putSerializable(Util.LISTA_AGENDA_EXTRA, listaAgenda);
		AgendarConsultaFragment novoFragment = new AgendarConsultaFragment();
		novoFragment.setArguments(parametros);
		return novoFragment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		HoraDisp hora1 = null;
		HoraDisp hora2 = null;
		mConsulta = new Consulta();
		
		Log.d("agendaFragment", "onCreate\\savadInstanceState " + (savedInstanceState==null?"isNULL":"notNULL"));
		
		int imes = GregorianCalendar.getInstance().get(GregorianCalendar.MONTH);
		mMesAtual = new Mes(imes, GregorianCalendar.getInstance().get(GregorianCalendar.YEAR), "apenas para iniciar");
		mMeses = new ArrayList<>();
		mMeses.add(mMesAtual);
		
		if (savedInstanceState != null){
			Log.e("agendaFragment", "onCreate.recuperando dados de savedInstanceState...");
			
			mListaAgenda = (ArrayList<Agenda>)savedInstanceState.getSerializable(Util.LISTA_AGENDA_EXTRA);
			
			mDisponibilidades = (ArrayList<Disponibilidade>)savedInstanceState.getSerializable(Util.LISTA_DISPONIBILIDADES_EXTRA);
			mMesSelecionadoIndex = savedInstanceState.getInt(MES_SELECIONADO_EXTRA);
			mMedico = (Funcionario)savedInstanceState.getSerializable(Util.MEDICO_EXTRA);
			mClinica = (Clinica)savedInstanceState.getSerializable(Util.CLINICA_EXTRA);
			mUsuario = (Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA);
			mConsulta = (Consulta)savedInstanceState.getSerializable(Util.CONSULTA_EXTRA);
			hora1 = (HoraDisp)savedInstanceState.getSerializable(HORA1_EXTRA);
			hora2 = (HoraDisp)savedInstanceState.getSerializable(HORA2_EXTRA);
			if(hora1.visivel){
				mRbtnHora1.setVisibility(View.VISIBLE);
				mRbtnHora1.setText(hora1.texto);
				mRbtnHora1.setChecked(hora1.marcada);
			}
			if (hora2.visivel){
				mRbtnHora2.setVisibility(View.VISIBLE);
				mRbtnHora2.setText(hora2.texto);
				mRbtnHora2.setChecked(hora2.marcada);
			}
		}else{
			
			Log.e("agendaFragment", "onCreate.savedInstanceState==NULL...");
			
			mListaAgenda = new ArrayList<Agenda>();
			mDisponibilidades = new ArrayList<Disponibilidade>();
			mMedico = (Funcionario)getArguments().getSerializable(Util.MEDICO_EXTRA);
			mClinica = (Clinica)getArguments().getSerializable(Util.CLINICA_EXTRA);
			mUsuario = (Usuario)getArguments().getSerializable(Util.USER_EXTRA);

			hora1 = (HoraDisp)getArguments().getSerializable(HORA1_EXTRA);
			hora2 = (HoraDisp)getArguments().getSerializable(HORA2_EXTRA);
			if(hora1 != null && hora1.visivel){
				mRbtnHora1.setVisibility(View.VISIBLE);
				mRbtnHora1.setText(hora1.texto);
				mRbtnHora1.setChecked(hora1.marcada);
			}
			if (hora2 != null && hora2.visivel){
				mRbtnHora2.setVisibility(View.VISIBLE);
				mRbtnHora2.setText(hora2.texto);
				mRbtnHora2.setChecked(hora2.marcada);
			}

			mMesSelecionadoIndex = 0;
			pegarAgendaDoMedicoNoWebService();
		}
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d("agendaFragment", "onCreateView...");
		
		View view = inflater.inflate(R.layout.fragment_agendar_consulta, container, false);
		mMesAnterior = (TextView)view.findViewById(R.id.txtPreviousMonth);
		mMesAnterior.setOnClickListener(this);
		mProximoMes = (TextView)view.findViewById(R.id.txtNextMonth);
		mProximoMes.setOnClickListener(this);
		mTxtMesAtual = (TextView)view.findViewById(R.id.txtMonthAndYear);
		txtAux = new TextView(getActivity());
		mGrdvAgenda = (GridView)view.findViewById(R.id.grdvAgenda);
		mGrdvAgenda.setOnItemClickListener(this);
		gerateMonths();
		ajustarMeses();
		mTxtMedico = (TextView)view.findViewById(R.id.txtMedico_agenda);
		mTxtClinica = (TextView)view.findViewById(R.id.txtClinica_agenda);
		mTxtEspecialidades = (TextView)view.findViewById(R.id.txtEspecialidades_agenda);
		mRbtnHora1 = (RadioButton)view.findViewById(R.id.rbtnHora1_agenda);
		mRbtnHora1.setOnClickListener(this);
		mRbtnHora2 = (RadioButton)view.findViewById(R.id.rbtnHora2_agenda);
		mRbtnHora2.setOnClickListener(this);
		mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar_agenda);
		mViewMessage = new ViewMessage(getActivity());
		
		//mAdapter = new AgendaAdapter(getActivity(), mDisponibilidades, mMesAtual);
		mAdapter = new AgendaAdapter(getActivity(), mMesAtual, mListaAgenda);
		mGrdvAgenda.setAdapter(mAdapter);
		
		
		mTxtMesAtual.setText(mMeses.get(mMesSelecionadoIndex).toString());
		txtAux.setText(R.string.crm);
		mTxtMedico.setText(mMedico.getNome() +", " + txtAux.getText().toString() + " " + mMedico.getRegistroConselho());
		mTxtClinica.setText(mClinica.toString());
		String especialidades = null;
		
		if (savedInstanceState != null){
			Log.e("agendaFragment", "onCreateView.savedInstanceState != null...");
		}
		
		if (mClinica.getEspecialidades() != null)
			for (int i = 0; i < mClinica.getEspecialidades().size(); i++) {
				if (i > 0)
					especialidades += ", " + mClinica.getEspecialidades().get(i).getDescricao();
				else
					especialidades = mClinica.getEspecialidades().get(i).getDescricao();
			}
		if (especialidades != null){
			especialidades += ".";
			mTxtEspecialidades.setText(especialidades);
		}
		mTxtEspecialidades.setVisibility(especialidades==null?View.GONE:View.VISIBLE);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(MES_SELECIONADO_EXTRA, mMesSelecionadoIndex);
		outState.putSerializable(Util.LISTA_AGENDA_EXTRA, mListaAgenda);
		outState.putSerializable(Util.MEDICO_EXTRA, mMedico);
		outState.putSerializable(Util.CLINICA_EXTRA, mClinica);
		outState.putStringArray(Util.MESES_DO_ANO_EXTRA, mMesesDoAno);
		outState.putSerializable(Util.MESES_AGENDA, mMeses);
		outState.putSerializable(Util.CONSULTA_EXTRA, mConsulta);
		HoraDisp hora1 = new HoraDisp();
		if (mRbtnHora1.getVisibility() == View.VISIBLE){
			hora1.disp = (Disponibilidade)mRbtnHora1.getTag();
			hora1.visivel = true;
			hora1.texto = mRbtnHora1.getText().toString();
		}else{
			hora1.visivel = false;
		}
		hora1.marcada = mRbtnHora1.isChecked();
		outState.putSerializable(HORA1_EXTRA, hora1);
		HoraDisp hora2 = new HoraDisp();
		if (mRbtnHora2.getVisibility() == View.VISIBLE){
			hora2.disp = (Disponibilidade)mRbtnHora2.getTag();
			hora2.visivel = true;
			hora2.texto = mRbtnHora2.getText().toString();
		}else{
			hora2.visivel = false;
		}
		hora2.marcada = mRbtnHora2.isChecked();
		outState.putSerializable(HORA2_EXTRA, hora2);
		Log.d("agendaFragment", "onSaveInstanceState...");
		//this.setArguments(outState);
		this.getArguments().putSerializable(HORA1_EXTRA, hora1);
		this.getArguments().putSerializable(HORA2_EXTRA, hora2);
	}
	
	private void ajustarMeses(){
		
		Log.d("agendarConsultaFragment", "ajustandoMeses...");
		
		GregorianCalendar calendario = new GregorianCalendar();
		GregorianCalendar calendarioInicial = new GregorianCalendar();
		Integer imes;
		if (mMeses == null)
			mMeses = new ArrayList<Mes>();
		mMeses.clear();
		for (int i = 0; i < 3; i++){
			imes = calendario.get(GregorianCalendar.MONTH) + i;
			if (imes > calendarioInicial.getActualMaximum(GregorianCalendar.MONTH)){
				calendario.set(calendarioInicial.get(GregorianCalendar.YEAR)+1, calendario.getActualMinimum(GregorianCalendar.MONTH), 1);
			}else{
				calendario.set(calendarioInicial.get(GregorianCalendar.YEAR), calendarioInicial.get(GregorianCalendar.MONTH)+i, 1);
			}
			imes = calendario.get(GregorianCalendar.MONTH);
			mMeses.add(new Mes(imes, calendario.get(GregorianCalendar.YEAR), mMesesDoAno[imes]));
			
			Log.d("ajustarMeses", mMeses.get(i).toString());
		}
		if (mMesAtual == null)
			mMesAtual = mMeses.get(0);
	}
	
	private void gerateMonths(){
		txtAux.setText(R.string.january);
		mMesesDoAno[0] = txtAux.getText().toString();
		txtAux.setText(R.string.february);
		mMesesDoAno[1] = txtAux.getText().toString();
		txtAux.setText(R.string.march);
		mMesesDoAno[2] = txtAux.getText().toString();
		txtAux.setText(R.string.april);
		mMesesDoAno[3] = txtAux.getText().toString();
		txtAux.setText(R.string.may);
		mMesesDoAno[4] = txtAux.getText().toString();
		txtAux.setText(R.string.june);
		mMesesDoAno[5] = txtAux.getText().toString();
		txtAux.setText(R.string.july);
		mMesesDoAno[6] = txtAux.getText().toString();
		txtAux.setText(R.string.august);
		mMesesDoAno[7] = txtAux.getText().toString();
		txtAux.setText(R.string.september);
		mMesesDoAno[8] = txtAux.getText().toString();
		txtAux.setText(R.string.october);
		mMesesDoAno[9] = txtAux.getText().toString();
		txtAux.setText(R.string.november);
		mMesesDoAno[10] = txtAux.getText().toString();
		txtAux.setText(R.string.december);
		mMesesDoAno[11] = txtAux.getText().toString();
		
		Log.i("agendarConsultaFragment", "mMesesDoAno=" + mMesesDoAno.toString()); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.txtPreviousMonth:
				irParaMesAnterior();
				break;
			case R.id.txtNextMonth:
				irParaProximoMes();
				break;
			case R.id.rbtnHora1_agenda:
				mConsulta.setDisponibilidade((Disponibilidade)mRbtnHora1.getTag());
				mRbtnHora2.setChecked(false);
				break;
			case R.id.rbtnHora2_agenda:
				mConsulta.setDisponibilidade((Disponibilidade)mRbtnHora2.getTag());
				mRbtnHora1.setChecked(false);
				break;
		}
	}

	public void cancelarAgendamento() {
		mRbtnHora1.setVisibility(View.GONE);
		mRbtnHora1.setChecked(false);
		mRbtnHora2.setVisibility(View.GONE);
		mRbtnHora2.setChecked(false);
		mAdapter.unselectView();
	}

	public void agendarConsulta() {
		if ((mRbtnHora1.isChecked() && mRbtnHora1.getVisibility() == View.VISIBLE)
				|| (mRbtnHora2.isChecked() && mRbtnHora2.getVisibility() == View.VISIBLE)){
			mConsulta.setFuncionario(mMedico);
			mConsulta.setPaciente(mUsuario.getPaciente());
			//mConsulta.setDisponibilidade(); // atributo recebe disponibilidade ao clicar no radioButom
			//mConsulta.setDataConsulta(data); // atributo recebe uma data ao clicar num dia com disponibilidade
			agendarConsultaNoWebService();
			//Toast.makeText(getActivity(), "Agendar consulta: não implementado!", Toast.LENGTH_SHORT).show();
		}
	}

	private void irParaProximoMes() {
		mMesSelecionadoIndex++;
		if (mMesSelecionadoIndex <= mMeses.size()-1){
			mTxtMesAtual.setText(mMeses.get(mMesSelecionadoIndex).toString());
		}else
			mMesSelecionadoIndex = mMeses.size()-1;
		mMesAtual = mMeses.get(mMesSelecionadoIndex);
		
		Log.d("irParaProximoMes", mMesAtual.toString());
		Log.d("irParaProximoMes", "pegarDisponibilidadeDoMedicoNoWebService()...");
		
		//pegarDisponibilidadeDoMedicoNoWebService();
		
		pegarAgendaDoMedicoNoWebService();
		cancelarAgendamento();
	}

	private void irParaMesAnterior() {
		mMesSelecionadoIndex--;
		if (mMesSelecionadoIndex >= 0){
			mTxtMesAtual.setText(mMeses.get(mMesSelecionadoIndex).toString());
		}else
			mMesSelecionadoIndex = 0;
		mMesAtual = mMeses.get(mMesSelecionadoIndex);
		
		Log.i("irParaMesAnterior", mMesAtual.toString());
		
		//pegarDisponibilidadeDoMedicoNoWebService();
		
		pegarAgendaDoMedicoNoWebService();
		cancelarAgendamento();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GregorianCalendar calendario = new GregorianCalendar();
		String sDia = ((TextView)view.findViewById(R.id.txtDia_agenda)).getText().toString();
		calendario.set(mMeses.get(mMesSelecionadoIndex).getAno(), mMesAtual.getId(), Integer.parseInt(sDia));
		
		Log.d("onItemClick", "mMesAtual=" + mMeses.get(mMesSelecionadoIndex).toString());
		mConsulta.setDataConsulta(calendario.getTime());
		int iDiaSemana = calendario.get(GregorianCalendar.DAY_OF_WEEK);
		if (view.getTag() instanceof DayHolder){
			if (!((DayHolder)view.getTag()).agenda.getCursor().equals("pointer_selected")){
				mostrarHorarioDisponiveis(((DayHolder)view.getTag()).agenda);
				mAdapter.setSelectedView(view);
			}
		}
		else
			mostrarHorarioDisponiveis(iDiaSemana);

		//Toast.makeText(getActivity(), "Clicou no item(" +position+")=" + sDia + "/ dia da semana="+iDiaSemana, Toast.LENGTH_SHORT).show();
	}
	
	private void mostrarHorarioDisponiveis(Agenda diaAgenda){
		List<Disponibilidade> lista = diaAgenda.getDisponibilidades();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		txtAux.setText(R.string.time_at);
		mRbtnHora1.setChecked(false);
		mRbtnHora2.setChecked(false);
		if (diaAgenda.getDisponibilidades().size() > 0){
			for (int i=0;i<lista.size();i++){
				if (i==0){
					mRbtnHora1.setText(sdf.format(lista.get(i).getHoraInicial()) + " " + txtAux.getText().toString() 
							+ " " + sdf.format(lista.get(i).getHoraFinal()));
					mRbtnHora1.setVisibility(View.VISIBLE);
					mRbtnHora1.setTag(lista.get(i));
					mRbtnHora2.setVisibility(View.GONE);
				}else if (i == 1){
					mRbtnHora2.setText(sdf.format(lista.get(i).getHoraInicial()) + " " + txtAux.getText().toString() 
							+ " " + sdf.format(lista.get(i).getHoraFinal()));
					mRbtnHora2.setVisibility(View.VISIBLE);
					mRbtnHora2.setTag(lista.get(i));
					break;
				}
			}
		}
	}
	
	private void mostrarHorarioDisponiveis(Integer iDiaSemana){
		ArrayList<Disponibilidade> lista = new ArrayList<Disponibilidade>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		txtAux.setText(R.string.time_at);
		for (Disponibilidade d : mDisponibilidades){
			if (d.getDiaSemana().getValor()+1 == iDiaSemana){
				lista.add(d);
			}
		}
		mRbtnHora2.setVisibility(lista.size()<2?View.GONE:View.VISIBLE);
		for (int i = 0;i<lista.size();i++){	
			if (i == 0){
				mRbtnHora1.setText(sdf.format(lista.get(i).getHoraInicial()) + " " + txtAux.getText().toString() 
						+ " " + sdf.format(lista.get(i).getHoraFinal()));
				mRbtnHora1.setVisibility(View.VISIBLE);
				mRbtnHora1.setTag(lista.get(i));
			}else if (i == 1){
				mRbtnHora2.setText(sdf.format(lista.get(i).getHoraInicial()) + " " + txtAux.getText().toString() 
						+ " " + sdf.format(lista.get(i).getHoraFinal()));
				mRbtnHora2.setVisibility(View.VISIBLE);
				mRbtnHora2.setTag(lista.get(i));
				break;
			}
		}
	}
	
	private void agendarConsultaNoWebService(){
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCagendarConsulta(getActivity(), WebServiceConnection.getWebServiceToUser(mUsuario),
				mConsulta);
		task.execute(wsCommand);
	}
	
	private void pegarAgendaDoMedicoNoWebService(){
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCgerarAgenda(getActivity(), WebServiceConnection.getWebServiceToUser(mUsuario),
				mMedico, mClinica, mMeses.get(mMesSelecionadoIndex));
		task.execute(wsCommand);
	}
	
//	private void pegarDisponibilidadeDoMedicoNoWebService(){
//		TaskForWs task = new TaskForWs(this);
//		WSCommand wsCommand = new WSCdisponibilidadesMedico(getActivity(), WebServiceConnection.getWebServiceToUser(mUsuario),
//				mMedico.getCodFuncionario(), mClinica.getCpf_cnpj());
//		task.execute(wsCommand);
//	}
	
	private void atualizarListaAgenda(List<Agenda> lista){
		
		Log.e("agendarConsultaFragment", "atualizarListaAgenda...");
		
		mListaAgenda.clear();
		mListaAgenda.addAll(lista);
		mAdapter.setMesAtual(mMeses.get(mMesSelecionadoIndex));
		mAdapter.notifyDataSetChanged();
	}
	
	private void atualizarListaDisponibilidade(List<Disponibilidade> lista){
		
		Log.d("agendarConsultaFragment", "atualizarListaDisponibilidade...");
		
		mDisponibilidades.clear();
		mDisponibilidades.addAll(lista);
		mAdapter.setMesAtual(mMeses.get(mMesSelecionadoIndex));
		mAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postExecuteTask(ResultAction resultAction) {
		
		Log.i("postExecuteTask", "executando...");
		
		List<?> lista = null;
		List<Disponibilidade> disponibilidades;
		List<Agenda> agendas;
		if (resultAction != null) {
			if (resultAction.getObj() != null && resultAction.getObj() instanceof List<?>){
				lista = (List<?>) resultAction.getObj();
				if (lista.size() == 0){
					mListaAgenda.clear();
					mDisponibilidades.clear();
					mAdapter.setMesAtual(mMeses.get(mMesSelecionadoIndex));
					mAdapter.notifyDataSetChanged();
					return;
				}
				if (lista.get(0) instanceof Disponibilidade){
					disponibilidades = (List<Disponibilidade>)lista;
					atualizarListaDisponibilidade(disponibilidades);
				}
				if (lista.get(0) instanceof Agenda){
					agendas = (List<Agenda>)lista;
					
					Log.e("postExecuteTask", "recebendo lista de agenda="+agendas.size());
					
					atualizarListaAgenda(agendas);
				}
			}
			if (resultAction.getCodResult() == R.string.msg_scheduling_successful){
				mRbtnHora1.setVisibility(View.GONE);
				mRbtnHora2.setVisibility(View.GONE);
				mAdapter.unselectView();
				pegarAgendaDoMedicoNoWebService();
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater().inflate(R.menu.agenda_menu, menu);
//		mnAgendar = menu.findItem(R.id.menu_agendar);
//		mnCancelar = menu.findItem(R.id.menu_cancelar_agenda);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_agendar:
			agendarConsulta();
			break;
		case R.id.menu_cancelar_agenda:
			cancelarAgendamento();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		
		Log.e("agendaFragment", "onViewStateRestored...");
		
		if (savedInstanceState != null){
			Log.e("agendaFragment", "onViewStateRestored.recuperando dados de savedInstanceState...");
			HoraDisp hora1, hora2;
			hora1 = (HoraDisp)savedInstanceState.getSerializable(HORA1_EXTRA);
			hora2 = (HoraDisp)savedInstanceState.getSerializable(HORA2_EXTRA);
			if(hora1.visivel){
				mRbtnHora1.setVisibility(View.VISIBLE);
				mRbtnHora1.setText(hora1.texto);
				mRbtnHora1.setChecked(hora1.marcada);
			}
			if (hora2.visivel){
				mRbtnHora2.setVisibility(View.VISIBLE);
				mRbtnHora2.setText(hora2.texto);
				mRbtnHora2.setChecked(hora2.marcada);
			}
		}else{
			Log.e("agendaFragment", "onViewStateRestored.getArguments()...");
			HoraDisp hora1, hora2;
			hora1 = (HoraDisp)getArguments().getSerializable(HORA1_EXTRA);
			hora2 = (HoraDisp)getArguments().getSerializable(HORA2_EXTRA);
			if(hora1 != null && hora1.visivel){
				mRbtnHora1.setVisibility(View.VISIBLE);
				mRbtnHora1.setText(hora1.texto);
				mRbtnHora1.setChecked(hora1.marcada);
			}
			if (hora2 != null && hora2.visivel){
				mRbtnHora2.setVisibility(View.VISIBLE);
				mRbtnHora2.setText(hora2.texto);
				mRbtnHora2.setChecked(hora2.marcada);
			}
		}
		
	}
	
	class HoraDisp implements Serializable{
		
		private static final long serialVersionUID = 101L;
		
		Disponibilidade disp;
		boolean visivel;
		boolean marcada;
		String texto;
	}
	
}
