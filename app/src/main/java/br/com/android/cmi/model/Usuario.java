package br.com.android.cmi.model;

import java.io.Serializable;

public class Usuario implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer codUsuario;
    private String login;
    private String senha;
    private Perfil perfil;
    private Integer situacao;

    
    private Clinica clinica;
    private Funcionario funcionario;
    private Paciente paciente;
    
    public Usuario(){
        clinica = new Clinica();
        funcionario = new Funcionario();
        paciente = new Paciente();
        perfil = new Perfil();
    }
    
    public Usuario(Integer codUsuario, String login, String senha, 
            Perfil perfil, Clinica clinica, Funcionario funcionario,
            Paciente paciente, Integer situacao) {
        this.codUsuario = codUsuario;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
        this.clinica = clinica;
        this.funcionario = funcionario;
        this.paciente = paciente;
        this.situacao = situacao;
    }
    
    public Usuario(Integer codUsuario, String login, String senha, 
            Perfil perfil, Clinica clinica, Funcionario funcionario) {
        this(codUsuario, login, senha, perfil, clinica, funcionario,
                new Paciente(), 1);
    }
    
    public Usuario(Integer codUsuario, String login, String senha, Perfil perfil) {
        this(codUsuario, login, senha, perfil, 
                new Clinica(), new Funcionario());
    }
    
    public Usuario(Integer codUsuario, String login, String senha) {
        this(codUsuario, login, senha, new Perfil());
    }
    
    public Usuario(String login, String senha) {
        this();
        this.login = login;
        this.senha = senha;
    }
    
    

    /**
     * @return the codUsuario
     */
    public Integer getCodUsuario() {
        return codUsuario;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the clinica
     */
    public Clinica getClinica() {
        return clinica;
    }

    /**
     * @param clinica the clinica to set
     */
    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    /**
     * @return the perfil
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
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
     * @return the situacao
     */
    public Integer getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }
    
    
    public boolean isAtivo() {
        return situacao==1?true:false;
    }

    public void setAtivo(boolean situacao) {
        this.situacao = situacao?1:0;
    }
	
}
