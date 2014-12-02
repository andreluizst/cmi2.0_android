package br.com.android.cmi.cmiapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListView;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.model.Consulta;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;

public class ConsultasAgendadasFragment extends Fragment implements OnClickListener, OnTouchListener {
	
	private Usuario mUsuario;
	private ArrayList<Consulta> mConsultas;
	private HashMap<Integer, Atendimento> mAtendimentoDaConsulta;
	private ConsultasAgendadasAdapter mAdapter;
	private ListView mListView;
	
	
	public interface OnCancelarAgendamentoClick{
		public void onCancelarAgendamentoClick(Consulta consulta);
	}
	
	public interface OnAvaliarAtendimentoDaConsultaClick{
		public void onAvaliarAtendimentoDaConsultaClick(Atendimento atendimento);
	}
	
	
	public static ConsultasAgendadasFragment novaInstancia(Usuario usuario, ArrayList<Consulta> lista, 
			HashMap<Integer, Atendimento> atendimentosConsulta){
		ConsultasAgendadasFragment novoFragment = new ConsultasAgendadasFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Util.USER_EXTRA, usuario);
		bundle.putSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA, lista);
		bundle.putSerializable(Util.MAPA_ATENDIMENTOS_EXTRA, atendimentosConsulta);
		novoFragment.setArguments(bundle);
		return novoFragment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (savedInstanceState != null){
			mUsuario = (Usuario)savedInstanceState.getSerializable(Util.USER_EXTRA);
			mConsultas = (ArrayList<Consulta>)savedInstanceState.getSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA);
			mAtendimentoDaConsulta = (HashMap<Integer, Atendimento>)savedInstanceState.getSerializable(Util.MAPA_ATENDIMENTOS_EXTRA);
		}else{
			mUsuario = (Usuario)getArguments().getSerializable(Util.USER_EXTRA);
			mConsultas = (ArrayList<Consulta>)getArguments().getSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA);
			mAtendimentoDaConsulta = (HashMap<Integer, Atendimento>)getArguments().getSerializable(Util.MAPA_ATENDIMENTOS_EXTRA);
		}
		mAdapter = new ConsultasAgendadasAdapter(getActivity(), mConsultas, mAtendimentoDaConsulta);
		mAdapter.setOnClickListennerToCancelOption((OnClickListener)this);
		mAdapter.setOnTouchListenerToRatingBar((OnTouchListener)this);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(Util.USER_EXTRA, mUsuario);
		outState.putSerializable(Util.LISTA_CONSULTAS_AGENDADAS_EXTRA, mConsultas);
		outState.putSerializable(Util.MAPA_ATENDIMENTOS_EXTRA, mAtendimentoDaConsulta);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_consultas_agendadas, container, false);
		mListView = (ListView)view.findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setClickable(false);
		return view;
	}

	
	@Override
	public void onClick(View v) {
		Integer position;
		switch(v.getId()){
			case R.id.txtCancel_item_consultasAgendadas:
				position = (Integer)v.getTag();
				Consulta consulta = mConsultas.get(position);
				if (getActivity() instanceof OnCancelarAgendamentoClick)
					((OnCancelarAgendamentoClick)getActivity()).onCancelarAgendamentoClick(consulta);
				break;
//			case R.id.rtbAvaliacao_item_consultasAgendadas:
//				
//				Log.i(null, "clicando em rtbAvaliacao_item_consultasAgendadas...");
//				
//				position = (Integer)v.getTag();
//				Consulta consultaAvaliacao = mConsultas.get(position);
//				if (getActivity() instanceof OnAvaliarAtendimentoDaConsultaClick)
//					((OnAvaliarAtendimentoDaConsultaClick)getActivity()).onAvaliarAtendimentoDaConsultaClick(consultaAvaliacao);
//				break;
		}
	}
	
	public void refreshItemList(Consulta consulta){
		mAdapter.notifyDataSetChanged();
	}
	
	public void refreshItemList(Atendimento atendimento){
		mAtendimentoDaConsulta.put(atendimento.getConsulta().getCodConsulta(), atendimento);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.rtbAvaliacao_item_consultasAgendadas && v.isClickable()){
			if (event.getAction() == MotionEvent.ACTION_UP){
				Integer position;
			
				Log.i("onTouch", "clicando em rtbAvaliacao_item_consultasAgendadas...");
			
				position = (Integer)v.getTag();
				Consulta consultaAvaliacao = mConsultas.get(position);
				if (getActivity() instanceof OnAvaliarAtendimentoDaConsultaClick){
					Atendimento atendimento = mAtendimentoDaConsulta.get(consultaAvaliacao);
					if (atendimento == null){
						atendimento = new Atendimento();
						atendimento.setConsulta(consultaAvaliacao);
					}
					selecionarConsulta(consultaAvaliacao);
					((OnAvaliarAtendimentoDaConsultaClick)getActivity())
								.onAvaliarAtendimentoDaConsultaClick(atendimento);
				}
			}
		}
		return true;
	}
	
	public void selecionarConsulta(Consulta consulta){
		mAdapter.setConsultaSelecionada(consulta);
		mAdapter.notifyDataSetChanged();
	}

	
}
