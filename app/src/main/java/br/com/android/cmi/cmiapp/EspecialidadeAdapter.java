package br.com.android.cmi.cmiapp;

import java.util.List;

import br.com.android.cmi.model.Especialidade;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EspecialidadeAdapter extends ArrayAdapter<Especialidade> {

	public EspecialidadeAdapter(Context context, List<Especialidade> especialidades) {
		super(context, 0, especialidades);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//return super.getView(position, convertView, parent);
		ViewHolder holder;
		Especialidade especialidade = getItem(position);
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_list, parent, false);
			holder = new ViewHolder();
			holder.txtEspecialidade = (TextView)convertView.findViewById(R.id.itemText);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.txtEspecialidade.setText(especialidade.getDescricao());
		return convertView;
	}
	
	static class ViewHolder{
		TextView txtEspecialidade;
	}

}
