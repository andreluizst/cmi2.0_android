package br.com.android.cmi.cmiapp;

import java.util.ArrayList;

import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.util.Util;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ListaMedicosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_medicos);
		
		@SuppressWarnings("unchecked")
		ArrayList<Funcionario> funcionarios = (ArrayList<Funcionario>)getIntent().getSerializableExtra(Util.LISTA_FUNCIONARIOS);
		
		Log.i("ListaMedicosActivity", funcionarios.get(0).toString());
		if (savedInstanceState == null) {
			MedicoListFragment fragment = MedicoListFragment
					.novaInstancia(funcionarios);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fragmentListaMedicos, fragment, "tagMedicoListFragment");
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista__medicos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
