/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Vitor
 */
public class Disponibilidade implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	
	
	private Integer codDisponibilidade;
    private Date horaInicial;
    private Date horaFinal;
    private DiaSemana diaSemana;
    private Funcionario funcionario;
    private String dispExib;
    
    public Disponibilidade(){
        this.funcionario = new Funcionario();
        horaInicial = new Date();
        horaFinal = new Date();
    }
    
    public Disponibilidade(Funcionario func){
        this.funcionario = func;
    }
    
    /**
     * @return the codDisponibilidade
     */
    public Integer getCodDisponibilidade() {
        return codDisponibilidade;
    }

    /**
     * @param codDisponibilidade the codDisponibilidade to set
     */
    public void setCodDisponibilidade(Integer codDisponibilidade) {
        this.codDisponibilidade = codDisponibilidade;
    }

    /**
     * @return the horaInicial
     */
    public Date getHoraInicial() {
        return horaInicial;
    }

    /**
     * @param horaInicial the horaInicial to set
     */
    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    /**
     * @return the horaFinal
     */
    public Date getHoraFinal() {
        return horaFinal;
    }

    /**
     * @param horaFinal the horaFinal to set
     */
    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }


    /**
     * @return the funcionario
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the diaSemana
     */
    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    /**
     * @param diaSemana the diaSemana to set
     */
    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    /**
     * @return the dispExib
     */
    public String getDispExib() {
        
        DateFormat df = new SimpleDateFormat("HH:mm");
        
        String txt = df.format(horaInicial) + " às " + df.format(horaFinal);
        
        return txt;
    }

    /**
     * @param dispExib the dispExib to set
     */
    public void setDispExib(String dispExib) {
        this.dispExib = dispExib;
    }
    
    
    
    
    
    

	@Override
	public String toString() {
		return "Disponibilidade [codDisponibilidade=" + codDisponibilidade
				+ ", horaInicial=" + horaInicial + ", horaFinal=" + horaFinal
				+ ", diaSemana=" + diaSemana + ", funcionario=" + funcionario
				+ ", dispExib=" + dispExib + "]";
	}
	

}
