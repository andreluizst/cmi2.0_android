package br.com.android.cmi.cmiapp;

import java.util.List;

import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Especialidade;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MedicoAdapter extends ArrayAdapter<Funcionario> {

	
	public MedicoAdapter(Context context, List<Funcionario> lista) {
		super(context, 0, lista);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Funcionario funcionario = getItem(position);
		String especialidades = null;
		String clinicas = null;
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_medico, parent, false);
			holder = new ViewHolder();
			holder.txtNomeMedico = (TextView)convertView.findViewById(R.id.txtNomeMedicoItemList);
			holder.txtCRM = (TextView)convertView.findViewById(R.id.txtCRMitemList);
			holder.txtEspecialidades = (TextView)convertView.findViewById(R.id.txtEspecialidadesItemList);
			holder.txtClinicas = (TextView)convertView.findViewById(R.id.txtClinicasItemList);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.txtNomeMedico.setText(funcionario.getNome());
		holder.txtCRM.setText(funcionario.getRegistroConselho());
//		for (int i = 0;i < funcionario.getEspecialidades().size(); i++){
//			if (i > 0)
//				especialidades += ", " + funcionario.getEspecialidades().get(i).getDescricao();
//			else
//				especialidades = funcionario.getEspecialidades().get(i).getDescricao();
//		}
//		if (especialidades != null)
//			especialidades+= ".";
		holder.txtEspecialidades.setText(especialidades);
		holder.txtEspecialidades.setVisibility(especialidades==null?View.GONE:View.VISIBLE);
//		for (int i = 0;i < funcionario.getClinicas().size(); i++){
//			if (i > 0)
//				clinicas += "\n" + funcionario.getClinicas().get(i).toString();
//			else
//				clinicas = funcionario.getClinicas().get(i).toString();
//		}
		for (int i = 0;i < funcionario.getClinicas().size();i++){
			if (i > 0)
				clinicas += "\n";
			clinicas = funcionario.getClinicas().get(i).toString();
			if (funcionario.getClinicas().get(i).getEspecialidades() != null) {
				for (int j = 0; j < funcionario.getClinicas().get(i).getEspecialidades().size(); j++) {
					if (j > 0)
						especialidades += ", ";
					especialidades += funcionario.getClinicas().get(i).getEspecialidades().get(j).getDescricao();
				}
			}
			if (especialidades != null)
				especialidades = "\t" + especialidades + ".";
			clinicas += "\n" + especialidades;
		}
		holder.txtClinicas.setText(clinicas);
		holder.txtClinicas.setVisibility(clinicas==null?View.GONE:View.VISIBLE);
		return convertView;
	}
	
	static class ViewHolder{
		TextView txtNomeMedico;
		TextView txtCRM;
		TextView txtEspecialidades;
		TextView txtClinicas;
	}

}
