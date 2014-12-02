/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Jéssica
 */
public class PlanoDeSaude implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	
	private Integer id;
    private String descricao;
    
    public PlanoDeSaude(){
    	
    }
    
    public PlanoDeSaude(Integer id, String descricao){
    	this.id = id;
    	this.descricao = descricao;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    
    
    
    @Override
    public String toString() {
    	return descricao;
    }
}
