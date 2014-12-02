package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import android.content.Context;
import android.util.Log;
import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;

import com.google.gson.Gson;

public class WSCautenticarUsuario extends WSCommand implements OnExecuteWsCommand {
	
	public static final String SUB_URI_AUTENTICAR_USUARIO = "/usuario/auth";
	
	public static final String USUARIO_AUTENTICADO = "usuário autenticado";
	public static final String USUARIO_INVALIDO = "Usuário inválido";
	public static final String SENHA_INVALIDA = "Senha inválida";
	
	private boolean mShowSuccessfulMessage;
	
	public WSCautenticarUsuario(Context context, WebServiceConnection ws) throws InvalidParameterException {
		super(context, ws);
		mShowSuccessfulMessage = true;
	}
	
	public WSCautenticarUsuario(Context context, String uriServerRoot, WebServiceConnection ws) throws InvalidParameterException {
		this(context, uriServerRoot, ws, true);
	}
	
	public WSCautenticarUsuario(Context context, String uriServerRoot, WebServiceConnection ws, boolean showSuccessfulMessage)
			throws InvalidParameterException {
		super(context, ws);
		mUriServer = uriServerRoot;
		mShowSuccessfulMessage = showSuccessfulMessage;
	}
	
	public WSCautenticarUsuario(Context context, WebServiceConnection ws, boolean showSuccessfulMessage) throws InvalidParameterException {
		super(context, ws);
		mShowSuccessfulMessage = showSuccessfulMessage;
	}

	@Override
	public ResultAction executeWsCommand() {
		Usuario u = null;
		ResultAction result = null;
		Gson gson = null;
//		WSMessage wsMsg = null;
		
		Log.e("autenticar.execute", "ws.mUsuario.getSenha()="+ws.mUsuario.getSenha());
		
		result = execute(SUB_URI_AUTENTICAR_USUARIO, HttpMethods.GET, ws.mUsuario);
		
		Log.d(null, "result.code=="+result.getCodResult());
		
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_OK:
				gson = new Gson();
				if (ws.mJson.toLowerCase().contains("não encontrado")){
					return new ResultAction(R.string.msg_Invalid_login_or_password);
				}
				u = gson.fromJson(ws.mJson, Usuario.class);

				if (ws.mUsuario.getLogin().equals(u.getLogin())
						&& ws.mUsuario.getSenha().equals(u.getSenha())) {
					ws.mUsuario = u;
				
					Log.i("INFO", "mostrando ws.mUsuario...");
					Log.i("WSConnection...", ws.mUsuario.getLogin() + "|" + ws.mUsuario.getSenha() + "|"
							+ ws.mUsuario.getPerfil().getDescricao());
					
					Util.usuarioAtual = ws.mUsuario;
					if (mShowSuccessfulMessage)
						result = new ResultAction(R.string.connected, ws.mUsuario);
					else
						result = new ResultAction(R.string.connected, ws.mUsuario, ResultAction.NOT_SHOW_MSG);
				}
				break;
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				result = new ResultAction(R.string.msg_acess_denied);
				break;
			case HttpURLConnection.HTTP_NO_CONTENT:
				
				Log.d("atenticarusuario", "HttpURLConnection.HTTP_NO_CONTENT");
				
				result = new ResultAction(R.string.msg_Invalid_login_or_password);
				
//				gson = new Gson();
//				wsMsg = gson.fromJson(ws.mJson, WSMessage.class);
//				if (wsMsg.getCode() == WSMessage.INVALID_USER_NAME_OR_PASSWORD)
//					result = new ResultAction(R.string.msg_Invalid_login_or_password);
//				else
//					result = new ResultAction(R.string.msg_enter_stars_first);
				break;
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				result = new ResultAction(R.string.msg_server_time_out);
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:
				result = new ResultAction(R.string.msg_resource_not_found);
				break;
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				result = new ResultAction(R.string.msg_server_error);
				break;
		}
		
		Log.d(null, "result.getCodResult()=="+result.getCodResult());

		return result;
	}

}
