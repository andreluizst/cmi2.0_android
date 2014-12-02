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
 * @author Bruno
 */
public class Funcionario implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private Integer codFuncionario;
    private String nome;
    private String registroConselho;
    private String email;
    private String telefone1;
    private String telefone2;
    private String rg;
    private String cpf;
    private Date dataNascimento;   
    private List<Especialidade> especialidades;
    private List<Clinica> clinicas;
    
    public Funcionario()
    {
        this.especialidades = new ArrayList<>();        
        this.clinicas = new ArrayList<>();
    }   

    /**
     * @return the codFuncionario
     */
    public Integer getCodFuncionario() {
        return codFuncionario;
    }

    /**
     * @param codFuncionario the codFuncionario to set
     */
    public void setCodFuncionario(Integer codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the registroConselho
     */
    public String getRegistroConselho() {
        return registroConselho;
    }

    /**
     * @param registroConselho the registroConselho to set
     */
    public void setRegistroConselho(String registroConselho) {
        this.registroConselho = registroConselho;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the telefone1
     */
    public String getTelefone1() {
        return telefone1;
    }

    /**
     * @param telefone1 the telefone1 to set
     */
    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    /**
     * @return the telefone2
     */
    public String getTelefone2() {
        return telefone2;
    }

    /**
     * @param telefone2 the telefone2 to set
     */
    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the dataNascimento
     */
    public Date getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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
     * @return the clinicas
     */
    public List<Clinica> getClinicas() {
        return clinicas;
    }

    /**
     * @param clinicas the clinicas to set
     */
    public void setClinicas(List<Clinica> clinicas) {
        this.clinicas = clinicas;
    }
    
    
    

	public Funcionario(Integer codFuncionario, String nome,
			String registroConselho, String email, String telefone1,
			String telefone2, String rg, String cpf, Date dataNascimento,
			List<Especialidade> especialidades, List<Clinica> clinicas) {
		this();
		this.codFuncionario = codFuncionario;
		this.nome = nome;
		this.registroConselho = registroConselho;
		this.email = email;
		this.telefone1 = telefone1;
		this.telefone2 = telefone2;
		this.rg = rg;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.especialidades.addAll(especialidades);
		this.clinicas.addAll(clinicas);
	}

	@Override
	public String toString() {
		return "Funcionario [nome=" + nome + ", registroConselho="
				+ registroConselho + ", especialidades=" + especialidades + "]";
	}
	
	@Override
	public Object clone() {
		Funcionario obj = new Funcionario(codFuncionario, nome,
				registroConselho, email, telefone1,
				telefone2, rg, cpf, dataNascimento, especialidades, clinicas);
		return obj;
	}
    
    
}
