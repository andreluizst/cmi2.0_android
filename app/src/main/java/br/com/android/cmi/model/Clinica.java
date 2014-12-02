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
 * @author Neto Andrade
 */
public class Clinica implements Serializable{
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	
	private String cpf_cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String telefone1;
    private String telefone2;
    private Endereco endereco;
    private List<Especialidade> especialidades;
    private List<PlanoDeSaude> planos;


    public Clinica() {
        this.planos = new ArrayList<PlanoDeSaude>();
        this.especialidades = new ArrayList<Especialidade>();
        this.endereco = new Endereco();
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


   @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cpf_cnpj == null) ? 0 : cpf_cnpj.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Clinica other = (Clinica) obj;
        if (cpf_cnpj == null) {
            if (other.cpf_cnpj != null) {
                return false;
            }
        } else if (!cpf_cnpj.equals(other.cpf_cnpj)) {
            return false;
        }
        return true;
    }

    /**
     * @return the especialidades
     */
    public List<Especialidade> getEspecialidades() {
        return especialidades;
    }

    /**
     * @param especialidades the especialidades to set
     */
    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
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
    
    
    
    @Override
    public String toString() {
    	return nomeFantasia + " (" + razaoSocial + ")" + " - " + telefone1 + " - " + endereco.toString();
    }
}
