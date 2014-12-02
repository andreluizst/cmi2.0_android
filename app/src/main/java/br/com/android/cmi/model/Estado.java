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
public class Estado implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7L;
	
	
	private String uf;
    private String descricao;
    

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    
    
	@Override
	public String toString() {
		return uf;
	}
    
}
