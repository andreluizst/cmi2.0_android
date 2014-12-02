package br.com.android.cmi.cmiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import br.com.android.cmi.model.MainVisibleOptions;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.storage.RepositorioUsuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCautenticarUsuario;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;


public class MainActivity extends Activity implements OnPostExecuteTask, OnClickListener{
	
	private RepositorioUsuario mRepositorio;
	private Usuario mUsuario;
	private MenuItem mnChangePassword, mnConsultarMedico, mnAvaliarMedico, mnCancelarAgendamento, 
						mnPesquiarConsultasAgendadas, mnConsultarHistoricoAtendimento;
	private ViewMessage mViewMessage;
	private ProgressBar mProgressBar;
	
	private LinearLayout mLayoutDoctor, mLayoutSettings, mLayoutSearchSchedluledAppointments, mLayoutChangePassword;
	private MainVisibleOptions mVisibleOptions;
	private boolean mUsuarioAutenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mUsuarioAutenticado = false;
        mVisibleOptions = null;//será instanciado após a primeira mudança de orientação da tela
        mViewMessage = new ViewMessage(this);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarMainActivity);
        
        mLayoutDoctor = (LinearLayout)findViewById(R.id.llSeekDoctors);
        mLayoutDoctor.setOnClickListener(this);
        mLayoutSettings = (LinearLayout)findViewById(R.id.llSettings);
        mLayoutSettings.setOnClickListener(this);
        mLayoutSearchSchedluledAppointments = (LinearLayout)findViewById(R.id.llScheduledAppointments);
        mLayoutSearchSchedluledAppointments.setOnClickListener(this);
        mLayoutChangePassword = (LinearLayout)findViewById(R.id.llChangePassword);
        mLayoutChangePassword.setOnClickListener(this);
        
        Log.i("create MainActivity:", "Instanciando o respositório....");

        Context context = this;
        mRepositorio = new RepositorioUsuario(context);
        
        if (savedInstanceState !=null){
        	setUsuario((Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA));
        	mVisibleOptions = (MainVisibleOptions)savedInstanceState.getSerializable(Util.MAIN_VISIBLE_OPTIONS_EXTRA);
        	mLayoutDoctor.setVisibility(mVisibleOptions.seekDoctors?View.VISIBLE:View.GONE);
        	mLayoutSearchSchedluledAppointments.setVisibility(mVisibleOptions.searchScheduledAppointments?View.VISIBLE:View.GONE);
        	mLayoutChangePassword.setVisibility(mVisibleOptions.changePassword?View.VISIBLE:View.GONE);
        	mUsuarioAutenticado = savedInstanceState.getBoolean(Util.IS_USUARIO_AUTENTICADO_EXTRA);
		}
        
        authUser();
        
    }
    
	private boolean isTablet(){
		//return findViewById(R.id.content) != null;
		return false;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.USER_EXTRA, mUsuario);
		updateVisibleOptionsField();
		
		Log.i(null, "outState.putSerializable(Util.MAIN_VISIBLE_OPTIONS_EXTRA, mVisibleOptions)...");
		
		outState.putSerializable(Util.MAIN_VISIBLE_OPTIONS_EXTRA, mVisibleOptions);
		outState.putBoolean(Util.IS_USUARIO_AUTENTICADO_EXTRA, mUsuarioAutenticado);
	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if ((requestCode == LoginActivity.LOGIN_ACTIVITY_RESPONSE || requestCode == LoginActivity.CONFIG_ACTIVITY_RESPONSE
    			|| requestCode == LoginActivity.LOGIN_ACTIVITY_RESPONSE_WITH_USER
    			|| requestCode == LoginActivity.CHANGE_PASSWORD_ACTIVITY_RESPONSE) 
    			&& resultCode == RESULT_OK){
    		mUsuarioAutenticado = true;
    		setUsuario((Usuario)data.getSerializableExtra(Util.USER_EXTRA));
    	
    		Log.i("onActivityResult", mUsuario.getLogin() + "|" + mUsuario.getSenha()+"|"+mUsuario.getPerfil().getDescricao());

    	}
    }
    
    private void updatePermissions(){
    	if (mUsuario !=null && mnConsultarMedico != null && mUsuarioAutenticado){
    		
    		Log.i("updatePermissions", mUsuario.getLogin() + "|" + mUsuario.getSenha()+"|"+mUsuario.getPerfil().getDescricao());
    		Log.d("updatePermissions", "if (mUsuario !=null && mnConsultarMedico != null && mUsuarioAutenticado)...");
    		
    		if (mVisibleOptions == null){
    			
    			Log.d("updatePermissions", "if (mVisibleOptions == null)...");
    			
    			mnConsultarMedico.setVisible(Util.usuarioTemPerfil(mUsuario, Util.ADMINISTRADOR, Util.PACIENTE));
    			mnPesquiarConsultasAgendadas.setVisible(Util.usuarioTemPerfil(mUsuario, Util.ADMINISTRADOR, Util.PACIENTE));
    			mnChangePassword.setVisible(Util.usuarioTemPerfil(mUsuario, Util.ADMINISTRADOR, Util.PACIENTE));
    			mLayoutDoctor.setVisibility(mnConsultarMedico.isVisible()?View.VISIBLE:View.GONE);
    			mLayoutSearchSchedluledAppointments.setVisibility(mnPesquiarConsultasAgendadas.isVisible()?View.VISIBLE:View.GONE);
				mLayoutChangePassword.setVisibility(mnChangePassword.isVisible()?View.VISIBLE:View.GONE);
    		}else{
    			mnConsultarMedico.setVisible(mVisibleOptions.seekDoctors);
    			mnPesquiarConsultasAgendadas.setVisible(mVisibleOptions.searchScheduledAppointments);
    			mnChangePassword.setVisible(mVisibleOptions.changePassword);
    		}

    	}else{
    		
    		Log.d("updatePermissions", "blockOptionsMenu()...");
    		
    		blockOptionsMenu();
    	}
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	Log.i("MainActivity", "onCreateOptionsMenu...");
    	
        getMenuInflater().inflate(R.menu.main, menu);
        mnChangePassword = (MenuItem)menu.findItem(R.id.action_change_password);
        mnConsultarMedico = (MenuItem)menu.findItem(R.id.action_consultar_medico);
        mnPesquiarConsultasAgendadas = (MenuItem)menu.findItem(R.id.action_search_for_scheduled_appointments);
        mnAvaliarMedico = (MenuItem)menu.findItem(R.id.action_avaliar_medico);
    	mnCancelarAgendamento = (MenuItem)menu.findItem(R.id.action_cancelar_agendamento);
    	mnConsultarHistoricoAtendimento = (MenuItem)menu.findItem(R.id.action_consultar_historico_atendimento);
        //blockOptionsMenu();
    	updatePermissions();
        return true;
    }

    private void blockOptionsMenu() {
		if (mnConsultarMedico != null) {
			
			Log.d("blockOptionsMenu", "executando...");
			
			mnConsultarMedico.setVisible(false);
			mnAvaliarMedico.setVisible(false);
			mnCancelarAgendamento.setVisible(false);
			mnPesquiarConsultasAgendadas.setVisible(false);
			mnConsultarHistoricoAtendimento.setVisible(false);
			mnChangePassword.setVisible(false);
			mLayoutDoctor.setVisibility(View.GONE);
			mLayoutSearchSchedluledAppointments.setVisibility(View.GONE);
			mLayoutChangePassword.setVisibility(View.GONE);
		}
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
        	case R.id.action_settings:
        		showConfigActivity();
        		break;
        	case R.id.action_consultar_medico:
        		showSearchDoctor();
        		break;
        	case R.id.action_search_for_scheduled_appointments:
        		showSearchScheduledAppointments();        		
        		break;
        	case R.id.action_change_password:
        		changePasswordUser();
        		break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void changePasswordUser() {
		Bundle parametros = new Bundle();
		parametros.putSerializable(Util.USER_EXTRA, mUsuario);
		parametros.putInt(LoginActivity.SHOW_MODE_EXTRA, LoginActivity.CHANGE_PASSWORD_ACTIVITY_RESPONSE);
		showView(LoginActivity.class, parametros, LoginActivity.CHANGE_PASSWORD_ACTIVITY_RESPONSE);
	}

	private void showSearchDoctor() {
		showView(BuscarMedicoActivity.class);
	}
	
	private void showSearchScheduledAppointments(){
		showView(ConsultasAgendadasActivity.class);
	}
	
	private void showView(Class<? extends Activity> classe){
		showView(classe, null, -1);
	}
	
	private void showView(Class<? extends Activity> classe, Bundle parametros, int resultCode){
		Intent it = new Intent(this, classe);
		Bundle bundle;
		if (parametros == null){
			bundle = new Bundle();
			bundle.putSerializable(Util.USER_EXTRA, mUsuario);
		}else{
			bundle = parametros;
		}
		it.putExtras(bundle);
		if (resultCode == -1)
			startActivity(it);
		else
			startActivityForResult(it, resultCode);
	}

	private void authUser(){
		
		Log.i("mainActivity", "authUser()...");
		
		if (mUsuario != null)
			return;
		if (mRepositorio.hasSavedLogin()){
			setUsuario(buscarUsuario());
    		if (mUsuario != null){
    			TaskForWs task;
    			task = new TaskForWs(this, mProgressBar, mViewMessage);
    			WSCommand wsCommand = new WSCautenticarUsuario(this,
    					WebServiceConnection.getWebServiceToUser(mUsuario));
    			task.execute(wsCommand);
    		}else{
    			askLogin();
    		}
    	}else
    		askLogin();
	}
    
    private void askLogin(){
    	Log.i("MainActivity", "askLogin()....");
    	if (isTablet()){
    		Intent it = new Intent(this, LoginActivity.class);
    		if (mUsuario != null)
    			startActivityForResult(it, LoginActivity.LOGIN_ACTIVITY_RESPONSE_WITH_USER);
    		else
    			startActivityForResult(it, LoginActivity.LOGIN_ACTIVITY_RESPONSE);
    	}else{
    		Intent it = new Intent(this, LoginActivity.class);
    		Log.i("askLogin", "it.putExtras");
    		if (mUsuario != null)
    			startActivityForResult(it, LoginActivity.LOGIN_ACTIVITY_RESPONSE_WITH_USER);
    		else
    			startActivityForResult(it, LoginActivity.LOGIN_ACTIVITY_RESPONSE);
    	}
    }
    
    public void showConfigActivity(){
    	Intent it = new Intent(this, LoginActivity.class);
    	it.putExtra(LoginActivity.SHOW_MODE_EXTRA, LoginActivity.CONFIG_ACTIVITY_RESPONSE);
    	startActivityForResult(it, LoginActivity.CONFIG_ACTIVITY_RESPONSE);
    }
    
    private Usuario buscarUsuario(){
    	Usuario u;
    	try {
    		Log.i("buscarUsuario", "pegando usuario do banco");
			u = mRepositorio.listar().get(0);
			return u;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	@Override
	public void postExecuteTask(ResultAction resultAction) {
		if (resultAction.getObj() == null){
		
			Log.i(null, "if (resultAction.getObj() == null)...");
			
			blockOptionsMenu();
		}else{
			if (resultAction.getObj() instanceof Usuario)
				setUsuario((Usuario)resultAction.getObj());
			if (resultAction.getCodResult() == R.string.msg_Invalid_login_or_password){
				Log.i(null, "if (resultAction.getCodResult() == R.string.msg_Invalid_login_or_password)...");
				blockOptionsMenu();
				askLogin();
			}else{
				mUsuarioAutenticado = true;
				updatePermissions();
				updateVisibleOptionsField();
			}
		}
	}
	
	private void updateVisibleOptionsField() {
		
		Log.d(null, "executando updateVisibleOptionsField()...");
		
		if (mVisibleOptions == null)
			mVisibleOptions = new MainVisibleOptions();
		mVisibleOptions.seekDoctors = mnConsultarMedico!=null?mnConsultarMedico.isVisible():false;
		mVisibleOptions.searchScheduledAppointments = mnPesquiarConsultasAgendadas!=null?mnPesquiarConsultasAgendadas.isVisible():false;
		mVisibleOptions.changePassword = mnChangePassword!=null?mnChangePassword.isVisible():false;
	}

	private void setUsuario(Usuario usuario){
		
		Log.d("setUsuario", "executando...");
		
//		if (mUsuario != null && !mUsuario.getLogin().equals(usuario.getLogin()))
			mVisibleOptions = null;
		mUsuario = usuario;
		Util.usuarioAtual = mUsuario;
		updatePermissions();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.llSettings:
				showConfigActivity();
				break;
			case R.id.llSeekDoctors:
				showSearchDoctor();
				break;
			case R.id.llScheduledAppointments:
				showSearchScheduledAppointments();
				break;
			case R.id.llChangePassword:
				changePasswordUser();
        		break;
		}
	}
	

}
