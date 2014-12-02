package br.com.android.cmi.cmiapp;

import br.com.android.cmi.model.Clinica;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Paciente;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class AgendarConsultaActivity extends Activity {
	
	private Usuario mUsuario;
	private ViewMessage mViewMessage;
	private Paciente mPaciente;
	private Funcionario mMedico;
	private Clinica mClinica;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agendar_consulta);
		
		if (savedInstanceState != null){
			mMedico = (Funcionario)savedInstanceState.getSerializable(Util.MEDICO_EXTRA);
		}else{
			Intent it = getIntent();
			mMedico = (Funcionario)it.getSerializableExtra(Util.MEDICO_EXTRA);
		}
		
		
		
		if (Util.usuarioAtual == null)
			Log.e("onCreate", "Util.usuarioAtual == null");
		if (mMedico == null)
			Log.e("onCreate", "mMedico == null");
		if (Util.usuarioAtual.getPaciente() == null)
			Log.e("onCreate", "Util.usuarioAtual.getPaciente() == null");
		if (mMedico.getClinicas() == null)
			Log.e("onCreate", "mMedico.getClinicas() == null");
		if (mMedico.getClinicas().get(0) == null)
			Log.e("onCreate", "mMedico.getClinicas().get(0) == null");
		
		Log.e("agendaActivity", "AgendarConsultaFragment.novaInstancia...");
		
		if (savedInstanceState == null) {
			
			Log.i("agendarConsultaActivity", "instanciando fragment...");
			
			AgendarConsultaFragment fragment = AgendarConsultaFragment.novaInstancia(Util.usuarioAtual, mMedico,
							Util.usuarioAtual.getPaciente(), mMedico.getClinicas().get(0));
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragmentAgendarConsulta, fragment,
					"tagFragmentAgendarConsulta");
			fragmentTransaction.commit();
		}else{
			Log.i("agendarConsultaActivity", "fragment já instanciado.");
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.MEDICO_EXTRA, mMedico);
	}
	
	
	
}
