package br.com.android.cmi.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelperDBcmi extends SQLiteOpenHelper {

	private static final int VERSAO_DO_BANCO = 1;
	private static final String NOME_DO_BANCO = "SQLite_DB_CMI";

	public SQLHelperDBcmi(Context context) {
		super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table usuario(_id integer primary key, "
				+ "login text not null, "
				+ "senha text, "
				+ "idPerfil integer not null)");
		db.execSQL("create table perfil(_id integer primary key, "
				+ "descricao text)");
		db.execSQL("create table config(_id integer primary key, "
				+ "salvar_login integer, servidor text)");
		//Log.w(null, "SQLHelperEndereco.onCreate()...OK");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/*db.execSQL("create table endereco(_id integer primary key autoincrement, "
				+ "nome text not null, "
				+ "tipo_logradouro text not null, "
				+ "logradouro text not null, "
				+ "bairro text, "
				+ "cidade text not null, "
				+ "uf text not null, "
				+ "cep text not null, "
				+ "numero text, "
				+ "latitude real, "
				+ "longitude real)");
		Log.w(null, "SQLHelperEndereco.onUpgrade()...OK");*/
	}

}
