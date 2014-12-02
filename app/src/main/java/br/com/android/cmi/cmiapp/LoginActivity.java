package br.com.android.cmi.cmiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import br.com.android.cmi.model.Config;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.storage.RepositorioUsuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCalterarSenha;
import br.com.android.cmi.ws.WSCautenticarUsuario;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;

public class LoginActivity extends Activity implements OnClickListener, OnPostExecuteTask, OnKeyListener, OnEditorActionListener {


	public static final int LOGIN_ACTIVITY_RESPONSE = 1;
	public static final int CONFIG_ACTIVITY_RESPONSE = 2;
	public static final int LOGIN_ACTIVITY_RESPONSE_WITH_USER = 3;
	public static final int CHANGE_PASSWORD_ACTIVITY_RESPONSE = 4;
	public static final String SHOW_MODE_EXTRA = "SHOW_MODE_EXTRA";
	public static final String NEW_PASSWORD_EXTRA = "NEW_PASSWORD_EXTRA";
	public static final String CONFIRM_NEW_PASSWORD_EXTRA = "CONFIRM_NEW_PASSWORD_EXTRA";

	private Config mConfig;
	private Usuario mUser;
	private RepositorioUsuario mRep;

	private EditText edtLogin;
	private EditText edtPassword, edtServerAddress, edtNewPassword, edtConfirmNewPassword;
	private CheckBox chkSaveLogin;
	private ProgressBar mProgressBar;
	private TextView mTextMessage, mTxtvRestoreServer;
	private ViewMessage mViewMessage;
	private Button btnOk;
	private int mViewMode = LOGIN_ACTIVITY_RESPONSE;
	private LinearLayout llNewPassword;
	private String mNewPassword;
	private String mConfirmNewPassword;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Intent it = getIntent();
		
		if (savedInstanceState !=null){
			mConfig = (Config)savedInstanceState.getSerializable(Util.CONFIG_EXTRA);
			mUser = (Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA);
			mNewPassword = savedInstanceState.getString(NEW_PASSWORD_EXTRA);
			mConfirmNewPassword = savedInstanceState.getString(CONFIRM_NEW_PASSWORD_EXTRA);
			mViewMode = savedInstanceState.getInt(SHOW_MODE_EXTRA);
			loadModels();
		}else{
			mViewMode = it.getIntExtra(SHOW_MODE_EXTRA, LOGIN_ACTIVITY_RESPONSE);
		}
	
		((LinearLayout) findViewById(R.id.layout_server_address))
				.setVisibility(mViewMode == CONFIG_ACTIVITY_RESPONSE?View.VISIBLE:View.GONE);

