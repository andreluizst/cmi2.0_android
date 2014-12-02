package br.com.android.cmi.cmiapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.android.cmi.model.Agenda;
import br.com.android.cmi.model.Disponibilidade;
import br.com.android.cmi.model.Mes;

public class AgendaAdapter extends BaseAdapter {
	
	static final int COR_FONTE_DIA_DISPONIVEL = Color.BLACK;
	static final int COR_FONTE_DIA_COM_CONSULTA = Color.WHITE;
	static final int COR_FONTE_DIA_FORA_DO_MES = Color.LTGRAY;
	static final int COR_FONTE_DIA_NORMAL = Color.BLUE;
	static final int COR_FONTE_DIA_SELECIONADO = Color.WHITE;
	static final int FUNDO_DIA_DISPONIVEL = R.drawable.background_gradiente_verde;
	static final int FUNDO_DIA_FORA_DO_MES = Color.GRAY;
	static final int FUNDO_DIA_COM_CONSULTA = R.drawable.background_gradiente_azul;
	static final int FUNDO_DIA_NORMAL = R.drawable.background_gradiente_prata;
	
	private Context mContext;
	private GregorianCalendar mMesSelecionado;
	private GregorianCalendar mMesAnterior;
	private List<Disponibilidade> mDisponibilidades;
	private List<Agenda> mListaAgenda;
	
	public GregorianCalendar pmonthmaxset;
	private int firstDay;
	private int maxWeeknumber;
	private int maxP;
	private int calMaxP;
	//private int lastWeekDay;
	//private int leftDays;
	private int mnthlength;
	private String itemvalue;
	//private String curentDateString;
	private DateFormat df;

	private ArrayList<String> items;
	public static List<String> dayString;
	
	private View lastViewClicked;

	
	public AgendaAdapter(Context context, Mes mes, List<Agenda> listaAgenda){
		this(context, null, mes, listaAgenda);
	}
	
	public AgendaAdapter(Context context, List<Disponibilidade> lista, Mes mes){
		this(context, lista, mes, null);
	}
	
