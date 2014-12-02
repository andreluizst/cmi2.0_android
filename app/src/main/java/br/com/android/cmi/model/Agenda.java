/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Vitor
 */

public class Agenda implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 41L;
	
	
	
	private Date data;
    private String dia;
    private Consulta consulta;
    private List<Disponibilidade> disponibilidades;
    private boolean existeVaga;
    private Cor padrao;
    private Cor hover;
    private String cursor;

    
    
    public Agenda(){
        consulta = new Consulta();
        disponibilidades = new ArrayList<>();
        padrao = new Cor(255,255,255);
        hover = new Cor(255,255,255);
        cursor="default";
    }
    
    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the dia
     */
    public String getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * @return the padrao
     */
    public Cor getPadrao() {
        return padrao;
    }

    /**
     * @param padrao the padrao to set
     */
    public void setPadrao(Cor padrao) {
        this.padrao = padrao;
    }

    /**
     * @return the hover
     */
    public Cor getHover() {
        return hover;
    }

    /**
     * @param hover the hover to set
     */
    public void setHover(Cor hover) {
        this.hover = hover;
    }

    /**
     * @return the existeVaga
     */
    public boolean isExisteVaga() {
        return existeVaga;
    }

    /**
     * @param existeVaga the existeVaga to set
     */
    public void setExisteVaga(boolean existeVaga) {
        this.existeVaga = existeVaga;
    }

    /**
     * @return the disponibilidades
     */
    public List<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    /**
     * @param disponibilidades the disponibilidades to set
     */
    public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    /**
     * @return the cursor
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

}
