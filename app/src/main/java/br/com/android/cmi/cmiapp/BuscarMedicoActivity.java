package br.com.android.cmi.cmiapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
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
import br.com.android.cmi.cmiapp.MedicoListFragment.OnMedicoListClick;
import br.com.android.cmi.model.Cidade;
import br.com.android.cmi.model.Especialidade;
import br.com.android.cmi.model.Estado;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.PlanoDeSaude;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCbuscarMedico;
import br.com.android.cmi.ws.WSClistarCidadesDoEstado;
import br.com.android.cmi.ws.WSClistarEspecialidades;
import br.com.android.cmi.ws.WSClistarEstados;
import br.com.android.cmi.ws.WSClistarPlanosDeSaude;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;



public class BuscarMedicoActivity extends Activity implements OnPostExecuteTask, OnItemSelectedListener, OnKeyListener,
			OnMedicoListClick {
	
	private static final String ESPECIALIDADES_EXTRA = "especialidades_extra";
	private static final String ESTADOS_EXTRA = "estados_extra";
	private static final String CIDADES_EXTRA = "cidades_extra";
	private static final String PLANOS_EXTRA = "planos_extra";
	private static final String ESPECIALIDADE_SELECIONADA_EXTRA = "especialidade_selecionada_extra";
	private static final String ESTADO_SELECIONADO_EXTRA = "estado_selecionado_extra";
	private static final String CIDADE_SELECIONADA_EXTRA = "cidade_selecionada_extra";
	private static final String PLANO_SELECIONADO_EXTRA = "plano_selecionado_extra";
	private static final String NOME_MEDICO_EXTRA = "nome_medico";
	private static final String NOME_CLINICA_EXTRA = "nome_clinica";
	private static final String AGENDA_VISIVEL_EXTRA = "agenda_visivel_extra";
	private static final String TAG_MEDICO_LIST_FRAGMENT = "tagMedicoListFragment";
	
	private Usuario mUsuario;
	private ViewMessage mViewMessage;
	private ArrayList<Especialidade> mEspecialidades;
	private ArrayAdapter<Especialidade> mEspecialidadeAdapter;
	private ArrayList<Estado> mEstados;
	private ArrayAdapter<Estado> mEstadosAdapter;
	private ArrayList<Cidade> mCidades;
	private ArrayAdapter<Cidade> mCidadesAdapter;
	private ArrayList<PlanoDeSaude> mPlanos;
	private ArrayAdapter<PlanoDeSaude> mPlanosAdapter;
	private ArrayList<Funcionario> mMedicos;
	private Integer mCidadeIndex;
	private Integer mEstadoIndex;
	private Integer mEspecialidadeIndex;
	private Integer mPlanoIndex;
	private boolean mViewRecriada;
	private int mCountItemSelectedAfterRecreateView;
	
	
	private EditText edtNomeMedico;
	private EditText edtNomeClinica;
	private Spinner spnEspecialidades;
	private Spinner spnEstados;
	private Spinner spnCidades;
	private Spinner spnPlanos;
	private ProgressBar mProgressBar;
	private String mNomeMedico;
	private String mNomeClinica;
	
	private boolean mListCreated;
	private TextView txtAux;
	private FrameLayout mFrameAgendaTablet;
	private boolean mFragmentAgendaEstaVisivel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscar_medico);
		
		txtAux = new TextView(this);
		mViewMessage = new ViewMessage(this);
		mViewRecriada = false;
		mListCreated = false;
		mFragmentAgendaEstaVisivel = false;
		mCountItemSelectedAfterRecreateView = 0;
		
		if (isTablet()){
			mFrameAgendaTablet = (FrameLayout)findViewById(R.id.fragmentAgendaMedico_tablet);
		}
		
		edtNomeMedico = (EditText)findViewById(R.id.edtDoctorNameFilter);
		edtNomeMedico.setOnKeyListener(this);
		edtNomeClinica = (EditText)findViewById(R.id.edtClinicNameFilter);
		edtNomeClinica.setOnKeyListener(this);
		
		mEspecialidadeIndex = 0;
		mEspecialidades = new ArrayList<Especialidade>();
		txtAux.setText(R.string.all_female);
		mEspecialidades.add(new Especialidade(0, txtAux.getText().toString()));
		mEspecialidadeAdapter = new ArrayAdapter<Especialidade>(this, R.layout.spinner_item, mEspecialidades);
		mEspecialidadeAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnEspecialidades = (Spinner)findViewById(R.id.spnSpecialtyFilter);
		spnEspecialidades.setAdapter(mEspecialidadeAdapter);
		
		mEstadoIndex = 0;
		mEstados = new ArrayList<Estado>();
		Estado estado = new Estado();
		txtAux.setText(R.string.all_male);
		estado.setUf(txtAux.getText().toString());
		mEstados.add(estado);
		mEstadosAdapter = new ArrayAdapter<Estado>(this, R.layout.spinner_item, mEstados);
		mEstadosAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnEstados = (Spinner)findViewById(R.id.spnStateFilter);
		spnEstados.setAdapter(mEstadosAdapter);

		mCidadeIndex = 0;
		mCidades = new ArrayList<Cidade>();
		txtAux.setText(R.string.all_female);
		mCidades.add(new Cidade(0, txtAux.getText().toString()));
		mCidadesAdapter = new ArrayAdapter<Cidade>(this, R.layout.spinner_item, mCidades);
		mCidadesAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnCidades = (Spinner)findViewById(R.id.spnCityFilter);
		spnCidades.setAdapter(mCidadesAdapter);
		
		mPlanoIndex = 0;
		mPlanos = new ArrayList<PlanoDeSaude>();
		txtAux.setText(R.string.all_male);
		mPlanos.add(new PlanoDeSaude(0, txtAux.getText().toString()));
		mPlanosAdapter = new ArrayAdapter<PlanoDeSaude>(this, R.layout.spinner_item, mPlanos);
		mPlanosAdapter.setDropDownViewResource(R.layout.spinner_dorpdown_item);
		spnPlanos = (Spinner)findViewById(R.id.spnHealthPlanFilter);
		spnPlanos.setAdapter(mPlanosAdapter);
		
		mMedicos = new ArrayList<Funcionario>();
		
		if (savedInstanceState != null){
			mViewRecriada = true;
			mUsuario = (Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA);
			@SuppressWarnings("unchecked")
			ArrayList<Especialidade> especialidades = (ArrayList<Especialidade>)savedInstanceState.getSerializable(ESPECIALIDADES_EXTRA); 
			atualizarEspecialidades(especialidades, savedInstanceState.getInt(ESPECIALIDADE_SELECIONADA_EXTRA, 0));
			@SuppressWarnings("unchecked")
			ArrayList<Estado> estados = (ArrayList<Estado>)savedInstanceState.getSerializable(ESTADOS_EXTRA);
			atualizarEstados(estados, savedInstanceState.getInt(ESTADO_SELECIONADO_EXTRA, 0));
			@SuppressWarnings("unchecked")
			ArrayList<Cidade> cidades = (ArrayList<Cidade>)savedInstanceState.getSerializable(CIDADES_EXTRA);
			atualizarCidades(cidades, savedInstanceState.getInt(CIDADE_SELECIONADA_EXTRA, 0));
			@SuppressWarnings("unchecked")
			ArrayList<PlanoDeSaude> planos = (ArrayList<PlanoDeSaude>)savedInstanceState.getSerializable(PLANOS_EXTRA); 
			atualizarPlanos(planos, savedInstanceState.getInt(PLANO_SELECIONADO_EXTRA, 0));
			mNomeMedico = savedInstanceState.getString(NOME_MEDICO_EXTRA);
			mNomeClinica = savedInstanceState.getString(NOME_CLINICA_EXTRA);
			mFragmentAgendaEstaVisivel = savedInstanceState.getBoolean(AGENDA_VISIVEL_EXTRA);
			if (isTablet()){
				mFrameAgendaTablet.setVisibility(mFragmentAgendaEstaVisivel?View.VISIBLE:View.GONE);
			}
		}else{
			Intent it = getIntent();
			mUsuario = (Usuario)it.getSerializableExtra(Util.USER_EXTRA);
			pegarEspecialidadesNoWebService();
			pegarEstadosNoWebService();
			pegarPlanosDeSaudeNoWebService();
		}
		spnEstados.setOnItemSelectedListener(this);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBarSearchDoctor);
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.USER_EXTRA, mUsuario);
		outState.putSerializable(ESPECIALIDADES_EXTRA, mEspecialidades);
		outState.putSerializable(ESTADOS_EXTRA, mEstados);
		outState.putSerializable(CIDADES_EXTRA, mCidades);
		outState.putInt(ESPECIALIDADE_SELECIONADA_EXTRA, mEspecialidadeIndex);
		outState.putSerializable(PLANOS_EXTRA, mPlanos);
		outState.putInt(ESTADO_SELECIONADO_EXTRA, mEstadoIndex);
		outState.putInt(CIDADE_SELECIONADA_EXTRA, mCidadeIndex);
		outState.putInt(PLANO_SELECIONADO_EXTRA, mPlanoIndex);
		outState.putString(NOME_MEDICO_EXTRA, mNomeMedico);
		outState.putString(NOME_CLINICA_EXTRA, mNomeClinica);
		outState.putBoolean(AGENDA_VISIVEL_EXTRA, mFragmentAgendaEstaVisivel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.buscar_medico, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_seek_doctor)
			seekDoctor();
		if (id == R.id.action_clear)
			clearFilters();
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isTablet(){
		return findViewById(R.id.fragmentListaMedicos_tablet) != null;
	}
	
	private void clearFilters() {
		mNomeMedico = "";
		mNomeClinica = "";
		edtNomeMedico.setText(mNomeMedico);
		edtNomeClinica.setText(mNomeClinica);
		spnEspecialidades.setSelection(0);
		spnEstados.setSelection(0);
		spnPlanos.setSelection(0);
	}

	private void seekDoctor() {
		buscarMedicoNoWebService();		
	}
	
	private void showListResult(){
		if (isTablet()){
			closeAgendaFragment();
			MedicoListFragment fragment = MedicoListFragment.novaInstancia(mMedicos);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fragmentListaMedicos_tablet, fragment, TAG_MEDICO_LIST_FRAGMENT);
			ft.commit();
			mListCreated = true;
		}else{
			Intent it = new Intent(this, ListaMedicosActivity.class);
			it.putExtra(Util.LISTA_FUNCIONARIOS, mMedicos);
			startActivityForResult(it, 0);
		}
	}

	private void pegarEspecialidadesNoWebService(){
		TaskForWs task = new TaskForWs(this);
		WSCommand wsCommand = new WSClistarEspecialidades(this, WebServiceConnection.getWebServiceToUser(mUsuario));
		task.execute(wsCommand);
	}
	
	private void atualizarEspecialidades(List<Especialidade> lista){
		atualizarEspecialidades(lista, null);
	}
	
	private void atualizarEspecialidades(List<Especialidade> lista, Integer index){
		mEspecialidades.clear();
		if (index == null){
			txtAux.setText(R.string.all_female);
			mEspecialidades.add(new Especialidade(0, txtAux.getText().toString()));
		}
		mEspecialidades.addAll(lista);
		mEspecialidadeAdapter.notifyDataSetChanged();
		if (index == null)
			spnEspecialidades.setSelection(0);
		else
			spnEspecialidades.setSelection(index);
	}
	
	private void pegarEstadosNoWebService(){
		TaskForWs task = new TaskForWs(this);
		WSCommand wsCommand = new WSClistarEstados(this, WebServiceConnection.getWebServiceToUser(mUsuario));
		task.execute(wsCommand);
	}
	
	private void atualizarEstados(List<Estado> lista){
		atualizarEstados(lista, null);
	}
	
	private void atualizarEstados(List<Estado> lista, Integer index){
		mEstados.clear();
		if (index == null){
			Estado estado = new Estado();
			txtAux.setText(R.string.all_male);
			estado.setUf(txtAux.getText().toString());
			mEstados.add(estado);
		}
		mEstados.addAll(lista);
		mEstadosAdapter.notifyDataSetChanged();
		if (index == null)
			spnEstados.setSelection(0);
		else
			spnEstados.setSelection(index);
	}
	
	private void pegarCidadesDoEstadoNoWebService(Estado estado){
		TaskForWs task = new TaskForWs(this);
		WSCommand wsCommand = new WSClistarCidadesDoEstado(this, WebServiceConnection.getWebServiceToUser(mUsuario), estado);
		task.execute(wsCommand);
	}
	
	private void atualizarCidades(List<Cidade> lista){
		atualizarCidades(lista, null);
	}
	
	private void atualizarCidades(List<Cidade> lista, Integer index){
		mCidades.clear();
		if (index == null){
			txtAux.setText(R.string.all_female);
			mCidades.add(new Cidade(0, txtAux.getText().toString()));
		}
		if (lista != null)
			mCidades.addAll(lista);
		mCidadesAdapter.notifyDataSetChanged();
		if (index == null)
			spnCidades.setSelection(0);
		else
			spnCidades.setSelection(index);
	}
	
	private void pegarPlanosDeSaudeNoWebService(){
		TaskForWs task = new TaskForWs(this);
		WSCommand wsCommand = new WSClistarPlanosDeSaude(this, WebServiceConnection.getWebServiceToUser(mUsuario));
		task.execute(wsCommand);
	}
	
	private void atualizarPlanos(List<PlanoDeSaude> lista) {
		atualizarPlanos(lista, null);
	}
	
	private void atualizarPlanos(List<PlanoDeSaude> lista, Integer index) {
		mPlanos.clear();
		if (index == null){
			txtAux.setText(R.string.all_male);
			mPlanos.add(new PlanoDeSaude(0, txtAux.getText().toString()));
		}
		mPlanos.addAll(lista);
		mPlanosAdapter.notifyDataSetChanged();
		if (index == null)
			spnPlanos.setSelection(0);
		else
			spnPlanos.setSelection(index);
	}
	
	private void buscarMedicoNoWebService(){
		String uf = "0";
		uf = mEstados.get(spnEstados.getSelectedItemPosition()).getUf(); 
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCbuscarMedico(this, WebServiceConnection.getWebServiceToUser(mUsuario),
				mNomeMedico, mNomeClinica, uf,
				mCidades.get(spnCidades.getSelectedItemPosition()).getId(),
				mEspecialidades.get(spnEspecialidades.getSelectedItemPosition()).getCodEspecialidade(),
				mPlanos.get(spnPlanos.getSelectedItemPosition()).getId());
		task.execute(wsCommand);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postExecuteTask(ResultAction resultAction) {
		List<Especialidade> especialidades = new ArrayList<Especialidade>();
		List<Estado> estados = new ArrayList<Estado>();
		List<Cidade> cidades = new ArrayList<Cidade>();
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		List<PlanoDeSaude> planos = new ArrayList<PlanoDeSaude>();
		List<?> lista = null;
		if (resultAction != null){
			if (resultAction.getObj() != null && resultAction.getObj() instanceof List<?>){
				lista = (List<?>) resultAction.getObj();
				if (lista != null && lista.size() > 0) {
					if (lista.get(0) instanceof Especialidade) {
						especialidades = (List<Especialidade>) lista;
						atualizarEspecialidades(especialidades);
					} else if (lista.get(0) instanceof Estado) {
						estados = (List<Estado>) lista;
						atualizarEstados(estados);
					} else if (lista.get(0) instanceof Cidade) {
						cidades = (List<Cidade>) lista;
						atualizarCidades(cidades);
						if ( mViewRecriada){
							if (mCidadeIndex != null)
								spnEspecialidades.setSelection(mCidadeIndex);
							mViewRecriada = false;
						}
					}else if(lista.get(0) instanceof Funcionario){
						funcionarios = (List<Funcionario>)lista;
						mMedicos.clear();
						mMedicos.addAll(funcionarios);
						if (mMedicos.size() > 0)
							showListResult();
						else
							closeDetailFragment();
					}else if (lista.get(0) instanceof PlanoDeSaude){
						planos = (List<PlanoDeSaude>)lista;
						atualizarPlanos(planos);
					}
				}
			}else{
				if (isTablet())
					closeDetailFragment();
			}
		}
	}
	
	private void closeDetailFragment(){
		if (mListCreated) {
			closeAgendaFragment();
			FragmentManager fm;
			FragmentTransaction ft;
			fm = getFragmentManager();
			ft = fm.beginTransaction();
			MedicoListFragment detalhe = (MedicoListFragment) fm.findFragmentByTag(TAG_MEDICO_LIST_FRAGMENT);
			ft.remove(detalhe);
			ft.commit();
			mListCreated = false;
		}
	}
	
	private void closeAgendaFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		AgendarConsultaFragment agendaFragment = (AgendarConsultaFragment)fm.findFragmentByTag(Util.TAG_AGENDAR_CONSULTA_TABLET);
		if (agendaFragment != null){
			ft.remove(agendaFragment);
			ft.commit();
			mFrameAgendaTablet.setVisibility(View.GONE);
			mFragmentAgendaEstaVisivel = false;
		}
	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//		Log.i("BucarMedicoActivity", "onItemSelected...");
		mCountItemSelectedAfterRecreateView++;
		switch (parent.getId()) {
			case R.id.spnStateFilter:
				//Log.i("onItemSelected", "pegarCidadesDoEstadoNoWebService((Estado)parent.getSelectedItem())...");
				if (mViewRecriada && mCountItemSelectedAfterRecreateView > 2){
					//Log.i("BucarMedicoActivity", "onItemSelected...mViewRecriada==true...");
					mViewRecriada = false;
				}
				if (!mViewRecriada){
					mEstadoIndex = position;
					if (position == 0) {
						atualizarCidades(null);
					} else {
						//Log.i("BucarMedicoActivity", "onItemSelected...mViewRecriada==false...");
						pegarCidadesDoEstadoNoWebService((Estado) parent.getSelectedItem());
					}
				}
				break;
			case R.id.spnSpecialtyFilter:
				mEspecialidadeIndex = position;
				break;
			case R.id.spnCityFilter:
				mCidadeIndex = position;
				break;
			case R.id.spnHealthPlanFilter:
				mPlanoIndex = position;
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((v.getId() == R.id.edtDoctorNameFilter) || (v.getId() == R.id.edtClinicNameFilter)){
			mNomeMedico = edtNomeMedico.getText().toString();
			mNomeClinica = edtNomeClinica.getText().toString();
		}
		return false;
	}


	@Override
	public void onSubItemMedicoClick(Funcionario funcionario) {
		closeAgendaFragment();
		AgendarConsultaFragment fragment = AgendarConsultaFragment.novaInstancia(Util.usuarioAtual, funcionario, 
				Util.usuarioAtual.getPaciente(), funcionario.getClinicas().get(0));
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragmentAgendaMedico_tablet, fragment, Util.TAG_AGENDAR_CONSULTA_TABLET);
		fragmentTransaction.commit();
		mFrameAgendaTablet = (FrameLayout)findViewById(R.id.fragmentAgendaMedico_tablet);
		mFrameAgendaTablet.setVisibility(View.VISIBLE);
		mFragmentAgendaEstaVisivel = true;
	}


}