	private AgendaAdapter(Context context, List<Disponibilidade> lista, Mes mes, List<Agenda> listaAgenda){
		mContext = context;
		mDisponibilidades = lista;
		mMesSelecionado = new GregorianCalendar();
		mMesSelecionado.set(mes.getAno(), mes.getId(), 1);
		mListaAgenda = listaAgenda;
		dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		//selectedDate = (GregorianCalendar) mMesSelecionado.clone();
		mMesSelecionado.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		//curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}
	
	public void setMesAtual(Mes mes){
		mMesSelecionado.set(mes.getAno(), mes.getId(), 1);
		refreshDays();
	}
	
	
	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	@Override
	public int getCount() {
		return dayString.size();//mAgenda.size();
	}

	@Override
	public Object getItem(int position) {
		return dayString.get(position);//mAgenda.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//Log.i("AgendaAdapter", "getView()....");
		
		DayHolder holder;
		Integer day;
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dia_da_agenda, parent, false);
			holder = new DayHolder();
			holder.txtDay = (TextView)convertView.findViewById(R.id.txtDia_agenda);
			convertView.setTag(holder);
		}else{
			holder = (DayHolder)convertView.getTag();
		}
		
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridValue = separatedTime[2].replaceFirst("^0*", "");
		// checking whether the day is in current month or not.
		holder.txtDay.setText(gridValue);
		day = Integer.parseInt(holder.txtDay.getText().toString());
		holder.txtDay.setClickable(true);
		holder.txtDay.setFocusable(true);
		convertView.setClickable(true);
		convertView.setFocusable(true);
		holder.agenda = pegarAgendaDoDia(gridValue);
		if ((day > 25) && (position < 7)) {
			convertView.setBackgroundColor(FUNDO_DIA_FORA_DO_MES);
			holder.txtDay.setTextColor(COR_FONTE_DIA_FORA_DO_MES);
		} else if ((day < 7) && (position > 28)) {
			convertView.setBackgroundColor(FUNDO_DIA_FORA_DO_MES);
			holder.txtDay.setTextColor(COR_FONTE_DIA_FORA_DO_MES);
		} else {
			holder.agenda = pegarAgendaDoDia(holder.txtDay.getText().toString());
			boolean temDisponibilidade = false;
			//temDisponibilidade = mListaAgenda!=null && mListaAgenda.size()>0?holder!=null?holder.agenda.isExisteVaga():false:
				//	mDisponibilidades!=null?temDisponibilidade(String.valueOf(position), gridValue):false;
			if (mListaAgenda != null && holder.agenda != null)
				temDisponibilidade = holder.agenda.isExisteVaga();
			else
				if (mDisponibilidades != null && mDisponibilidades.size() > 0)
					temDisponibilidade = temDisponibilidade(String.valueOf(position), gridValue);
			if (temDisponibilidade) {
				convertView.setBackgroundResource(FUNDO_DIA_DISPONIVEL);
				if (holder.agenda.getCursor().equals("pointer_selected")){
					holder.txtDay.setTextColor(COR_FONTE_DIA_SELECIONADO);
					if (lastViewClicked == null)
						lastViewClicked = convertView;
				}
				else
					holder.txtDay.setTextColor(COR_FONTE_DIA_DISPONIVEL);
				holder.txtDay.setClickable(false);
				holder.txtDay.setFocusable(false);
				convertView.setClickable(false);
				convertView.setFocusable(false);
			} else {
				convertView.setBackgroundResource(FUNDO_DIA_NORMAL);
				holder.txtDay.setTextColor(COR_FONTE_DIA_NORMAL);
			}
			if (holder.agenda != null && holder.agenda.getCursor().toLowerCase().equals("consulta_marcada")){
				convertView.setBackgroundResource(FUNDO_DIA_COM_CONSULTA);
				holder.txtDay.setTextColor(COR_FONTE_DIA_COM_CONSULTA);
			}
		}
		return convertView;
	}
	
	private boolean temDisponibilidade(String diaSemana, String diaMes){
		Integer iDiaAtual = GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_MONTH);
		GregorianCalendar mesAtual = new GregorianCalendar();
		GregorianCalendar calendario = new GregorianCalendar();
		Integer imes = mMesSelecionado.get(GregorianCalendar.MONTH);
		Integer iano = mMesSelecionado.get(GregorianCalendar.YEAR);
		calendario.set(iano, imes, Integer.parseInt(diaMes));
		Integer idiaSemana = calendario.get(GregorianCalendar.DAY_OF_WEEK);
		if (mDisponibilidades == null)
			return false;
		if (mDisponibilidades.size() == 0)
			return false;
		
		Log.d("temDisponibilidade", "diaSemana="+idiaSemana + " // diaMes="+iDiaAtual);
		
		Log.d("temDisponibilidade", "verificando disponibilidades...");
		for (int i = 0;i< mDisponibilidades.size(); i++){
			
//			Log.d(null, mDisponibilidades.get(i).getDiaSemana().getValor() + "==" + String.valueOf(idiaSemana));
//			Log.d("temDisponibilidade", "diponibilidade.getDiaSemana()=" + mDisponibilidades.get(i).getDiaSemana());
//			Log.d("temDisponibilidade", "diaSemana=" + diaSemana + " // idiaSemana=" + idiaSemana);
			
			if (mDisponibilidades.get(i).getDiaSemana().getValor()+1 == idiaSemana){
					if (Integer.parseInt(diaMes) < iDiaAtual && mesAtual.get(GregorianCalendar.MONTH) 
							== mMesSelecionado.get(GregorianCalendar.MONTH))
						return false;
					else
						return true;
			}
		}
		return false;
	}
	

	public void refreshDays() {
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		mMesAnterior = (GregorianCalendar)mMesSelecionado.clone();
		// month start day. ie; sun, mon, etc
		firstDay = mMesSelecionado.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = mMesSelecionado.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar)mMesAnterior.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (mMesSelecionado.get(GregorianCalendar.MONTH) == mMesSelecionado
				.getActualMinimum(GregorianCalendar.MONTH)) {
			mMesAnterior.set((mMesSelecionado.get(GregorianCalendar.YEAR) - 1),
					mMesSelecionado.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			mMesAnterior.set(GregorianCalendar.MONTH,
					mMesSelecionado.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = mMesAnterior.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}
	
	
	private Agenda pegarAgendaDoDia(String diaMes){
		
//		Log.d("agendaAdapter", "pegarAgendaDoDia()...");
		
		if (mListaAgenda != null){
			for (int i=0;i<mListaAgenda.size();i++){
				
//				Log.e("pegarAgendaDoDia", "mListaAgenda.get(i).getDia()="+mListaAgenda.get(i).getDia());
//				Log.e("pegarAgendaDoDia", "parametro diaMes="+diaMes);
//				Log.e("pegarAgendaDoDia", "temDisp="+mListaAgenda.get(i).isExisteVaga());
				
				if (Integer.parseInt(mListaAgenda.get(i).getDia()) == Integer.parseInt(diaMes)){
					return mListaAgenda.get(i);
				}
			}
		}
		return null;
	}
	
	void setSelectedView(View view){
		if (lastViewClicked != view) {
			if (view.getTag() instanceof DayHolder) {
				view.setSelected(true);
				((DayHolder) view.getTag()).txtDay.setTextColor(COR_FONTE_DIA_SELECIONADO);
				((DayHolder) view.getTag()).agenda.setCursor("pointer_selected");
				if (lastViewClicked != null) {
					if (!((DayHolder) lastViewClicked.getTag()).agenda.getCursor().equals("consulta_marcada")){
						((DayHolder) lastViewClicked.getTag()).txtDay.setTextColor(COR_FONTE_DIA_DISPONIVEL);
						((DayHolder) lastViewClicked.getTag()).agenda.setCursor("pointer");
						lastViewClicked.setSelected(false);
					}
				}
				lastViewClicked = view;
			}
		}
	}
	
	void unselectView(){
		if (lastViewClicked != null && lastViewClicked.getTag() instanceof DayHolder){
			((DayHolder)lastViewClicked.getTag()).txtDay.setTextColor(COR_FONTE_DIA_DISPONIVEL);
			((DayHolder)lastViewClicked.getTag()).agenda.setCursor("pointer");
			lastViewClicked.setSelected(false);
			lastViewClicked = null;
		}
	}
	
	View getSelectedView(){
		return lastViewClicked;
	}
	
	
	class DayHolder{
		TextView txtDay;
		Agenda agenda;
	}

}
