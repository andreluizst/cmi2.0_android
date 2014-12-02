package br.com.android.cmi.model;

import java.io.Serializable;

public class User implements Serializable{
    private long id;
    private String login;
    private String senha;

    public User(long id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    public User(String login, String senha) {
        this(0, login, senha);
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the nome
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
}