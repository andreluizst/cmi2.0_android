package br.com.android.cmi.cmiapp;

import java.util.List;

import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import br.com.android.cmi.model.Clinica;
import br.com.android.cmi.model.Funcionario;

public class MedicosExpandableListAdapter extends BaseExpandableListAdapter {
	
	private List<Funcionario> mLista;
	private Context mContext;
	
	
	public MedicosExpandableListAdapter(Context context, List<Funcionario> lista){
		mContext = context;
		mLista = lista;
		
//		Log.i("ExpAdapter", "executando o construtor...");
	}
	
	
	@Override
	public int getGroupCount() {
		return mLista.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mLista.get(groupPosition).getClinicas().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mLista.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mLista.get(groupPosition).getClinicas().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder holder;
		Funcionario funcionario = mLista.get(groupPosition);
//		for (Clinica c : funcionario.getClinicas()){
//			Log.i("getGroupView", funcionario.getNome());
//			Log.i("getGroupView", c.toString());
//		}
		if (convertView != null && convertView.getTag() instanceof GroupHolder){
			holder = (GroupHolder)convertView.getTag();
		}else{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_medico, parent, false);
			holder = new GroupHolder();
			holder.txtMedico = (TextView)convertView.findViewById(R.id.txtNomeMedicoItemList);
			//holder.txtMedico.setTypeface(null, Typeface.BOLD);
			holder.txtCRM = (TextView)convertView.findViewById(R.id.txtCRMitemList);
			//holder.txtCRM.setTypeface(null, Typeface.BOLD);
			convertView.setTag(holder);
		}
		holder.txtMedico.setText(funcionario.getNome());
		holder.txtCRM.setText(funcionario.getRegistroConselho());
		
		((ExpandableListView)parent).expandGroup(groupPosition);
		
		//Log.i("ExpAdapter.getGroupView", funcionario.getNome());
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ItemHolder holder;
		Clinica clinica = (Clinica)getChild(groupPosition, childPosition);
		String especialidades = null;
//		Log.i("ExpAdapter", "getChildView...");
//		Log.i("getChildView", clinica.toString());
		if (convertView != null && convertView.getTag() instanceof ItemHolder){
			holder = (ItemHolder)convertView.getTag();
		}else{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_clinica, parent, false);
			holder = new ItemHolder();
			holder.txtClinica = (TextView)convertView.findViewById(R.id.txtItemListClinicas_nome);
			holder.txtEspecialidades = (TextView)convertView.findViewById(R.id.txtItemListClinicas_especialidades);
			convertView.setTag(holder);
		}
		holder.txtClinica.setText(clinica.toString());
		if (clinica.getEspecialidades() != null){
			for (int i = 0;i < clinica.getEspecialidades().size(); i++){
				if (i > 0)
					especialidades += ", " + clinica.getEspecialidades().get(i).getDescricao();
				else
					especialidades = clinica.getEspecialidades().get(i).getDescricao();
			}
			if (especialidades != null){
				especialidades += ".";
				holder.txtEspecialidades.setText(especialidades);
			}
		}
		holder.txtEspecialidades.setVisibility(especialidades==null?View.GONE:View.VISIBLE);
		
//		Log.i("ExpAdapter.getGroupView", clinica.toString());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	class GroupHolder{
		TextView txtMedico;
		TextView txtCRM;
	}
	
	class ItemHolder{
		TextView txtClinica;
		TextView txtEspecialidades;
	}

}
