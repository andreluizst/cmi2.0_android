package br.com.android.cmi.cmiapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.util.Util;
import br.com.android.cmi.ws.ResultAction;
import br.com.android.cmi.ws.WSCavaliarAtendimento;
import br.com.android.cmi.ws.WSCommand;
import br.com.android.cmi.ws.WebServiceConnection;
import br.com.android.cmi.ws.task.OnPostExecuteTask;
import br.com.android.cmi.ws.task.TaskForWs;

public class AvaliarAtendimentoFragment extends Fragment implements OnEditorActionListener, OnPostExecuteTask, 
		OnClickListener, OnRatingBarChangeListener {
	
	private Atendimento mAtendimento;
	private RatingBar mRatingBar;
	private EditText mEdtComentario;
	private ViewMessage mViewMessage;
	private ProgressBar mProgressBar;
	private Button mBtnOk;
	private int mBackgroundResource;
	
	
	public interface OnAvaliarAtendimento{
		public void onConfirmarAvaliacaoAtendimento(Atendimento atendimento);
		public void onCancelarAvaliacaoAtendimento();
	}
	
	
	public static AvaliarAtendimentoFragment novaInstancia(Atendimento atendimento){
		return novaInstancia(atendimento, -1);
	}
	
	public static AvaliarAtendimentoFragment novaInstancia(Atendimento atendimento, int backgroundResource){
		Bundle bundle = new Bundle();
		AvaliarAtendimentoFragment fragment = new AvaliarAtendimentoFragment();
		bundle.putSerializable(Util.ATENDIMENTO_EM_AVALIACAO_EXTRA, atendimento);
		bundle.putInt(Util.AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA, backgroundResource);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		if (savedInstanceState != null){
			mAtendimento = (Atendimento)savedInstanceState.getSerializable(Util.ATENDIMENTO_EM_AVALIACAO_EXTRA);
			mBackgroundResource = savedInstanceState.getInt(Util.AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA);
		}else{
			mAtendimento = (Atendimento)getArguments().getSerializable(Util.ATENDIMENTO_EM_AVALIACAO_EXTRA);
			mBackgroundResource = getArguments().getInt(Util.AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA);
		}
//		if (mAtendimento == null)
//			mAtendimento = new Atendimento();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		mViewMessage = new ViewMessage(getActivity());
		view = inflater.inflate(R.layout.fragment_avaliar_atendimento, container, false);
		mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar_avaliacaoAtendimento);
		mRatingBar = (RatingBar)view.findViewById(R.id.ratingBar_avaliacaoAtendimento);
		mRatingBar.setRating(mAtendimento.getAvaliacao());
		mRatingBar.setOnRatingBarChangeListener(this);
		mEdtComentario = (EditText)view.findViewById(R.id.edtComentario_AvaliacaoAtendimento);
		mEdtComentario.setOnEditorActionListener(this);
		mBtnOk = (Button)view.findViewById(R.id.btnOk_avaliacaoAtendimento);
		mBtnOk.setOnClickListener(this);
		((Button)view.findViewById(R.id.btnCancelar_avaliacaoAtendimento)).setOnClickListener(this);
		mBtnOk.setEnabled(mAtendimento.getAvaliacao()>0?true:false);
		if (mBackgroundResource > -1)
			view.setBackgroundResource(mBackgroundResource);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAtendimento.setAvaliacao(Math.round(mRatingBar.getRating()));
		mAtendimento.setComentario(mEdtComentario.getText().toString());
		outState.putSerializable(Util.ATENDIMENTO_EM_AVALIACAO_EXTRA, mAtendimento);
		outState.putInt(Util.AVALIAR_ATENDIMENTO_BACKGROUND_EXTRA, mBackgroundResource);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v == mEdtComentario && actionId == EditorInfo.IME_ACTION_DONE){
			if (mAtendimento.getAvaliacao()>0)
				enviarAvaliacaoAtendimentoParaWebService();
			else
				Toast.makeText(getActivity(), R.string.msg_enter_stars_first, Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	private void confirmarAvaliacao(){
		if (getActivity() instanceof OnAvaliarAtendimento){
//			mAtendimento.setAvaliacao(Math.round(mRatingBar.getRating()));
//			mAtendimento.setComentario(mEdtComentario.getText().toString());
			((OnAvaliarAtendimento)getActivity()).onConfirmarAvaliacaoAtendimento(mAtendimento);
		}
	}
	
	private void enviarAvaliacaoAtendimentoParaWebService(){
		mAtendimento.setAvaliacao(Math.round(mRatingBar.getRating()));
		mAtendimento.setComentario(mEdtComentario.getText().toString());
		TaskForWs task = new TaskForWs(this, mProgressBar, mViewMessage);
		WSCommand wsCommand = new WSCavaliarAtendimento(getActivity(), 
										WebServiceConnection.getWebServiceToUser(Util.usuarioAtual), mAtendimento);
		task.execute(wsCommand);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnOk_avaliacaoAtendimento:
				enviarAvaliacaoAtendimentoParaWebService();
				break;
			case R.id.btnCancelar_avaliacaoAtendimento:
				if (getActivity() instanceof OnAvaliarAtendimento){
					((OnAvaliarAtendimento)getActivity()).onCancelarAvaliacaoAtendimento();
				}
				break;
		}
	}
	
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
//		Toast.makeText(getActivity(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
		mAtendimento.setAvaliacao(Math.round(rating));
		mBtnOk.setEnabled(mAtendimento.getAvaliacao()>0);
	}
	
	
	
	@Override
	public void postExecuteTask(ResultAction resultAction) {
		if (resultAction != null){
			if (resultAction.getCodResult() != null && resultAction.getCodResult() == R.string.msg_evaluation_performed){
				confirmarAvaliacao();
			}
		}
	}

}
