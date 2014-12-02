package br.com.android.cmi.cmiapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import br.com.android.cmi.cmiapp.AvaliarAtendimentoFragment.OnAvaliarAtendimento;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.util.Util;

public class AvaliarAtendimentoActivity extends Activity implements OnAvaliarAtendimento {
	
	
	private Atendimento mAtendimento, mAtendimentoParaAvaliacao;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avaliar_atendimento);
		
		if (savedInstanceState == null){
			Intent it = getIntent();
			mAtendimento = (Atendimento)it.getSerializableExtra(Util.ATENDIMENTO_EXTRA);
			mAtendimentoParaAvaliacao = new Atendimento();
			mAtendimentoParaAvaliacao.setCodAtendimento(mAtendimento.getCodAtendimento());
			mAtendimentoParaAvaliacao.setConsulta(mAtendimento.getConsulta());
			AvaliarAtendimentoFragment fragment = AvaliarAtendimentoFragment.novaInstancia(mAtendimentoParaAvaliacao);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fragmentAvaliacaoDoAtendimento, fragment, Util.TAG_AVALIACAO_DO_ATENDIMENTO);
			ft.commit();
		}else{
			mAtendimento = (Atendimento)savedInstanceState.getSerializable(Util.ATENDIMENTO_EXTRA);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.ATENDIMENTO_EXTRA, mAtendimento);
	}

	
	
	@Override
	public void onConfirmarAvaliacaoAtendimento(Atendimento atendimento) {
		mAtendimentoParaAvaliacao = atendimento;
		//enviarAvaliacaoAtendimentoParaWebService();
		Intent it = new Intent();
		Bundle bundle = new Bundle();
		mAtendimento.setAvaliacao(mAtendimentoParaAvaliacao.getAvaliacao());
		mAtendimento.setComentario(mAtendimentoParaAvaliacao.getComentario());
		bundle.putSerializable(Util.ATENDIMENTO_EXTRA, mAtendimento);
		it.putExtras(bundle);
		setResult(RESULT_OK, it);
		finish();
	}
	
	@Override
	public void onCancelarAvaliacaoAtendimento() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	
	
//	private void enviarAvaliacaoAtendimentoParaWebService(){
//		TaskForWs task = new TaskForWs(this, mViewMessage);
//		WSCommand wsCommand = new WSCavaliarAtendimento(this, WebServiceConnection.getWebServiceToUser(Util.usuarioAtual), mAtendimentoParaAvaliacao);
//		task.execute(wsCommand);
//	}
//	
//	
//
//	@Override
//	public void postExecuteTask(ResultAction resultAction) {
//		if (resultAction != null){
//			if (resultAction.getCodResult() != null && resultAction.getCodResult() == R.string.msg_evaluation_performed){
//				Intent it = new Intent();
//				Bundle bundle = new Bundle();
//				mAtendimento.setAvaliacao(mAtendimentoParaAvaliacao.getAvaliacao());
//				mAtendimento.setComentario(mAtendimentoParaAvaliacao.getComentario());
//				bundle.putSerializable(Util.ATENDIMENTO_EXTRA, mAtendimento);
//				it.putExtras(bundle);
//				setResult(RESULT_OK, it);
//				finish();
//			}
//		}
//	}

	
}
