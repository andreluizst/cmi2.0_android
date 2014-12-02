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
 * @author Bruno
 */
public class Atendimento implements Serializable {    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 161L;
	
	
	private Integer codAtendimento;
    private String anamnese;
    private String diagnostico;
    private String tratamento;  
    private Consulta consulta;  
    private int avaliacao;
    private String comentario;
    private List<Exame> exames;
    
    public Atendimento()
    {       
        this.consulta = new Consulta();
        this.exames = new ArrayList<>();
    }            

    /**
     * @return the codAtendimento
     */
    public Integer getCodAtendimento() {
        return codAtendimento;
    }

    /**
     * @param codAtendimento the codAtendimento to set
     */
    public void setCodAtendimento(Integer codAtendimento) {
        this.codAtendimento = codAtendimento;
    }

    /**
     * @return the anamnese
     */
    public String getAnamnese() {
        return anamnese;
    }

    /**
     * @param anamnese the anamnese to set
     */
    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    /**
     * @return the diagnostico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * @param diagnostico the diagnostico to set
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * @return the tratamento
     */
    public String getTratamento() {
        return tratamento;
    }

    /**
     * @param tratamento the tratamento to set
     */
    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }

    /**
     * @return the consulta
     */
    public Consulta getConsulta() {
        return consulta;
    }

    /**
     * @param consulta the consulta to set
     */
    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    /**
     * @return the exames
     */
    public List<Exame> getExames() {
        return exames;
    }

    /**
     * @param exames the exames to set
     */
    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }

    /**
     * @return the avaliacao
     */
    public int getAvaliacao() {
        return avaliacao;
    }

    /**
     * @param avaliacao the avaliacao to set
     */
    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
