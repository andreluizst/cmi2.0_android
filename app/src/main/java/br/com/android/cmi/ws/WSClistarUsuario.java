package br.com.android.cmi.ws;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.network.NoConnectionException;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WSClistarUsuario extends WSCommand {
	
	
	public WSClistarUsuario(Context context, WebServiceConnection ws) throws InvalidParameterException {
		super(context, ws);
		// TODO Auto-generated constructor stub
	}


	public List<Usuario> listarUsuarios() throws IOException, NoConnectionException {
		List<Usuario> lista = new ArrayList<Usuario>();
		HttpURLConnection connection;
		connection = ws.open(WebServiceConnection.URI_CMI + WebServiceConnection.SUB_URI_LISTAR_USUARIOS,
				HttpMethods.GET, ws.mUsuario);
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			Gson gson = new Gson();
			lista = gson.fromJson(WebServiceConnection.bytesToString(connection.getInputStream()),
					new TypeToken<List<Usuario>>() {
					}.getType());
		}
		return lista;
	}
}
