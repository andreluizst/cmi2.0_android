package br.com.android.cmi.cmiapp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.android.cmi.model.Atendimento;
import br.com.android.cmi.model.Consulta;

public class ConsultasAgendadasAdapter extends BaseAdapter {
	
	private List<Consulta> mLista;
	private Context mContext;
	private HashMap<Integer, Atendimento> mAtendimentoDaConsulta;
	private OnClickListener mOnClickToCancelText;
	private OnTouchListener mOnTouchToRatingBar;
	private Consulta mConsultaSelecionada;

	public ConsultasAgendadasAdapter(Context context, List<Consulta> lista, HashMap<Integer, Atendimento> atendimentoConsulta) {
		super();
		mContext = context;
		mLista = lista;
		mAtendimentoDaConsulta = atendimentoConsulta;
		mConsultaSelecionada = null;
	}
	
	public void setOnTouchListenerToRatingBar(OnTouchListener touchListener){
		mOnTouchToRatingBar = touchListener;
	}
	
	public void setOnClickListennerToCancelOption(OnClickListener listener){
		mOnClickToCancelText = listener;
	}
	
	public void setConsultaSelecionada(Consulta consulta){
		mConsultaSelecionada = consulta;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLista.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mLista.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		Consulta consulta = (Consulta)getItem(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_consultas_agendas, parent, false);
			holder = new Holder();
			holder.txtMedico = (TextView)convertView.findViewById(R.id.txtMedico_item_consultasAgendadas);
			holder.txtData = (TextView)convertView.findViewById(R.id.txtData_item_consultasAgendadas);
			holder.txtSituacao = (TextView)convertView.findViewById(R.id.txtSituacao_item_consultasAgendadas);
			holder.txtHorario = (TextView)convertView.findViewById(R.id.txtHorario_item_consultasAgendadas);
			holder.txtAux = new TextView(mContext);
			holder.txtCancelar = (TextView)convertView.findViewById(R.id.txtCancel_item_consultasAgendadas);
			holder.txtCancelar.setOnClickListener(mOnClickToCancelText);
			holder.rtbAvaliacao = (RatingBar)convertView.findViewById(R.id.rtbAvaliacao_item_consultasAgendadas);
			holder.rtbAvaliacao.setOnTouchListener(mOnTouchToRatingBar);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		holder.consulta = consulta;
		if (holder.consulta == mConsultaSelecionada){
			convertView.setBackgroundResource(R.drawable.background_gradiente_azul_selected_item);
		}else{
			convertView.setBackgroundResource(R.drawable.background_gradiente_verde_agenda);
		}
		holder.txtAux.setText(R.string.time_at);
		holder.txtMedico.setText(consulta.getFuncionario().getNome());
		holder.txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(consulta.getDataConsulta()));
		holder.txtHorario.setText(new SimpleDateFormat("HH:mm:ss").format(consulta.getDisponibilidade().getHoraInicial())
				+ " " + holder.txtAux.getText().toString() + " "
				+ new SimpleDateFormat("HH:mm:ss").format(consulta.getDisponibilidade().getHoraFinal()));
		holder.txtCancelar.setVisibility(View.GONE);
		holder.txtCancelar.setTag(position);
		holder.rtbAvaliacao.setTag(position);
		holder.rtbAvaliacao.setClickable(false);
		holder.rtbAvaliacao.setRating(0);
		holder.rtbAvaliacao.setVisibility(View.GONE);
		switch (consulta.getSituacao()){
			case 1:
				holder.txtSituacao.setText(R.string.status_scheduled);
				holder.txtCancelar.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.txtSituacao.setText(R.string.status_service_provided);
				holder.rtbAvaliacao.setVisibility(View.VISIBLE);
				if (mAtendimentoDaConsulta.get(consulta.getCodConsulta()) != null){
					int avaliacao = mAtendimentoDaConsulta.get(consulta.getCodConsulta()).getAvaliacao();
					holder.rtbAvaliacao.setRating(avaliacao);
					holder.rtbAvaliacao.setClickable(avaliacao>0?false:true);
				}else{
					holder.rtbAvaliacao.setClickable(true);
				}
				break;
			default:
				holder.txtSituacao.setText(R.string.status_schedule_canceled);
				break;
		}
		
		return convertView;
	}
	
	class Holder{
		TextView txtMedico;
		TextView txtData;
		TextView txtSituacao;
		TextView txtHorario;
		TextView txtAux;
		TextView txtCancelar;
		RatingBar rtbAvaliacao;
		Consulta consulta;
	}

}
