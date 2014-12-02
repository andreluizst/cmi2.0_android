/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Neto Andrade
 */
public class Cidade implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	
	private Integer id;
    private String descricao;
    private Estado estado;

    
    
    public Cidade(){
        this.estado = new Estado();
    }

    public Cidade(Integer id, String descricao) {
		this();
		this.id = id;
		this.descricao = descricao;
	}

	public Cidade(Integer id, String descricao, Estado estado) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.estado = estado;
	}
	
	

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    

    @Override
    public String toString() {
    	return descricao;
    }
}
