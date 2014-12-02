/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.android.cmi.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Bruno
 */
public class Consulta implements Serializable 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 51L;
	
	
	
	private Integer codConsulta;
    private Date dataConsulta;
    private int situacao;
    private Paciente paciente;
    private Disponibilidade disponibilidade;   
    private Funcionario funcionario;
    
    public Consulta()
    {
        this.funcionario = new Funcionario();
        this.paciente = new Paciente();
        this.disponibilidade = new Disponibilidade();
    }

    /**
     * @return the codConsulta
     */
    public Integer getCodConsulta() {
        return codConsulta;
    }

    /**
     * @param codConsulta the codConsulta to set
     */
    public void setCodConsulta(Integer codConsulta) {
        this.codConsulta = codConsulta;
    }

    /**
     * @return the dataConsulta
     */
    public Date getDataConsulta() {
        return dataConsulta;
    }

    /**
     * @param dataConsulta the dataConsulta to set
     */
    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    /**
     * @return the situacao
     */
    public int getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the disponibilidade
     */
    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    /**
     * @param disponibilidade the disponibilidade to set
     */
    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
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
}
