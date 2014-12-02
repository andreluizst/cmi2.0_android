package br.com.android.cmi.ws;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import com.google.gson.Gson;

import br.com.android.cmi.cmiapp.R;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.ws.WebServiceConnection.HttpMethods;
import android.content.Context;

public class WSCalterarSenha extends WSCommand implements OnExecuteWsCommand {
	
	
	private static final String SUB_URI_ALTERAR_SENHA = "/usuario/alterarsenha";
	
	private Usuario mUsuarioComNovaSenha;

	public WSCalterarSenha(Context context, WebServiceConnection ws, Usuario usuarioComNovaSenha) throws InvalidParameterException {
		super(context, ws);
		mUsuarioComNovaSenha = usuarioComNovaSenha;
	}

	@Override
	public ResultAction executeWsCommand() {
		ResultAction result;
		Gson gson = new Gson();
		WSMessage wsMsg;
		ws.mJson = gson.toJson(mUsuarioComNovaSenha);
		result = execute(SUB_URI_ALTERAR_SENHA, HttpMethods.POST, ws.mUsuario);
		switch (result.getCodResult()){
			case HttpURLConnection.HTTP_OK:
				wsMsg = gson.fromJson(ws.mJson, WSMessage.class);
				if (wsMsg.getCode() == WSMessage.OK)
					return new ResultAction(R.string.msg_successfully_changed_password);
				if (wsMsg.getCode() == WSMessage.USER_PASSWORD_MUST_CONTAIN_5_TO_15_CARACTERS)
					return new ResultAction(R.string.msg_user_password_must_be_5_to_15_characters);
				else
					return new ResultAction(R.string.business_roles_exception);
			case HttpURLConnection.HTTP_NOT_FOUND:
				return new ResultAction(R.string.msg_resource_not_found);
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				return new ResultAction(R.string.msg_acess_denied);
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				return new ResultAction(R.string.msg_server_time_out);
			case HttpURLConnection.HTTP_INTERNAL_ERROR:
				return new ResultAction(R.string.msg_server_error);
			case HttpURLConnection.HTTP_NO_CONTENT:
				return new ResultAction(R.string.msg_no_content);
		}
		return new ResultAction(result.getCodResult());
	}

}
