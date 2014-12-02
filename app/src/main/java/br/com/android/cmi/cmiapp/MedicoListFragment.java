package br.com.android.cmi.cmiapp;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import br.com.android.cmi.model.Funcionario;
import br.com.android.cmi.model.Usuario;
import br.com.android.cmi.util.Util;

public class MedicoListFragment extends Fragment implements OnChildClickListener {
	
	private ArrayList<Funcionario> mLista;
	private MedicosExpandableListAdapter mExpAdapter;
	private ExpandableListView mExpListView;
	private boolean fragmentAgendaCriado = false;
	private FrameLayout mFrameAgenda;
	private boolean mFameAgendaVisible = false;

	public static MedicoListFragment novaInstancia(ArrayList<Funcionario> lista) {
		Bundle parametros = new Bundle();
		parametros.putSerializable(Util.LISTA_FUNCIONARIOS, lista);
		MedicoListFragment novoFragment = new MedicoListFragment();
		novoFragment.setArguments(parametros);
		return novoFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		//setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			mLista = (ArrayList<Funcionario>)savedInstanceState.getSerializable(Util.LISTA_FUNCIONARIOS);
		}else{
			mLista = (ArrayList<Funcionario>)getArguments().getSerializable(Util.LISTA_FUNCIONARIOS);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_list_medicos, null);
		mExpListView = (ExpandableListView)layout.findViewById(R.id.expListViewMedicos);
		mExpAdapter = new MedicosExpandableListAdapter(getActivity(), mLista);
		mExpListView.setAdapter(mExpAdapter);
		mExpListView.setOnChildClickListener(this);
		return layout;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Funcionario funcionario = (Funcionario)mLista.get(groupPosition).clone();
		funcionario.getClinicas().clear();
		funcionario.getClinicas().add(mLista.get(groupPosition).getClinicas().get(childPosition));
		if (isTablet()){
			if (getActivity() instanceof OnMedicoListClick)
				((OnMedicoListClick)getActivity()).onSubItemMedicoClick(funcionario);
		}else{
			Intent it = new Intent(getActivity(), AgendarConsultaActivity.class);
			Bundle parametros = new Bundle();
			parametros.putSerializable(Util.USER_EXTRA, Util.usuarioAtual);
			parametros.putSerializable(Util.MEDICO_EXTRA, funcionario);
			it.putExtras(parametros);
			startActivity(it);
		}
		return false;
	}
	
	private boolean isTablet(){
		return getActivity().findViewById(R.id.fragmentAgendaMedico_tablet) != null;
	}
	
	private void destruirAgendarConsultaFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		AgendarConsultaFragment fragment = (AgendarConsultaFragment)fm.findFragmentByTag(Util.TAG_AGENDAR_CONSULTA_TABLET);
		ft.remove(fragment);
		ft.commit();
		if (isTablet()){
			mFrameAgenda = (FrameLayout)getActivity().findViewById(R.id.fragmentAgendaMedico_tablet);
			mFrameAgenda.setVisibility(View.GONE);
		}
	}
	
	public interface OnMedicoListClick{
		public void onSubItemMedicoClick(Funcionario funcionario);
	}
	

}
