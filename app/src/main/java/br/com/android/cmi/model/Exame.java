/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Bruno
 */
public class Exame implements Serializable 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 162L;
	
	
	private Integer codExame;
    private String Descricao;
    
    public Exame()
    {
        
    }

    /**
     * @return the codExame
     */
    public Integer getCodExame() {
        return codExame;
    }

    /**
     * @param codExame the codExame to set
     */
    public void setCodExame(Integer codExame) {
        this.codExame = codExame;
    }

    /**
     * @return the Descricao
     */
    public String getDescricao() {
        return Descricao;
    }

    /**
     * @param Descricao the Descricao to set
     */
    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }
}