		mViewMessage = new ViewMessage(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mTextMessage = new TextView(this);

		edtLogin = (EditText) findViewById(R.id.edtLogin);
		edtLogin.setOnKeyListener(this);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtPassword.setOnKeyListener(this);
		edtPassword.setOnEditorActionListener(this);
		edtNewPassword = (EditText)findViewById(R.id.edtNewPassword);
		edtNewPassword.setOnKeyListener(this);
		edtConfirmNewPassword = (EditText)findViewById(R.id.edtConfirmNewPassword);
		edtConfirmNewPassword.setOnKeyListener(this);
		llNewPassword = (LinearLayout)findViewById(R.id.llNewPassword);
		llNewPassword.setVisibility(mViewMode==CHANGE_PASSWORD_ACTIVITY_RESPONSE?View.VISIBLE:View.GONE);
		edtServerAddress = (EditText) findViewById(R.id.edtServerAddress);
		
		chkSaveLogin = (CheckBox) findViewById(R.id.chkSaveLogin);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
		btnOk = (Button) findViewById(R.id.btnLoginOk);
		btnOk.setOnClickListener(this);
		((Button) findViewById(R.id.btnLoginCancel)).setOnClickListener(this);
		mTxtvRestoreServer = (TextView) findViewById(R.id.txtvLoginRestoreDefaultServerAddress);
		mTxtvRestoreServer.setOnClickListener(this);
		
		Log.i("LoginActivity", "mRep = new RepositorioUsuario(this)...");
		
		mRep = new RepositorioUsuario(this);
		
		Log.i("LoginActivity", "mConfig = mRep.getConfig()...");
		
		mConfig = mRep.getConfig();
		Log.i("LoginActivity", "mUser = mRep.listar().get(0)...");
		try {
			
			if (mRep.listar() != null && mRep.listar().size() > 0){
				mUser = mRep.listar().get(0);
			
				Log.w("INFO", mUser.getCodUsuario() + " - " + mUser.getLogin() + " - " + mUser.getSenha());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			mUser = null;
		}
		this.setTitle(R.string.login);
		if (mViewMode == CONFIG_ACTIVITY_RESPONSE 
				|| mViewMode == LOGIN_ACTIVITY_RESPONSE_WITH_USER
				|| mViewMode == CHANGE_PASSWORD_ACTIVITY_RESPONSE) {
			if (mViewMode == CONFIG_ACTIVITY_RESPONSE)
				this.setTitle(R.string.action_settings);
			if (mViewMode == CHANGE_PASSWORD_ACTIVITY_RESPONSE){
				this.setTitle(R.string.change_password);
				edtPassword.setImeOptions(EditorInfo.IME_FLAG_NAVIGATE_NEXT);
				TextView txtAux = new TextView(this);
				txtAux.setText(R.string.next);
				edtPassword.setImeActionLabel(txtAux.getText().toString(), EditorInfo.IME_ACTION_NEXT);
			}
			btnOk.setText(R.string.save);
			chkSaveLogin.setVisibility(View.INVISIBLE);
			
			Log.i("LoginActivity", "mostrando mConfig na view...");
			
			if (mConfig != null) {
				if (mConfig.getServerAddress() != null && !mConfig.getServerAddress().isEmpty())
					edtServerAddress.setText(mConfig.getServerAddress());
				else
					edtServerAddress.setText(WebServiceConnection.URI_CMI);
				if (mViewMode == CONFIG_ACTIVITY_RESPONSE || mViewMode == CHANGE_PASSWORD_ACTIVITY_RESPONSE)
					chkSaveLogin.setChecked(true);
				else
					chkSaveLogin.setChecked(mConfig.isSaveLogin());
				
			}else{
				edtServerAddress.setText(WebServiceConnection.URI_CMI);
			}
			
//			Log.i("LoginActivity", "mostrando mUser na view...");
			
			if (mUser != null){
				edtLogin.setText(mUser.getLogin());
				edtPassword.setText(mUser.getSenha());
			}
		} //else
//			this.setTitle(R.string.login);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtvLoginRestoreDefaultServerAddress:
			edtServerAddress.setText(WebServiceConnection.URI_CMI);
			break;
		case R.id.btnLoginOk:
			if (mViewMode==CHANGE_PASSWORD_ACTIVITY_RESPONSE)
				changePassword();
			else
				login();
			break;
		case R.id.btnLoginCancel:
			finish();
			break;
		}
	}
	
	private void changePassword(){
		if (checkFields()){
			Usuario usuarioComNovaSenha = new Usuario();
			usuarioComNovaSenha.setLogin(mUser.getLogin());
			usuarioComNovaSenha.setSenha(edtNewPassword.getText().toString());
			TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
			WSCommand wsCommand = new WSCalterarSenha(this, WebServiceConnection.getWebServiceToUser(mUser), usuarioComNovaSenha);
			task.execute(wsCommand);
		}
	}
	
	private void login(){
		login(null, true);
	}

	private void login(Usuario usuario, boolean showSuccessfulMessage) {
		String uriServerRoot = null;
		if (checkFields()){
			if (mViewMode == CONFIG_ACTIVITY_RESPONSE) {
				uriServerRoot = edtServerAddress.getText().toString();
			}

			// if (uriServerRoot == null)
			// Log.i("LoginActivity.login()", "(uriServerRoot == NULL");
			// else
			// Log.i("LoginActivity.login()", "(uriServerRoot -> " +
			// uriServerRoot);

			TaskForWs task;
			updateModels();
			task = new TaskForWs(this, mProgressBar, mViewMessage);
			Usuario u;
			if (usuario == null){
				u = mUser;
			}
			else{
				u = usuario;

			}
			
			Log.e("login", u.getLogin() + " | " + u.getSenha() + " | " + u.getPerfil().getDescricao());
			
			WSCommand wsCommand = new WSCautenticarUsuario(this, uriServerRoot, WebServiceConnection.getWebServiceToUser(u),
					showSuccessfulMessage);
			task.execute(wsCommand);
		}
	}
	
