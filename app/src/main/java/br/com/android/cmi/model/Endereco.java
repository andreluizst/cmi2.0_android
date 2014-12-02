package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Neto Andrade
 */
public class Endereco implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	
	
	private Integer id;
    private String logradouro;
    private Cidade cidade;
    private String bairro;
    private String numero;
    private String complemento;
    private String cep;

    public Endereco() {
        
        this.cidade = new Cidade();
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
    
    
    @Override
    public String toString() {
    	String endereco = "";
    	endereco = logradouro;
    	if (numero != null)
    		endereco += ", " + numero;
    	if (complemento != null)
    		endereco += " - " + complemento;
    	if (bairro != null)
    		endereco += " - " + bairro;
    	return endereco + " - " + cidade.getDescricao() + "/" + cidade.getEstado().getUf() + " - " + cep;  
    }

}
