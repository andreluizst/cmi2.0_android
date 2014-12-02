package br.com.android.cmi.cmiapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import br.com.android.cmi.cmiapp.AvaliarAtendimentoFragment.OnAvaliarAtendimento;
import br.com.android.cmi.cmiapp.ConsultasAgendadasFragment.OnAvaliarAtendimentoDaConsultaClick;
import br.com.android.cmi.cmiapp.ConsultasAgendadasFragment.OnCancelarAgendamentoClick;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.model.Mes;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCbuscarConsultasAgendadas;
import br.com.android.cmi.ws.WSCcancelarAgendamento;
import br.com.android.cmi.ws.WSClistarAtendimentosRealizadosUsuario;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConsultasAgendadasActivity extends Activity implements OnPostExecuteTask, OnItemSelectedListener, OnKeyListener,
				OnCancelarAgendamentoClick, OnAvaliarAtendimentoDaConsultaClick, OnAvaliarAtendimento {
	
	private static final String NOME_MEDICO = "NOME_MEDICO";
	private static final String COUNT_WSCOMMANDS_EXTRA = "COUNT_WSCOMMAND_EXTRA";
	private static final String AVALIACAO_ATENDIMENTO_ESTA_VISIVEL_EXTRA = "AVALIACAO_ATENDIMENTO_ESTA_VISIVEL_EXTRA";
	
	private Usuario mUsuario;
	private ArrayAdapter<String> mDiasAdapter;
	private ArrayAdapter<Mes> mMesesAdapter;
	private ArrayAdapter<String> mAnosAdapter;
	private List<String> mDias;
	private List<Mes> mMeses;
	private List<String> mAnos;
	private String mNomeMedico;
	private ArrayList<Consulta> mConsultas;
	private ArrayList<Atendimento> mAtendimentos;

	private TextView txtAux;
	private Spinner spnDia;
	private Spinner spnMes;
	private Spinner spnAno;
	private EditText edtNomeMedico;
	private FrameLayout mFragmentListaConsultas;
	private ProgressBar mProgressBar;
	private ViewMessage mViewMessage;
	private Integer mCountWSCommands;
	private Consulta mConsultaParaCancelamento;
	private FrameLayout mFragmentAvaliacaoDaConsultaTablet;
	private boolean mAvaliacaoDoAtendimentoEstaVisivel;
	//private boolean mListaEstaVisivel;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultas_agendadas);
		
		mViewMessage = new ViewMessage(this);
		txtAux = new TextView(this);
		mAvaliacaoDoAtendimentoEstaVisivel = false;
		mConsultas = new ArrayList<Consulta>();
		mAtendimentos = new ArrayList<Atendimento>();
		mCountWSCommands = -1;
		txtAux.setText(R.string.all_male);
		spnDia = (Spinner)findViewById(R.id.spnDia_filtro_consultasAgendadas);
		mDias = Util.gerarListaDeDias(Calendar.getInstance(), txtAux.getText().toString());
		mDiasAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mDias);
		mDiasAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnDia.setAdapter(mDiasAdapter);
		txtAux.setText(R.string.all_male);
		spnMes = (Spinner)findViewById(R.id.spnMes_filtro_consultasAgendadas);
		mMeses = Util.gerarListaDeMeses(txtAux, txtAux.getText().toString());
		mMesesAdapter = new ArrayAdapter<Mes>(this, R.layout.spinner_item, mMeses);
		mMesesAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnMes.setAdapter(mMesesAdapter);
		spnMes.setOnItemSelectedListener(this);
		txtAux.setText(R.string.all_male);
		spnAno = (Spinner)findViewById(R.id.spnAno_filtro_consultasAgendadas);
		mAnos = Util.gerarListaDeAnos(Util.ANO_MINIMO, GregorianCalendar.getInstance().get(GregorianCalendar.YEAR)+1,
				txtAux.getText().toString());
		mAnosAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mAnos);
		mAnosAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnAno.setAdapter(mAnosAdapter);
		spnAno.setSelection(mAnos.size()-2);
		edtNomeMedico = (EditText)findViewById(R.id.edtNomeMedico_filtro_consultasAgendadas);
		edtNomeMedico.setOnKeyListener(this);
		mFragmentListaConsultas = (FrameLayout)findViewById(R.id.fragmentListaConsultas);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar_consultasAgendadas);
		if (isTablet()){
			mFragmentAvaliacaoDaConsultaTablet = (FrameLayout)findViewById(R.id.fragmentAvaliacaoDoAtendimento_tablet);
		}
		if (savedInstanceState == null){
			Intent it = getIntent();
			mUsuario = (Usuario)it.getSerializableExtra(Util.USER_EXTRA);
		}else{
			mUsuario = (Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA);
			mNomeMedico = savedInstanceState.getString(NOME_MEDICO);
			edtNomeMedico.setText(mNomeMedico);
			mCountWSCommands = savedInstanceState.getInt(COUNT_WSCOMMANDS_EXTRA);
			mConsultas = (ArrayList<Consulta>)savedInstanceState.getSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA);
			mAtendimentos = (ArrayList<Atendimento>)savedInstanceState.getSerializable(Util.LISTA_ATENDIMENTOS_EXTRA);
			mAvaliacaoDoAtendimentoEstaVisivel = savedInstanceState.getBoolean(AVALIACAO_ATENDIMENTO_ESTA_VISIVEL_EXTRA, false);
			if (isTablet())
				mFragmentAvaliacaoDaConsultaTablet.setVisibility(mAvaliacaoDoAtendimentoEstaVisivel?View.VISIBLE:View.GONE);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.USER_EXTRA, mUsuario);
		outState.putString(NOME_MEDICO, mNomeMedico);
		outState.putInt(COUNT_WSCOMMANDS_EXTRA, mCountWSCommands);
		outState.putSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA, mConsultas);
		outState.putSerializable(Util.LISTA_ATENDIMENTOS_EXTRA, mAtendimentos);
		outState.putSerializable(AVALIACAO_ATENDIMENTO_ESTA_VISIVEL_EXTRA, mAvaliacaoDoAtendimentoEstaVisivel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.consultas_agendadas, menu);
		return true;//super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_seek_scheduled_appointment:
				pegarConsultasAgendadasNoWebService();
				break;
			case R.id.action_clear:
				clearFilters();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("UseSparseArrays")
	private void showFragmentListaConsultas(){
		
		Log.i(null, "executando...showFragmentListaConsultas()...");
		
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.setIndeterminate(true);
		ConsultasAgendadasFragment fragment;
		HashMap<Integer, Atendimento> atendimentoConsulta = new HashMap<Integer,Atendimento>();
		if (mConsultas == null)
			fragment = ConsultasAgendadasFragment.novaInstancia(mUsuario, new ArrayList<Consulta>(), atendimentoConsulta);
		else{
			
			Log.e(null, "mConsultas.size()="+mConsultas.size());
			
			for (Consulta c : mConsultas){
				atendimentoConsulta.put(c.getCodConsulta(), null);
				for (Atendimento a : mAtendimentos){
					if (c.getCodConsulta() == a.getConsulta().getCodConsulta() && a.getAvaliacao() != 0){
						atendimentoConsulta.put(c.getCodConsulta(), a);
//						break;
					}
				}
			}
			fragment = ConsultasAgendadasFragment.novaInstancia(mUsuario, mConsultas, atendimentoConsulta);
		}
		mProgressBar.setIndeterminate(false);
		mProgressBar.setVisibility(View.INVISIBLE);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragmentListaConsultas, fragment, Util.TAG_CONSULTAS_AGENDADAS);
		ft.commit();
		mFragmentListaConsultas.setVisibility(View.VISIBLE);
	}
	
	private void closeFragmentListaConsultas(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ConsultasAgendadasFragment listaFragment = (ConsultasAgendadasFragment)fm.findFragmentByTag(Util.TAG_CONSULTAS_AGENDADAS);
		if (listaFragment != null){
			ft.remove(listaFragment);
			ft.commit();
//			mFragmentListaConsultas.setVisibility(View.GONE);
//			mListaEstaVisivel = false;
		}
	}
	
	private void clearFilters(){
		edtNomeMedico.setText("");
		mNomeMedico = "";
		spnDia.setSelection(0);
		spnMes.setSelection(0);
		spnAno.setSelection(mAnos.size()-2);
	}
	
	private void pegarConsultasAgendadasNoWebService(){
		mCountWSCommands = 2;
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCbuscarConsultasAgendadas(this, WebServiceConnection.getWebServiceToUser(mUsuario),
				mUsuario.getPaciente().getId(), mNomeMedico, spnDia.getSelectedItemPosition(),
				mMeses.get(spnMes.getSelectedItemPosition()).getId(), Integer.parseInt(mAnos.get(spnAno.getSelectedItemPosition())));
		task.execute(wsCommand);
		TaskForWs taskAtendimentos = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wscAtendimentosRealizados = new WSClistarAtendimentosRealizadosUsuario(this, WebServiceConnection.getWebServiceToUser(mUsuario));
		taskAtendimentos.execute(wscAtendimentosRealizados);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.spnMes_filtro_consultasAgendadas){
			int lastDayIndex = spnDia.getSelectedItemPosition(); 
			Calendar calendario = Calendar.getInstance();
			int ano = Calendar.getInstance().get(Calendar.YEAR);
			int mes = 0;
			if (spnAno.getSelectedItemPosition() > 0){
				ano = Integer.parseInt(mAnos.get(spnAno.getSelectedItemPosition()));
			}
			if (spnMes.getSelectedItemPosition() > 0){
				mes = mMeses.get(spnMes.getSelectedItemPosition()).getId();
			}
			mDias.clear();
			calendario.set(ano, mes-1, 1);
			txtAux.setText(R.string.all_male);
			mDias.addAll(Util.gerarListaDeDias(calendario, txtAux.getText().toString()));
			mDiasAdapter.notifyDataSetChanged();
			spnDia.setSelection(lastDayIndex>mDias.size()?mDias.size()-1:lastDayIndex);
			Log.i("onItemSelected", calendario.toString());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v.getId() == R.id.edtNomeMedico_filtro_consultasAgendadas)
			mNomeMedico = edtNomeMedico.getText().toString();
		return false;
	}

	@Override
	public void onCancelarAgendamentoClick(Consulta consulta) {
		mConsultaParaCancelamento = consulta;
		DialogInterface.OnClickListener dlgClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						cancelarAgendamentoNoWebService(mConsultaParaCancelamento);
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						break;
				}
			}
		};
		
		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.dlg_title_confirmation)
				.setMessage(R.string.msg_confirm_cancel)
				.setPositiveButton(R.string.yes, dlgClick)
				.setNegativeButton(R.string.no, dlgClick)
				.create();
		alert.show();
	}
	
	private void cancelarAgendamentoNoWebService(Consulta consulta){
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCcancelarAgendamento(this, WebServiceConnection.getWebServiceToUser(mUsuario), consulta);
		task.execute(wsCommand);
	}
	
	private void refreshList(){
		FragmentManager fm = getFragmentManager();
		ConsultasAgendadasFragment listaFragment = (ConsultasAgendadasFragment)fm.findFragmentByTag(Util.TAG_CONSULTAS_AGENDADAS);
		listaFragment.refreshItemList(mConsultaParaCancelamento);
	}
	
	private void refreshList(Atendimento atendimento){
		FragmentManager fm = getFragmentManager();
		ConsultasAgendadasFragment listaFragment = (ConsultasAgendadasFragment)fm.findFragmentByTag(Util.TAG_CONSULTAS_AGENDADAS);
		listaFragment.refreshItemList(atendimento);
	}
	
	private boolean isTablet(){
		return findViewById(R.id.fragmentAvaliacaoDoAtendimento_tablet) != null;
	}
	
	@Override
	public void onAvaliarAtendimentoDaConsultaClick(Atendimento atendimento) {
		mProgressBar.setIndeterminate(true);
		if (isTablet()){
			AvaliarAtendimentoFragment fragment = AvaliarAtendimentoFragment.novaInstancia(atendimento, 
																R.drawable.background_gradiente_azul_selected_item); 
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fragmentAvaliacaoDoAtendimento_tablet, fragment, Util.TAG_AVALIACAO_DO_ATENDIMENTO_TABLET);
			ft.commit();
			mAvaliacaoDoAtendimentoEstaVisivel = true;
			mFragmentAvaliacaoDaConsultaTablet.setVisibility(View.VISIBLE);
		}else{
			Intent it = new Intent(this, AvaliarAtendimentoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Util.ATENDIMENTO_EXTRA, atendimento);
			it.putExtras(bundle);
			startActivityForResult(it, Util.REQUEST_AVALIAR_ATENDIMENTO);
		}
		mProgressBar.setIndeterminate(false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Util.REQUEST_AVALIAR_ATENDIMENTO ){
			if (resultCode == RESULT_OK)
				refreshList((Atendimento)data.getSerializableExtra(Util.ATENDIMENTO_EXTRA));
			limparSelecaoDaLista();
		}
	}
	
	//interface AvaliarAtendimentoFragment.OnAvaliarAtendimento {onConfirmarAvaliacaoAtendimento, onCancelarAvaliacaoAtendimento}
	@Override
	public void onConfirmarAvaliacaoAtendimento(Atendimento atendimento) {
		if (isTablet()){
			FragmentManager fm = getFragmentManager();
			ConsultasAgendadasFragment listaFragment = (ConsultasAgendadasFragment)fm.findFragmentByTag(Util.TAG_CONSULTAS_AGENDADAS);
			listaFragment.refreshItemList(atendimento);
			closeFragmentAvaliarAtendimento();
			limparSelecaoDaLista();
		}
	}

	@Override
	public void onCancelarAvaliacaoAtendimento() {
		closeFragmentAvaliarAtendimento();
		limparSelecaoDaLista();
	}
	
	private void closeFragmentAvaliarAtendimento(){
		if (isTablet()){
			AvaliarAtendimentoFragment fragment;
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fragment = (AvaliarAtendimentoFragment)fm.findFragmentByTag(Util.TAG_AVALIACAO_DO_ATENDIMENTO_TABLET);
			if (fragment != null){
				ft.remove(fragment);
				ft.commit();
			}
			mAvaliacaoDoAtendimentoEstaVisivel = false;
			mFragmentAvaliacaoDaConsultaTablet.setVisibility(View.GONE);
		}
	}
	
	private void limparSelecaoDaLista(){
		FragmentManager fm = getFragmentManager();
		ConsultasAgendadasFragment listaFragment = (ConsultasAgendadasFragment)fm.findFragmentByTag(Util.TAG_CONSULTAS_AGENDADAS);
		listaFragment.selecionarConsulta(null);
	}
	
	
	@Override
	public void postExecuteTask(ResultAction resultAction) {
		List<?> lista;
		if (resultAction != null){
			if (resultAction.getCodResult() != null && resultAction.getCodResult() == R.string.msg_cancellation_done){
				mConsultaParaCancelamento.setSituacao(3);//3=cancelada
				refreshList();
				return;
			}
//			if (resultAction.getCodResult() == R.string.msg_no_scheduled_appointments_found){
//				mConsultas.clear();
//				mAtendimentos.clear();
//				mCountWSCommands--;
//			}
			if (resultAction.getCodResult() == R.string.msg_no_service_found){
				mCountWSCommands--;
			}
			if (resultAction.getObj() != null && resultAction.getObj() instanceof List<?>){
				
				Log.e("postExecuteTask", "if (resultAction.getObj() != null && resultAction.getObj() instanceof List<?>)");
				
				lista = (List<?>) resultAction.getObj();
				if (lista != null && lista.size() > 0) {
					if (lista.get(0) instanceof Consulta){
						@SuppressWarnings("unchecked")
						List<Consulta> tmpLista = (List<Consulta>)lista;
						mConsultas.clear();
						mConsultas.addAll(tmpLista);
						mCountWSCommands--;
						Log.i("mCountWSCoomands", "Tarefas restantes="+mCountWSCommands);
					}
					if (lista.get(0) instanceof Atendimento){
						@SuppressWarnings("unchecked")
						List<Atendimento> tmpAtendimento = (List<Atendimento>)lista;
						mAtendimentos.clear();
						mAtendimentos.addAll(tmpAtendimento);
						mCountWSCommands--;
						Log.i("mCountWSCoomands", "Tarefas restantes="+mCountWSCommands);
					}
				}else{
					closeFragmentListaConsultas();
					Toast.makeText(this, R.string.msg_no_scheduled_appointments_found, Toast.LENGTH_SHORT).show();
				}
			}
		}else{
			closeFragmentListaConsultas();
			Toast.makeText(this, R.string.msg_no_scheduled_appointments_found, Toast.LENGTH_SHORT).show();
		}
		if (mCountWSCommands == 0){
			mCountWSCommands = -1;
			Log.i(null, "chmando...showFragmentListaConsultas()...");
			showFragmentListaConsultas();
		}
	}
	

}