	public boolean checkFields(){
		if (edtLogin.getText().toString().isEmpty()) {
			mTextMessage.setText(R.string.msg_required_field);
			edtLogin.setError(mTextMessage.getText().toString());
			return false;
		}
		if (edtPassword.getText().toString().isEmpty()) {
			mTextMessage.setText(R.string.msg_required_field);
			edtPassword.setError(mTextMessage.getText().toString());
			return false;
		}
		if (mViewMode == CONFIG_ACTIVITY_RESPONSE){
			if (edtServerAddress.getText().toString().isEmpty()) {
				mTextMessage.setText(R.string.msg_required_field);
				edtServerAddress.setError(mTextMessage.getText().toString());
				return false;
			}
		}
		if (mViewMode==CHANGE_PASSWORD_ACTIVITY_RESPONSE){
			if (edtNewPassword.getText().toString().isEmpty()) {
				mTextMessage.setText(R.string.msg_required_field);
				edtNewPassword.setError(mTextMessage.getText().toString());
				return false;
			}
			if (edtConfirmNewPassword.getText().toString().isEmpty()) {
				mTextMessage.setText(R.string.msg_required_field);
				edtConfirmNewPassword.setError(mTextMessage.getText().toString());
				return false;
			}
			if (!edtNewPassword.getText().toString().equals(edtConfirmNewPassword.getText().toString())){
				mTextMessage.setText(R.string.msg_invalid_password);
				edtConfirmNewPassword.setError(mTextMessage.getText().toString());
				return false;
			}
		}
		return true;
	}

	@Override
	public void postExecuteTask(ResultAction resultAction) {
		if (resultAction.getObj() != null && resultAction.getObj() instanceof Usuario)
			mUser = (Usuario)resultAction.getObj();
		if (resultAction.getCodResult() == R.string.connected){
			Log.i(null, "if (mProgressBar.getVisibility() == View.INVISIBLE){...");
			if (chkSaveLogin.isChecked() || mViewMode == CONFIG_ACTIVITY_RESPONSE) {
				mRep.setConfig(mConfig);
				
//				Log.i("LoginActivity", "chkSaveLogin.isChecked() == " + chkSaveLogin.isChecked());
//				Log.i("INFO", "mostrando mUser da activity que foi passado para a task..");
				Log.i("PostExecuteTask", mUser.getLogin() + " - " + mUser.getSenha() + " - " + mUser.getPerfil().getDescricao());
				
				mRep.saveUser(mUser);
			}
			mProgressBar.setVisibility(View.GONE);
			closeActivityResultOk();
		}
		if (resultAction.getCodResult() == R.string.msg_successfully_changed_password){
			mUser.setSenha(mNewPassword);
			
			mRep.setConfig(mConfig);
			mRep.saveUser(mUser);
			
			Log.e("PostExecuteTask", mUser.getLogin() + " - " + mUser.getSenha() + " - " + mUser.getPerfil().getDescricao());
			
			login(mRep.listar().get(0), ResultAction.NOT_SHOW_MSG);
//			closeActivityResultOk();
		}
	}
	
	private void closeActivityResultOk(){
		Intent it = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Util.USER_EXTRA, mUser);
		it.putExtras(bundle);
		setResult(RESULT_OK, it);
		finish();
	}
	
	private void loadModels(){
		edtLogin.setText(mUser.getLogin());
		edtPassword.setText(mUser.getSenha());
		edtServerAddress.setText(mConfig.getServerAddress());
		chkSaveLogin.setChecked(mConfig.isSaveLogin());
		if (mViewMode == CHANGE_PASSWORD_ACTIVITY_RESPONSE){
			if (mNewPassword != null)
				edtNewPassword.setText(mNewPassword);
			if (mConfirmNewPassword != null)
				edtConfirmNewPassword.setText(mConfirmNewPassword);
		}
	}
	
	private void updateModels(){
		if (mConfig == null)
			mConfig = new Config();
		if (mUser == null)
			mUser = new Usuario();
		mConfig.setSaveLogin(chkSaveLogin.isChecked());
		mConfig.setServerAddress(edtServerAddress.getText().toString());
		mUser.setLogin(edtLogin.getText().toString());
		mUser.setSenha(edtPassword.getText().toString());
		if (edtNewPassword.getText().toString().isEmpty())
			mNewPassword = null;
		else
			mNewPassword = edtNewPassword.getText().toString();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		updateModels();
		outState.putSerializable(Util.USER_EXTRA, mUser);
		outState.putSerializable(Util.CONFIG_EXTRA, mConfig);
		outState.putString(NEW_PASSWORD_EXTRA, mNewPassword);
		outState.putString(CONFIRM_NEW_PASSWORD_EXTRA, mConfirmNewPassword);
		outState.putInt(SHOW_MODE_EXTRA, mViewMode);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((v.getId() == R.id.edtLogin) || (v.getId() == R.id.edtPassword || v.getId() == R.id.edtServerAddress)
				|| v.getId() == R.id.edtNewPassword)
			updateModels();
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (((EditText) v).getImeActionId() == EditorInfo.IME_ACTION_DONE) {
			if (mViewMode == CHANGE_PASSWORD_ACTIVITY_RESPONSE)
				changePassword();
			else
				login();
		}
		return false;
	}
	
	

}
