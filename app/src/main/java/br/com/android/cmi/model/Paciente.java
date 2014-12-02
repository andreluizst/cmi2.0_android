/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class Paciente extends Pessoa implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private List<PlanoDeSaude> planos;
    
    public Paciente() {
        
        planos = new ArrayList<PlanoDeSaude>();
        
    }

    /**
     * @return the planos
     */
    public List<PlanoDeSaude> getPlanos() {
        return planos;
    }

    /**
     * @param planos the planos to set
     */
    public void setPlanos(List<PlanoDeSaude> planos) {
        this.planos = planos;
    }

}
