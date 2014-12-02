package br.com.android.cmi.cmiapp;

import br.com.android.cmi.model.Usuario;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	EditText edtLogin;
	EditText edtSenha;
	Usuario mUsuario;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("INFO", "Inflando fragmanet_login.xml");
		View layout = inflater.inflate(R.layout.fragment_login, null);
		edtLogin = (EditText)layout.findViewById(R.id.edtLogin);
		edtSenha = (EditText)layout.findViewById(R.id.edtPassword);
		return layout;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
}
