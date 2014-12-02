/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.android.cmi.model;

import java.io.Serializable;

import android.util.Log;

/**
 *
 * @author Bruno
 */
public enum DiaSemana implements Serializable{
    
    SEGUNDA("Segunda-Feira",1),
    TERCA("Terça-Feira",2),
    QUARTA("Quarta-Feira",3),
    QUINTA("Quinta-Feira",4),
    SEXTA("Sexta-Feira",5),
    SABADO("Sábado",6);
    
    private String nome;
    private int valor;
    
    private DiaSemana(String nome, int valor)
    {
        this.nome = nome;
        this.valor = valor;
    }

    /**
     * @return the nome
     */

    public String getNome() {
        return nome;
    }

    /**
     * @return the valor
     */

    public int getValor() {
        return valor;
    }
    
    public static DiaSemana getByValor(int valor) {
        for (DiaSemana perfil : values()) {
            if (perfil.getValor()== valor) {
                return perfil;
            }
        }
        return null;
    }    
    
    
    public static DiaSemana getByNome(String nome) {
        for (DiaSemana perfil : values()) {
        	
        	Log.e("DiaSemana", perfil.getNome());
        	
            if (perfil.getNome().equalsIgnoreCase(nome)) {
                return perfil;
            }
        }
        return null;
    }
}
