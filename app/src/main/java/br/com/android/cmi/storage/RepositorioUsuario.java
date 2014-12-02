package br.com.android.cmi.storage;

import java.util.ArrayList;
import java.util.List;

import br.com.android.cmi.model.Config;
import br.com.android.cmi.model.Perfil;
import br.com.android.cmi.model.Usuario;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RepositorioUsuario {
	private SQLHelperDBcmi helper;
	//private static boolean SALVAR_LOGIN = false;
	private static final String TABLE_CONFIG = "config";
	private static final String TABLE_CONFIG_ID = "_id";
	private static final String TABLE_CONFIG_KEEP_LOGIN = "salvar_login";
	private static final String TABLE_CONFIG_SERVER = "servidor";
	private static final String TABLE_NAME = "usuario";
	private static final String COLUNA_ID = "_id";
	private static final String COLUNA_LOGIN = "login";
	private static final String COLUNA_SENHA = "senha";
	private static final String COLUNA_IDPERFIL = "idPerfil";
	private static final String TABLE_PERFIL = "perfil";
	private static final String TABLE_PERFIL_ID = "_id";
	private static final String TABLE_PERFIL_DESCRICAO = "descricao";
	
	
	
	public RepositorioUsuario(Context context){
		helper = new SQLHelperDBcmi(context);
	}
	
	public void saveUser(Usuario usuario){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(TABLE_PERFIL, "_id > 0", null);
		db.delete(TABLE_NAME, "_id > 0", null);
		db.insert(TABLE_PERFIL, null, valuesFromPerfil(usuario.getPerfil()));
		db.insert(TABLE_NAME, null, valuesFromUsuario(usuario));
		if (usuario.getPerfil() != null && usuario.getPerfil().getDescricao() != null)
			Log.i("REPO.save", usuario.getPerfil().getDescricao());
		else
			Log.e("REPO.save", "Perfil está NULO");
		//db.insert(TABLE_PERFIL, null, valuesFromPerfil(usuario.getCodUsuario(), usuario.getPerfil()));
		//Log.i("REPO.save", "perfil = " + count);
		db.close();
	}
	
	/*public long inserir(Usuario usuario){
		SQLiteDatabase db = helper.getWritableDatabase();
		long id = db.insert(TABLE_NAME,null, valuesFromUsuario(usuario));
		if (usuario.getCodUsuario() == null)
			usuario.setCodUsuario(new Integer(String.valueOf(id)));
		if (usuario.getPerfil() == null)
			Log.e("Rep.inserir", "Perfil do usuário está nulo");
		//for (Perfil p : usuario.getPerfis()){
			db.insert(TABLE_PERFIL_USER, null, valuesFromPerfil(usuario.getCodUsuario(), usuario.getPerfil()));//p));
		//}
		db.close();
		return id;
	}
	
	public int alterar(Usuario usuario){
		SQLiteDatabase db = helper.getWritableDatabase();
		int linhasAfetadas = db.update(TABLE_NAME, valuesFromUsuario(usuario), "_id = ? ", new String[] {String.valueOf(usuario.getCodUsuario())});
		db.delete(TABLE_PERFIL_USER, RepositorioUsuario.TABLE_PERFIL_USER_ID + " > 0 and "
				+ RepositorioUsuario.TABLE_PERFIL_USER_ID + " = "+ String.valueOf(usuario.getCodUsuario()), null);
		if (usuario.getPerfil() != null){
			//for (Perfil p : usuario.getPerfis()) {
				linhasAfetadas += db.insert(TABLE_PERFIL_USER, null,valuesFromPerfil(usuario.getCodUsuario(), usuario.getPerfil()));//p));
			//}
		}else{
			Log.e("Rep.alterar", "Perfil do usuário está nulo");
		}
		db.close();
		return linhasAfetadas;
	}*/
	

	public int excluir(long id){
		SQLiteDatabase db = helper.getWritableDatabase();
		int linhasAfetadas = db.delete(TABLE_NAME, "_id = " + id, null);
		db.close();
		return linhasAfetadas;
	}
	
	public List<Usuario> listar(){
		//Log.w(null, "RepositorioEndereco.listar()...");
		List<Usuario> lista = new ArrayList<Usuario>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursorUsuario = db.rawQuery("select * from " + TABLE_NAME + " order by "+ COLUNA_LOGIN, null);
		while (cursorUsuario.moveToNext()){
			Usuario usuario = new Usuario();
			usuario = cursorToUsuario(cursorUsuario);
			Cursor cursorPerfil = db.rawQuery("select * from " + TABLE_PERFIL
					+ " where " + TABLE_PERFIL_ID + " = " 
					+ String.valueOf(cursorUsuario.getInt((cursorUsuario.getColumnIndex(COLUNA_IDPERFIL)))), null);
			while (cursorPerfil.moveToNext()){
				usuario.setPerfil(cursorToPerfil(cursorPerfil));
			}
			
			Log.w("REPO", usuario.getCodUsuario()+"|"+usuario.getLogin()+"|"+usuario.getSenha());
			if (usuario.getPerfil() != null && usuario.getPerfil().getDescricao() != null)
				Log.w("REPO", usuario.getPerfil().getDescricao());
			else
				Log.e("REPO", "Perfil est� NULO");
			
			cursorPerfil.close();
			lista.add(usuario);
		}
		cursorUsuario.close();
		db.close();
		//Log.i(null, "RepositorioEndereco.listar()...OK");
		return lista;
	}

	private ContentValues valuesFromUsuario(Usuario usuario) {
		ContentValues values = new ContentValues();
		values.put(RepositorioUsuario.COLUNA_ID, usuario.getCodUsuario());
		values.put(RepositorioUsuario.COLUNA_LOGIN, usuario.getLogin());
		values.put(COLUNA_SENHA, usuario.getSenha());
		values.put(RepositorioUsuario.COLUNA_IDPERFIL, usuario.getPerfil().getCodPerfil());
		return values;
	}
	
	private ContentValues valuesFromPerfil(Perfil p) {
		ContentValues values = new ContentValues();
		values.put(RepositorioUsuario.TABLE_PERFIL_ID, p.getCodPerfil());
		values.put(RepositorioUsuario.TABLE_PERFIL_DESCRICAO, p.getDescricao());
		return values;
	}
	
	private ContentValues valuesFromConfig(Config config) {
		ContentValues values = new ContentValues();
		values.put(RepositorioUsuario.TABLE_CONFIG_ID, config.getId());
		values.put(RepositorioUsuario.TABLE_CONFIG_SERVER, config.getServerAddress());
		values.put(RepositorioUsuario.TABLE_CONFIG_KEEP_LOGIN, config.isSaveLogin()?1:0);
		return values;
	}
	
	private Usuario cursorToUsuario(Cursor cursor){
		Usuario u = new Usuario();
		u.setCodUsuario(cursor.getInt(cursor.getColumnIndex(COLUNA_ID)));
		u.setLogin(cursor.getString(cursor.getColumnIndex(COLUNA_LOGIN)));
		u.setSenha(cursor.getString(cursor.getColumnIndex(COLUNA_SENHA)));
		return u;
	}
	
	private Config cursorToConfig(Cursor cursor){
		return new Config(
				cursor.getInt(cursor.getColumnIndex(TABLE_CONFIG_ID)),
				cursor.getString(cursor.getColumnIndex(TABLE_CONFIG_SERVER)),
				cursor.getInt(cursor.getColumnIndex(TABLE_CONFIG_KEEP_LOGIN))==1?true:false
				);
	}
	
	private Perfil cursorToPerfil(Cursor cursor){
		Perfil p = new Perfil();
		p.setCodPerfil(cursor.getInt(cursor.getColumnIndex(TABLE_PERFIL_ID)));
		p.setDescricao(cursor.getString(cursor.getColumnIndex(TABLE_PERFIL_DESCRICAO)));
		return p;
	}

	/*public List<Usuario> consultarPorNome(String mFilter) {
		List<Usuario> lista = new ArrayList<Usuario>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUNA_NOME + " like ? order by " + COLUNA_NOME, 
				new String[] {"%"+mFilter+"%"});
		while (cursor.moveToNext()){
			lista.add(cursorToUsuario(cursor));
		}
		cursor.close();
		db.close();
		return lista;
	}*/
	
	public List<Usuario> consultarPorLogin(String mFilter) {
		List<Usuario> lista = new ArrayList<Usuario>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUNA_LOGIN + " like ? order by " + COLUNA_LOGIN, 
				new String[] {"%"+mFilter+"%"});
		while (cursor.moveToNext()){
			lista.add(cursorToUsuario(cursor));
		}
		cursor.close();
		db.close();
		return lista;
	}

	public boolean hasSavedLogin() {
		//List<Usuario> lista = listar();
		Config cfg = getConfig();
		return listar().size()>0 && cfg.isSaveLogin()?true:false;
	}
	
	public boolean isAllrightWithLogin(){
		List<Usuario> lista = listar();
		for (Usuario usuario : lista){
			if ( usuario.getSenha() == null || usuario.getSenha().isEmpty())
				return false;
		}
		return lista.size()>0?true:false;
	}
	
	public boolean isConfigToSave(){
		boolean isConfig = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_CONFIG, null);
		while (cursor.moveToNext()){
			isConfig = cursor.getInt((cursor.getColumnIndex(TABLE_CONFIG_KEEP_LOGIN))) == 1?true:false;
		}
		cursor.close();
		db.close();
		return isConfig;
	}
	
	public void setConfigToSave(Config config){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(RepositorioUsuario.TABLE_CONFIG, "_id >= 0", null);
		ContentValues values = new ContentValues();
		values.put(RepositorioUsuario.TABLE_CONFIG_KEEP_LOGIN, config.isSaveLogin()?1:0);
		db.insert(TABLE_CONFIG,null, values);
		db.close();
	}
	
	public void setConfig(Config config){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(RepositorioUsuario.TABLE_CONFIG, "_id >= 0", null);
		config.setId(1);
		db.insert(TABLE_CONFIG, null, valuesFromConfig(config));
		db.close();
	}
	
	public Config getConfig(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Config config = null;
		Cursor cursor = db.rawQuery("select * from " + TABLE_CONFIG, null);
		while (cursor.moveToNext()){
			config = cursorToConfig(cursor);
		}
		db.close();
		return config;
	}
}
