package br.com.android.cmi.ws;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.util.Log;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.network.NetConnection;
import br.com.android.cmi.network.NoConnectionException;

import com.google.gson.Gson;


public class WebServiceConnection {
	
	static enum HttpMethods{POST, GET, PUT, DELETE, HEAD};
	//projetocmi-web
	//meu web service de teste = webserviceteste
	public static final String URI_CMI = "http://192.168.56.1:8080/projetocmi-web/servicos";
	public static final String SUB_URI_LISTAR_USUARIOS = "/usuario";
	public static final String SUB_URI_USUARIO_BY_ID = "/usuario/{id}";
	public static final String SUB_URI_AUTENTICAR_USUARIO = "/usuario/auth";
	
	protected Usuario mUsuario;
	protected String mJson;
	protected NetConnection netConnection;
	protected DataOutputStream mDataOutputStream;

	
//	private String lineEnd = "\r\n";
//	private String twoHyphens = "--";
//	private String boundary = "*****";
	
	
	private WebServiceConnection(){
		
	}
	
	public static WebServiceConnection getWebServiceToUser(Usuario usuario){
		WebServiceConnection ws = new WebServiceConnection(); 
		ws.mUsuario = usuario;
		//Log.i(null, "WebServiceConnection.getWebServiceToUser(..)....");
		return  ws;
	}
	
	protected HttpURLConnection open(String url, HttpMethods httpMethod, Usuario usuario) throws IOException, NoConnectionException, ConnectException {
		//Log.i(null, "WebServiceConnection.open(..)....");
		URL urlToConnect = new URL(url);
		HttpURLConnection httpUrlConnection = (HttpURLConnection)urlToConnect.openConnection();
		httpUrlConnection.setRequestMethod(httpMethod.toString());
		httpUrlConnection.setDoInput(true);
		if (httpMethod == HttpMethods.POST || httpMethod == HttpMethods.PUT){
			
			//Log.i("WebServiceConnection", "httpMethod=" + httpMethod);
			
			//Log.e("WebServiceConnection", mUsuario.getLogin() + " | " + mUsuario.getSenha() + " | " + mUsuario.getPerfil().getDescricao());
			
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("ENCTYPE", "application/json");//"multipart/form-data");
			httpUrlConnection.setRequestProperty("Content-Type", "application/json");//"multipart/form-data;boundary=" + boundary);
		}
		httpUrlConnection.setConnectTimeout(10000);
		httpUrlConnection.setRequestProperty("user-agent", usuario.getLogin() + ";" + usuario.getSenha());
		if (netConnection != null){
			if (!netConnection.exists())
				throw new NoConnectionException();
		}
		//Log.i(null, "WebServiceConnection.open(..)...httpUrlConnection.connect()....");
		try{
			httpUrlConnection.connect();
		}catch(Exception e){
			e.printStackTrace();
			throw new ConnectException(e.getMessage());
		}
		//Log.i(null, "WebServiceConnection.open(..)...httpUrlConnection.connect()..Executado");
		return httpUrlConnection;
	}
	
	protected static String bytesToString(InputStream is) throws IOException{
		//Log.i(null, "WebServiceConnection.bytesToString(..)....");
		byte bytes[] = new byte[1024];
		int bytesLidos;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((bytesLidos = is.read(bytes)) != -1){
			baos.write(bytes, 0, bytesLidos);
		}
		//Log.d("bytesToString", new String(baos.toByteArray()));
		return new String(baos.toByteArray());
		//return inputStreamToString(is);
	}
	
	protected void writeJsonToOutpuStream(String json, HttpURLConnection httpUrlConnection) throws IOException{
		
		//Log.i("WebServiceConnection", "writeJsonToOutpuStream...");
		
		mDataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
		mDataOutputStream.writeBytes(json);
		mDataOutputStream.flush();
		mDataOutputStream.close();
	}
	
	
	public Usuario consultarUsuarioPorId(int codigo) throws IOException, NoConnectionException{
		HttpURLConnection connection;
		connection = open(URI_CMI + SUB_URI_USUARIO_BY_ID.replace("{id}", String.valueOf(mUsuario.getCodUsuario())), HttpMethods.GET, mUsuario);
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
			Gson gson = new Gson();
			return gson.fromJson(bytesToString(connection.getInputStream()), Usuario.class);
		}
		return null;
	}
	
//	private static String inputStreamToString(InputStream is) {
//		BufferedReader br = null;
//		StringBuilder sb = new StringBuilder();
//		String line;
//		try {
//			br = new BufferedReader(new InputStreamReader(is));
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//				//Log.d("inputStreamToString", sb.toString());
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		//Log.d("inputStreamToString", sb.toString());
//		return sb.toString();
//	}
	
}
