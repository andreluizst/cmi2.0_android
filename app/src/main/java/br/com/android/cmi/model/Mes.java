package br.com.android.cmi.model;

import java.io.Serializable;

public class Mes implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 111L;
	
	public static enum ToStringBehavior {MES_ANO, MES};
	
	Integer id;
	Integer ano;
	String descricao;
	ToStringBehavior toStringBehavior;
	
	public Mes(Integer id, Integer ano, String descricao){
		this.id = id;
		this.ano = ano;
		this.descricao = descricao;
		toStringBehavior = ToStringBehavior.MES_ANO;
	}
	
	public Mes(Integer id, String descricao){
		this(id, null, descricao);
	}
	
	public Mes(Integer id, String descricao, ToStringBehavior behavior){
		this(id, null, descricao);
		this.toStringBehavior = behavior;
	}
	
	
	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getAno() {
		return ano;
	}



	public void setAno(Integer ano) {
		this.ano = ano;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	@Override
	public String toString() {
		if (this.toStringBehavior == ToStringBehavior.MES)
			return descricao;
		return descricao + " / " + String.valueOf(ano);
	}
}
