package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Vitor
 */
public class Especialidade implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8L;
	
	
	private Integer codEspecialidade;
    private String descricao;
    
    public Especialidade(){
    	
    }
    
    

    public Especialidade(Integer codEspecialidade, String descricao) {
		super();
		this.codEspecialidade = codEspecialidade;
		this.descricao = descricao;
	}
    



	/**
     * @return the codEspecialidade
     */
    public Integer getCodEspecialidade() {
        return codEspecialidade;
    }

    /**
     * @param codEspecialidade the codEspecialidade to set
     */
    public void setCodEspecialidade(Integer codEspecialidade) {
        this.codEspecialidade = codEspecialidade;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return descricao;
    }

}